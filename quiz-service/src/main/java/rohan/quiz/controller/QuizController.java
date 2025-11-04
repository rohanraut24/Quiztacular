package rohan.quiz.controller;

import rohan.quiz.dto.*;
import rohan.quiz.entity.QuizAttempt;
import rohan.quiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {

    private final QuizService quizService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "quiz-service");
        return ResponseEntity.ok(response);
    }

    /**
     * Get available categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> categories = List.of(
                Map.of("id", "general", "name", "General Knowledge", "icon", "🌐"),
                Map.of("id", "computers", "name", "Computers", "icon", "💻"),
                Map.of("id", "mathematics", "name", "Mathematics", "icon", "🔢"),
                Map.of("id", "sports", "name", "Sports", "icon", "⚽"),
                Map.of("id", "geography", "name", "Geography", "icon", "🗺️"),
                Map.of("id", "history", "name", "History", "icon", "📜"),
                Map.of("id", "politics", "name", "Politics", "icon", "🏛️"),
                Map.of("id", "animals", "name", "Animals", "icon", "🐾"),
                Map.of("id", "science", "name", "Science", "icon", "🔬"),
                Map.of("id", "music", "name", "Music", "icon", "🎵")
        );

        List<String> difficulties = List.of("easy", "medium", "hard");

        response.put("categories", categories);
        response.put("difficulties", difficulties);

        return ResponseEntity.ok(response);
    }

    /**
     * Generate new quiz
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@Valid @RequestBody QuizRequest request) {
        try {
            QuizResponse response = quizService.generateQuiz(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Submit quiz answers
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@Valid @RequestBody SubmitQuizRequest request) {
        try {
            QuizResultResponse response = quizService.submitQuiz(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Get user's quiz history
     */
    @GetMapping("/history")
    public ResponseEntity<List<QuizAttempt>> getHistory() {
        try {
            List<QuizAttempt> history = quizService.getUserHistory();
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get leaderboard
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            LeaderboardResponse response = quizService.getLeaderboard(category, limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Get user statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStatistics() {
        try {
            UserStatsResponse response = quizService.getUserStatistics();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Get quiz attempt details by ID
     */
    @GetMapping("/attempt/{id}")
    public ResponseEntity<?> getAttemptById(@PathVariable Long id) {
        try {
            // Add implementation if needed
            return ResponseEntity.ok(Map.of("message", "Feature coming soon"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}