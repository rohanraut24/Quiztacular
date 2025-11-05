package rohan.quizroom.controller;
import rohan.quizroom.entity.QuizRoom;
import rohan.quizroom.service.RoomManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoomRestController {

    private final RoomManagerService roomManager;

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "quiz-room-service");
        return ResponseEntity.ok(response);
    }

    /**
     * Get available rooms
     */
    @GetMapping("/available")
    public ResponseEntity<List<QuizRoom>> getAvailableRooms() {
        try {
            List<QuizRoom> rooms = roomManager.getAvailableRooms();
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get room details by code
     */
    @GetMapping("/{roomCode}")
    public ResponseEntity<?> getRoomByCode(@PathVariable String roomCode) {
        try {
            QuizRoom room = roomManager.getRoomByCode(roomCode);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Get available categories
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> categories = List.of(
                Map.of("id", "general", "name", "General Knowledge", "icon", "🌐"),
                Map.of("id", "books", "name", "Books", "icon", "📚"),
                Map.of("id", "film", "name", "Film", "icon", "🎬"),
                Map.of("id", "music", "name", "Music", "icon", "🎵"),
                Map.of("id", "musicals", "name", "Musicals & Theatres", "icon", "🎭"),
                Map.of("id", "television", "name", "Television", "icon", "📺"),
                Map.of("id", "videogames", "name", "Video Games", "icon", "🎮"),
                Map.of("id", "boardgames", "name", "Board Games", "icon", "🎲"),
                Map.of("id", "science", "name", "Science & Nature", "icon", "🔬"),
                Map.of("id", "computers", "name", "Computers", "icon", "💻"),
                Map.of("id", "mathematics", "name", "Mathematics", "icon", "🔢"),
                Map.of("id", "mythology", "name", "Mythology", "icon", "⚡"),
                Map.of("id", "sports", "name", "Sports", "icon", "⚽"),
                Map.of("id", "geography", "name", "Geography", "icon", "🗺️"),
                Map.of("id", "history", "name", "History", "icon", "📜"),
                Map.of("id", "politics", "name", "Politics", "icon", "🏛️"),
                Map.of("id", "art", "name", "Art", "icon", "🎨"),
                Map.of("id", "celebrities", "name", "Celebrities", "icon", "⭐"),
                Map.of("id", "animals", "name", "Animals", "icon", "🐾"),
                Map.of("id", "vehicles", "name", "Vehicles", "icon", "🚗"),
                Map.of("id", "comics", "name", "Comics", "icon", "🦸"),
                Map.of("id", "gadgets", "name", "Gadgets", "icon", "🔧"),
                Map.of("id", "anime", "name", "Japanese Anime & Manga", "icon", "🍥"),
                Map.of("id", "cartoon", "name", "Cartoon & Animations", "icon", "🎞️")
        );

        List<String> difficulties = List.of("easy", "medium", "hard");

        response.put("categories", categories);
        response.put("difficulties", difficulties);

        return ResponseEntity.ok(response);
    }
}

