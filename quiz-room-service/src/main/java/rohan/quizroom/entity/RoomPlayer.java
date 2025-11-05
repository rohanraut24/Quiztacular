package rohan.quizroom.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_players",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private QuizRoom room;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "current_score", nullable = false)
    private Integer currentScore = 0;

    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers = 0;

    @Column(name = "current_question", nullable = false)
    private Integer currentQuestion = 0;

    @Column(name = "is_ready", nullable = false)
    private Boolean isReady = false;

    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished = false;

    @Column(name = "is_connected", nullable = false)
    private Boolean isConnected = true;

    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }
}