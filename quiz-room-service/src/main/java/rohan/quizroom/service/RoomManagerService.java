package rohan.quizroom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rohan.quizroom.dto.*;
import rohan.quizroom.entity.*;
import rohan.quizroom.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
public class RoomManagerService {

    private final QuizRoomRepository roomRepository;
    private final RoomPlayerRepository playerRepository;
    private final RoomAnswerRepository answerRepository;
    private final TriviaApiService triviaApiService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Create a new quiz room
     */
    @Transactional
    public RoomCreatedResponse createRoom(CreateRoomRequest request, String username) {
        log.info("Creating room: {} by {}", request.getRoomName(), username);

        // Generate unique room code
        String roomCode = generateRoomCode();

        // Fetch questions from Trivia API
        Integer categoryId = triviaApiService.getCategoryId(request.getCategory());
        TriviaApiResponse triviaResponse = triviaApiService.fetchQuiz(
                categoryId,
                request.getDifficulty(),
                request.getTotalQuestions()
        );

        // Create room
        QuizRoom room = new QuizRoom();
        room.setRoomCode(roomCode);
        room.setRoomName(request.getRoomName());
        room.setHostUserId(1L); // TODO: Get from JWT
        room.setHostUsername(username);
        room.setCategory(request.getCategory());
        room.setDifficulty(request.getDifficulty());
        room.setTotalQuestions(request.getTotalQuestions());
        room.setMaxPlayers(request.getMaxPlayers());
        room.setStatus(QuizRoom.RoomStatus.WAITING);

        try {
            String questionsJson = objectMapper.writeValueAsString(triviaResponse.getResults());
            room.setQuestionsData(questionsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing questions");
        }

        room = roomRepository.save(room);

        // Add host as first player
        RoomPlayer host = new RoomPlayer();
        host.setRoom(room);
        host.setUserId(1L); // TODO: Get from JWT
        host.setUsername(username);
        host.setIsReady(true); // Host is always ready
        playerRepository.save(host);

        log.info("Room created: {} by {}", roomCode, username);

        return new RoomCreatedResponse(
                roomCode,
                request.getRoomName(),
                request.getCategory(),
                request.getDifficulty(),
                request.getTotalQuestions(),
                request.getMaxPlayers(),
                "Room created successfully! Share code: " + roomCode
        );
    }

    /**
     * Join existing room
     */
    @Transactional
    public void joinRoom(JoinRoomRequest request, String username) {
        log.info("{} attempting to join room: {}", username, request.getRoomCode());

        QuizRoom room = roomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != QuizRoom.RoomStatus.WAITING) {
            throw new RuntimeException("Room has already started");
        }

        if (room.getCurrentPlayers() >= room.getMaxPlayers()) {
            throw new RuntimeException("Room is full");
        }

        Long userId = 1L; // TODO: Get from JWT

        // Check if already in room
        if (playerRepository.existsByRoomIdAndUserId(room.getId(), userId)) {
            throw new RuntimeException("Already in this room");
        }

        // Add player
        RoomPlayer player = new RoomPlayer();
        player.setRoom(room);
        player.setUserId(userId);
        player.setUsername(username);
        playerRepository.save(player);

        room.setCurrentPlayers(room.getCurrentPlayers() + 1);
        roomRepository.save(room);

        log.info("{} joined room: {}", username, request.getRoomCode());

        // Broadcast room update to all players
        broadcastRoomUpdate(room);
    }

    /**
     * Player marks ready
     */
    @Transactional
    public void playerReady(PlayerReadyRequest request, String username) {
        log.info("{} marking ready in room: {}", username, request.getRoomCode());

        QuizRoom room = roomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomPlayer player = playerRepository.findByRoomIdAndUsername(room.getId(), username)
                .orElseThrow(() -> new RuntimeException("Not in this room"));

        player.setIsReady(true);
        playerRepository.save(player);

        // Broadcast room update
        broadcastRoomUpdate(room);

        // Check if all players are ready
        List<RoomPlayer> players = playerRepository.findByRoomId(room.getId());
        boolean allReady = players.stream().allMatch(RoomPlayer::getIsReady);

        if (allReady && players.size() >= 2) {
            log.info("All players ready in room: {}. Starting countdown.", request.getRoomCode());
            // Start countdown in separate thread
            new Thread(() -> startRoomCountdown(room)).start();
        }
    }

