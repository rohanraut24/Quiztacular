package rohan.quizroom.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResultBroadcast {
    private String username;
    private Integer questionNumber;
    private Boolean isCorrect;
    private Integer pointsEarned;
    private Integer totalScore;
    private Long timeTakenMs;
}