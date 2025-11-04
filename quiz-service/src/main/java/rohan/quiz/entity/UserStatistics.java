package rohan.quiz.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer totalQuizzesAttempted = 0;

    @Column(nullable = false)
    private Integer totalQuizzesCompleted = 0;

    @Column(nullable = false)
    private Integer totalQuestionsAnswered = 0;

    @Column(nullable = false)
    private Integer totalCorrectAnswers = 0;

    @Column(nullable = false)
    private Integer totalScore = 0;

    @Column(nullable = false)
    private Double averageScore = 0.0;

    @Column(nullable = false)
    private Integer highestScore = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Calculate accuracy percentage
    public double getAccuracyPercentage() {
        if (totalQuestionsAnswered == 0) return 0.0;
        return (totalCorrectAnswers * 100.0) / totalQuestionsAnswered;
    }
}
