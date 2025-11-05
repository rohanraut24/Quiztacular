package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBroadcast {
    private Integer questionNumber;
    private Integer totalQuestions;
    private String question;
    private List<String> options;
    private String type;
    private Long timeLimit; // in seconds
}
