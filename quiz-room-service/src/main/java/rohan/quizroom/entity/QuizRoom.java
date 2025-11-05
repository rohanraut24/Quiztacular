package rohan.quizroom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String roomCode;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Long hostUserId;

    @Column(nullable = false)
    private String hostUsername;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer maxPlayers = 10;

    @Column(nullable = false)
    private Integer currentPlayers = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.WAITING;

    @Column(columnDefinition = "TEXT")
    private String questionsData; // JSON string of questions

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPlayer> players = new ArrayList<>();

    public enum RoomStatus {
        WAITING,      // Waiting for players to join
        STARTING,     // Countdown before quiz starts
        IN_PROGRESS,  // Quiz is active
        COMPLETED     // Quiz finished
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}