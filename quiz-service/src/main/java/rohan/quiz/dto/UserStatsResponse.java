package rohan.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsResponse {
    private String username;
    private Integer totalQuizzesAttempted;
    private Integer totalQuizzesCompleted;
    private Integer totalQuestionsAnswered;
    private Integer totalCorrectAnswers;
    private Double accuracyPercentage;
    private Integer totalScore;
    private Double averageScore;
    private Integer highestScore;
}