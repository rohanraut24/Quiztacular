package rohan.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResultDTO {
    private Integer questionNumber;
    private String question;
    private String userAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
}
