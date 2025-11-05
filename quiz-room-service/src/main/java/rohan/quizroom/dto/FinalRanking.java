package rohan.quizroom.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalRanking {
    private Integer rank;
    private String username;
    private Integer totalScore;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Double accuracy;
}
