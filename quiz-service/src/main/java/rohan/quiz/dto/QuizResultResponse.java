package rohan.quiz.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultResponse {
    private Long attemptId;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer score;
    private Double percentage;
    private Long timeTakenSeconds;
    private String timeTaken; // formatted as "2m 30s"
    private List<QuestionResultDTO> results;
}