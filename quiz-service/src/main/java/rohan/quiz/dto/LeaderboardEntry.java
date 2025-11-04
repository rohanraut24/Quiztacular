package rohan.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntry {
    private Integer rank;
    private String username;
    private Integer score;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Double percentage;
    private String timeTaken;
}