    /**
     * Start quiz room with countdown
     */
    private void startRoomCountdown(QuizRoom room) {
        room.setStatus(QuizRoom.RoomStatus.STARTING);
        roomRepository.save(room);

        log.info("Starting countdown for room: {}", room.getRoomCode());

        // Broadcast countdown: 3... 2... 1...
        for (int i = 3; i > 0; i--) {
            RoomUpdateMessage message = buildRoomUpdateMessage(room);
            message.setStatus("STARTING");
            message.setCountdown(i);

            messagingTemplate.convertAndSend(
                    "/topic/room/" + room.getRoomCode(),
                    message
            );

            log.debug("Countdown {} for room {}", i, room.getRoomCode());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Countdown interrupted", e);
                return;
            }
        }

        // Start quiz
        startQuiz(room);
    }

    /**
     * Start the actual quiz
     */
    private void startQuiz(QuizRoom room) {
        log.info("Starting quiz for room: {}", room.getRoomCode());

        room.setStatus(QuizRoom.RoomStatus.IN_PROGRESS);
        room.setStartedAt(LocalDateTime.now());
        roomRepository.save(room);

        // Broadcast room status change
        broadcastRoomUpdate(room);

        // Send first question after 1 second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        broadcastQuestion(room, 1);
    }

    /**
     * Broadcast question to all players
     */
    private void broadcastQuestion(QuizRoom room, Integer questionNumber) {
        log.info("Broadcasting question {} for room: {}", questionNumber, room.getRoomCode());

        try {
            TriviaApiResponse.TriviaQuestion[] questions =
                    objectMapper.readValue(room.getQuestionsData(),
                            TriviaApiResponse.TriviaQuestion[].class);

            if (questionNumber > questions.length) {
                log.info("All questions completed for room: {}", room.getRoomCode());
                endQuiz(room);
                return;
            }

            TriviaApiResponse.TriviaQuestion q = questions[questionNumber - 1];

            QuestionBroadcast broadcast = new QuestionBroadcast();
            broadcast.setQuestionNumber(questionNumber);
            broadcast.setTotalQuestions(questions.length);
            broadcast.setQuestion(HtmlUtils.htmlUnescape(q.getQuestion()));
            broadcast.setType(q.getType());
            broadcast.setTimeLimit(30L); // 30 seconds per question

            // Mix correct and incorrect answers
            List<String> options = new ArrayList<>();
            options.add(HtmlUtils.htmlUnescape(q.getCorrectAnswer()));
            q.getIncorrectAnswers().forEach(ans ->
                    options.add(HtmlUtils.htmlUnescape(ans))
            );
            Collections.shuffle(options);

            broadcast.setOptions(options);

            messagingTemplate.convertAndSend(
                    "/topic/room/" + room.getRoomCode() + "/question",
                    broadcast
            );

            log.debug("Question {} sent to room {}", questionNumber, room.getRoomCode());

        } catch (JsonProcessingException e) {
            log.error("Error broadcasting question", e);
        }
    }

    // Helper method to generate unique room code
    private String generateRoomCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        do {
            code.setLength(0);
            for (int i = 0; i < 6; i++) {
                code.append(chars.charAt(random.nextInt(chars.length())));
            }
        } while (roomRepository.existsByRoomCode(code.toString()));

        return code.toString();
    }

    // Helper method to build room update message
    private RoomUpdateMessage buildRoomUpdateMessage(QuizRoom room) {
        List<RoomPlayer> players = playerRepository.findByRoomId(room.getId());

        List<PlayerInfo> playerInfos = players.stream().map(p -> {
            PlayerInfo info = new PlayerInfo();
            info.setUsername(p.getUsername());
            info.setAvatarUrl(p.getAvatarUrl());
            info.setCurrentScore(p.getCurrentScore());
            info.setCorrectAnswers(p.getCorrectAnswers());
            info.setIsReady(p.getIsReady());
            info.setIsHost(p.getUserId().equals(room.getHostUserId()));
            info.setIsConnected(p.getIsConnected());
            return info;
        }).collect(Collectors.toList());

        RoomUpdateMessage message = new RoomUpdateMessage();
        message.setRoomCode(room.getRoomCode());
        message.setRoomName(room.getRoomName());
        message.setStatus(room.getStatus().name());
        message.setCurrentPlayers(room.getCurrentPlayers());
        message.setMaxPlayers(room.getMaxPlayers());
        message.setPlayers(playerInfos);

        return message;
    }

    // Broadcast room update to all players
    private void broadcastRoomUpdate(QuizRoom room) {
        RoomUpdateMessage message = buildRoomUpdateMessage(room);
        messagingTemplate.convertAndSend(
                "/topic/room/" + room.getRoomCode(),
                message
        );
    }

    // End quiz implementation - continued in Part 2
    private void endQuiz(QuizRoom room) {
        log.info("Ending quiz for room: {}", room.getRoomCode());

        room.setStatus(QuizRoom.RoomStatus.COMPLETED);
        room.setEndedAt(LocalDateTime.now());
        roomRepository.save(room);

        // Get final rankings
        broadcastFinalResults(room);
    }

    private void broadcastFinalResults(QuizRoom room) {
        List<RoomPlayer> players = playerRepository.findByRoomIdOrderByScore(room.getId());

        List<FinalRanking> rankings = new ArrayList<>();
        int rank = 1;
        for (RoomPlayer p : players) {
            FinalRanking ranking = new FinalRanking();
            ranking.setRank(rank++);
            ranking.setUsername(p.getUsername());
            ranking.setTotalScore(p.getCurrentScore());
            ranking.setCorrectAnswers(p.getCorrectAnswers());
            ranking.setTotalQuestions(room.getTotalQuestions());
            ranking.setAccuracy((p.getCorrectAnswers() * 100.0) / room.getTotalQuestions());
            rankings.add(ranking);
        }

        String winner = players.isEmpty() ? "" : players.get(0).getUsername();

        QuizCompletedMessage completed = new QuizCompletedMessage();
        completed.setRoomCode(room.getRoomCode());
        completed.setWinnerUsername(winner);
        completed.setFinalRankings(rankings);

        messagingTemplate.convertAndSend(
                "/topic/room/" + room.getRoomCode() + "/completed",
                completed
        );

        log.info("Quiz completed in room: {} - Winner: {}", room.getRoomCode(), winner);
    }

    /**
     * Cleanup stale rooms (scheduled task)
     */
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    @Transactional
    public void cleanupStaleRooms() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30);
        List<QuizRoom> staleRooms = roomRepository.findByStatusAndCreatedAtBefore(
                QuizRoom.RoomStatus.WAITING, cutoff
        );

        for (QuizRoom room : staleRooms) {
            playerRepository.deleteByRoomId(room.getId());
            roomRepository.delete(room);
            log.info("Cleaned up stale room: {}", room.getRoomCode());
        }
    }
    /**
     * Handle player answer submission
     */
    @Transactional
    public void submitAnswer(SubmitAnswerRequest request, String username) {
        log.info("{} submitting answer for Q{} in room: {}",
                username, request.getQuestionNumber(), request.getRoomCode());

        QuizRoom room = roomRepository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != QuizRoom.RoomStatus.IN_PROGRESS) {
            throw new RuntimeException("Quiz is not in progress");
        }

        RoomPlayer player = playerRepository.findByRoomIdAndUsername(room.getId(), username)
                .orElseThrow(() -> new RuntimeException("Not in this room"));

        // Check if already answered this question
        if (answerRepository.existsByRoomIdAndUserIdAndQuestionNumber(
                room.getId(), player.getUserId(), request.getQuestionNumber())) {
            throw new RuntimeException("Already answered this question");
        }

        try {
            TriviaApiResponse.TriviaQuestion[] questions =
                    objectMapper.readValue(room.getQuestionsData(),
                            TriviaApiResponse.TriviaQuestion[].class);

            TriviaApiResponse.TriviaQuestion q = questions[request.getQuestionNumber() - 1];
            String correctAnswer = HtmlUtils.htmlUnescape(q.getCorrectAnswer());

            boolean isCorrect = request.getAnswer().equalsIgnoreCase(correctAnswer);
            int pointsEarned = 0;

            if (isCorrect) {
                // Calculate score based on time (faster = more points)
                // Base points: 10
                // Time bonus: max 10 points (30s limit)
                int basePoints = 10;
                long maxTimeMs = 30000; // 30 seconds
                long timeTaken = Math.min(request.getTimeTakenMs(), maxTimeMs);
                int timeBonus = (int) (10 * (maxTimeMs - timeTaken) / maxTimeMs);

                pointsEarned = basePoints + timeBonus;

                player.setCurrentScore(player.getCurrentScore() + pointsEarned);
                player.setCorrectAnswers(player.getCorrectAnswers() + 1);
            }

            player.setCurrentQuestion(request.getQuestionNumber());
            playerRepository.save(player);

            // Save answer record
            RoomAnswer answer = new RoomAnswer();
            answer.setRoomId(room.getId());
            answer.setUserId(player.getUserId());
            answer.setUsername(username);
            answer.setQuestionNumber(request.getQuestionNumber());
            answer.setAnswer(request.getAnswer());
            answer.setIsCorrect(isCorrect);
            answer.setPointsEarned(pointsEarned);
            answer.setTimeTakenMs(request.getTimeTakenMs());
            answerRepository.save(answer);

            // Broadcast answer result to all players
            AnswerResultBroadcast result = new AnswerResultBroadcast();
            result.setUsername(username);
            result.setQuestionNumber(request.getQuestionNumber());
            result.setIsCorrect(isCorrect);
            result.setPointsEarned(pointsEarned);
            result.setTotalScore(player.getCurrentScore());
            result.setTimeTakenMs(request.getTimeTakenMs());

            messagingTemplate.convertAndSend(
                    "/topic/room/" + room.getRoomCode() + "/answer",
                    result
            );

            log.debug("Answer processed: {} - Correct: {}, Points: {}",
                    username, isCorrect, pointsEarned);

            // Update live leaderboard
            broadcastLeaderboard(room);

            // Check if all players answered this question
            List<RoomPlayer> players = playerRepository.findByRoomId(room.getId());
            long answeredCount = answerRepository.findByRoomIdAndQuestionNumber(
                    room.getId(), request.getQuestionNumber()).size();

            if (answeredCount >= players.size()) {
                log.info("All players answered Q{} in room: {}",
                        request.getQuestionNumber(), room.getRoomCode());

                // Wait 2 seconds then send next question
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        broadcastQuestion(room, request.getQuestionNumber() + 1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

        } catch (JsonProcessingException e) {
            log.error("Error processing answer", e);
            throw new RuntimeException("Error processing answer");
        }
    }

    /**
     * Broadcast live leaderboard
     */
    private void broadcastLeaderboard(QuizRoom room) {
        List<RoomPlayer> players = playerRepository.findByRoomIdOrderByScore(room.getId());

        List<PlayerScore> rankings = new ArrayList<>();
        int rank = 1;
        for (RoomPlayer p : players) {
            PlayerScore ps = new PlayerScore();
            ps.setRank(rank++);
            ps.setUsername(p.getUsername());
            ps.setScore(p.getCurrentScore());
            ps.setCorrectAnswers(p.getCorrectAnswers());
            ps.setTotalQuestions(room.getTotalQuestions());
            rankings.add(ps);
        }

        LiveLeaderboardUpdate update = new LiveLeaderboardUpdate();
        update.setRoomCode(room.getRoomCode());
        update.setRankings(rankings);

        messagingTemplate.convertAndSend(
                "/topic/room/" + room.getRoomCode() + "/leaderboard",
                update
        );
    }

    /**
     * Get list of available rooms
     */
    public List<QuizRoom> getAvailableRooms() {
        return roomRepository.findByStatus(QuizRoom.RoomStatus.WAITING);
    }

    /**
     * Get room details by code
     */
    public QuizRoom getRoomByCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }
}
