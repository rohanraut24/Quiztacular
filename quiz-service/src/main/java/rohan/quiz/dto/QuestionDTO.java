package rohan.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Integer questionNumber;
    private String question;
    private List<String> options;
    private String type; // multiple or boolean
}
