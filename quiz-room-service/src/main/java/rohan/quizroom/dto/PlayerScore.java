package rohan.quizroom.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    private Integer rank;
    private String username;
    private Integer score;
    private Integer correctAnswers;
    private Integer totalQuestions;
}
