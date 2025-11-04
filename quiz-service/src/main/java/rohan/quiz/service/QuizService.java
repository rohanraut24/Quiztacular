package rohan.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rohan.quiz.dto.*;
import rohan.quiz.entity.QuizAttempt;
import rohan.quiz.entity.UserStatistics;
import rohan.quiz.repository.QuizAttemptRepository;
import rohan.quiz.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final TriviaApiService triviaApiService;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserStatisticsRepository userStatisticsRepository;
    private final ObjectMapper objectMapper;

    /**
     * Generate a new quiz from Open Trivia DB
     */
    @Transactional
    public QuizResponse generateQuiz(QuizRequest request) {
        String username = getCurrentUsername();

        // Fetch quiz from Trivia API
        Integer categoryId = triviaApiService.getCategoryId(request.getCategory());
        TriviaApiResponse triviaResponse = triviaApiService.fetchQuiz(
                categoryId,
                request.getDifficulty(),
                request.getAmount()
        );

        // Create quiz attempt record
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(getCurrentUserId());
        attempt.setUsername(username);
        attempt.setCategory(request.getCategory());
        attempt.setDifficulty(request.getDifficulty());
        attempt.setTotalQuestions(triviaResponse.getResults().size());
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(QuizAttempt.QuizStatus.IN_PROGRESS);

        // Store questions data as JSON
        try {
            String questionsJson = objectMapper.writeValueAsString(triviaResponse.getResults());
            attempt.setQuestionsData(questionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing quiz data");
        }

        attempt = quizAttemptRepository.save(attempt);

        // Prepare response with questions (without correct answers)
        List<QuestionDTO> questions = new ArrayList<>();
        int questionNumber = 1;

        for (TriviaApiResponse.TriviaQuestion tq : triviaResponse.getResults()) {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setQuestionNumber(questionNumber++);
            questionDTO.setQuestion(HtmlUtils.htmlUnescape(tq.getQuestion()));
            questionDTO.setType(tq.getType());

            // Mix correct and incorrect answers
            List<String> options = new ArrayList<>();
            options.add(HtmlUtils.htmlUnescape(tq.getCorrectAnswer()));
            tq.getIncorrectAnswers().forEach(ans -> options.add(HtmlUtils.htmlUnescape(ans)));
            Collections.shuffle(options);

            questionDTO.setOptions(options);
            questions.add(questionDTO);
        }

        QuizResponse response = new QuizResponse();
        response.setAttemptId(attempt.getId());
        response.setCategory(request.getCategory());
        response.setDifficulty(request.getDifficulty());
        response.setTotalQuestions(questions.size());
        response.setQuestions(questions);
        response.setStartTime(attempt.getStartTime().toString());

        log.info("Quiz generated for user: {} - Attempt ID: {}", username, attempt.getId());

        return response;
    }

    /**
     * Submit quiz answers and calculate score
     */
    @Transactional
    public QuizResultResponse submitQuiz(SubmitQuizRequest request) {
        String username = getCurrentUsername();

        QuizAttempt attempt = quizAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));

        if (!attempt.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to quiz attempt");
        }

        if (attempt.getStatus() == QuizAttempt.QuizStatus.COMPLETED) {
            throw new RuntimeException("Quiz already submitted");
        }

        // Parse stored questions
        List<TriviaApiResponse.TriviaQuestion> questions;
        try {
            questions = Arrays.asList(
                    objectMapper.readValue(attempt.getQuestionsData(),
                            TriviaApiResponse.TriviaQuestion[].class)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing quiz data");
        }

        // Calculate score
        int correctCount = 0;
        List<QuestionResultDTO> results = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            TriviaApiResponse.TriviaQuestion question = questions.get(i);
            String userAnswer = request.getAnswers().get(i + 1);
            String correctAnswer = HtmlUtils.htmlUnescape(question.getCorrectAnswer());

            boolean isCorrect = userAnswer != null &&
                    userAnswer.equalsIgnoreCase(correctAnswer);

            if (isCorrect) {
                correctCount++;
            }

            QuestionResultDTO resultDTO = new QuestionResultDTO();
            resultDTO.setQuestionNumber(i + 1);
            resultDTO.setQuestion(HtmlUtils.htmlUnescape(question.getQuestion()));
            resultDTO.setUserAnswer(userAnswer);
            resultDTO.setCorrectAnswer(correctAnswer);
            resultDTO.setIsCorrect(isCorrect);
            results.add(resultDTO);
        }

        // Update attempt
        attempt.setEndTime(LocalDateTime.now());
        attempt.setCorrectAnswers(correctCount);
        attempt.setScore(calculateScore(correctCount, questions.size()));
        attempt.setTimeTakenSeconds(
                Duration.between(attempt.getStartTime(), attempt.getEndTime()).getSeconds()
        );
        attempt.setStatus(QuizAttempt.QuizStatus.COMPLETED);
        quizAttemptRepository.save(attempt);

        // Update user statistics
        updateUserStatistics(attempt);

        // Prepare response
        QuizResultResponse response = new QuizResultResponse();
        response.setAttemptId(attempt.getId());
        response.setTotalQuestions(questions.size());
        response.setCorrectAnswers(correctCount);
        response.setWrongAnswers(questions.size() - correctCount);
        response.setScore(attempt.getScore());
        response.setPercentage(attempt.getScorePercentage());
        response.setTimeTakenSeconds(attempt.getTimeTakenSeconds());
        response.setTimeTaken(formatTime(attempt.getTimeTakenSeconds()));
        response.setResults(results);

        log.info("Quiz submitted - User: {}, Score: {}/{}",
                username, correctCount, questions.size());

        return response;
    }

    /**
     * Get user's quiz history
     */
    public List<QuizAttempt> getUserHistory() {
        String username = getCurrentUsername();
        return quizAttemptRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    /**
     * Get leaderboard
     */
    public LeaderboardResponse getLeaderboard(String category, Integer limit) {
        List<QuizAttempt> attempts;

        if (category != null && !category.isEmpty()) {
            attempts = quizAttemptRepository.findTopScoresByCategory(category);
        } else {
            attempts = quizAttemptRepository.findGlobalLeaderboard();
        }

        List<LeaderboardEntry> entries = attempts.stream()
                .limit(limit != null ? limit : 10)
                .map(attempt -> {
                    LeaderboardEntry entry = new LeaderboardEntry();
                    entry.setUsername(attempt.getUsername());
                    entry.setScore(attempt.getScore());
                    entry.setCorrectAnswers(attempt.getCorrectAnswers());
                    entry.setTotalQuestions(attempt.getTotalQuestions());
                    entry.setPercentage(attempt.getScorePercentage());
                    entry.setTimeTaken(formatTime(attempt.getTimeTakenSeconds()));
                    return entry;
                })
                .collect(Collectors.toList());

        // Assign ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }

        LeaderboardResponse response = new LeaderboardResponse();
        response.setCategory(category != null ? category : "Global");
        response.setEntries(entries);

        return response;
    }

    /**
     * Get user statistics
     */
    public UserStatsResponse getUserStatistics() {
        String username = getCurrentUsername();
        Long userId = getCurrentUserId();

        UserStatistics stats = userStatisticsRepository.findByUserId(userId)
                .orElse(createDefaultStatistics(userId, username));

        UserStatsResponse response = new UserStatsResponse();
        response.setUsername(stats.getUsername());
        response.setTotalQuizzesAttempted(stats.getTotalQuizzesAttempted());
        response.setTotalQuizzesCompleted(stats.getTotalQuizzesCompleted());
        response.setTotalQuestionsAnswered(stats.getTotalQuestionsAnswered());
        response.setTotalCorrectAnswers(stats.getTotalCorrectAnswers());
        response.setAccuracyPercentage(stats.getAccuracyPercentage());
        response.setTotalScore(stats.getTotalScore());
        response.setAverageScore(stats.getAverageScore());
        response.setHighestScore(stats.getHighestScore());

        return response;
    }

    // Helper methods

    private void updateUserStatistics(QuizAttempt attempt) {
        Long userId = attempt.getUserId();
        String username = attempt.getUsername();

        UserStatistics stats = userStatisticsRepository.findByUserId(userId)
                .orElse(createDefaultStatistics(userId, username));

        stats.setTotalQuizzesAttempted(stats.getTotalQuizzesAttempted() + 1);
        stats.setTotalQuizzesCompleted(stats.getTotalQuizzesCompleted() + 1);
        stats.setTotalQuestionsAnswered(stats.getTotalQuestionsAnswered() + attempt.getTotalQuestions());
        stats.setTotalCorrectAnswers(stats.getTotalCorrectAnswers() + attempt.getCorrectAnswers());
        stats.setTotalScore(stats.getTotalScore() + attempt.getScore());
        stats.setAverageScore((double) stats.getTotalScore() / stats.getTotalQuizzesCompleted());

        if (attempt.getScore() > stats.getHighestScore()) {
            stats.setHighestScore(attempt.getScore());
        }

        userStatisticsRepository.save(stats);
    }

    private UserStatistics createDefaultStatistics(Long userId, String username) {
        UserStatistics stats = new UserStatistics();
        stats.setUserId(userId);
        stats.setUsername(username);
        return stats;
    }

    private int calculateScore(int correctAnswers, int totalQuestions) {
        return (correctAnswers * 100) / totalQuestions;
    }

    private String formatTime(Long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%dm %ds", minutes, secs);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private Long getCurrentUserId() {
        // For now, return a mock ID. In production, you'd fetch this from auth-service
        // or include it in JWT token claims
        return 1L; // TODO: Get actual user ID from JWT or auth-service
    }
}