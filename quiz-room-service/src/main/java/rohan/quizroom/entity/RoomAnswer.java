package rohan.quizroom.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(name = "question_number", nullable = false)
    private Integer questionNumber;

    @Column(length = 500)
    private String answer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "points_earned")
    private Integer pointsEarned;

    @Column(name = "time_taken_ms")
    private Long timeTakenMs;

    @Column(name = "answered_at", updatable = false)
    private LocalDateTime answeredAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.answeredAt = LocalDateTime.now();
    }
}