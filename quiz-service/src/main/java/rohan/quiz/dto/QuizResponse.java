package rohan.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private Long attemptId;
    private String category;
    private String difficulty;
    private Integer totalQuestions;
    private List<QuestionDTO> questions;
    private String startTime;
}