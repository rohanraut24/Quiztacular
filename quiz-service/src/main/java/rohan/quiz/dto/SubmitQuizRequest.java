package rohan.quiz.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class SubmitQuizRequest {

    @NotNull(message = "Attempt ID is required")
    private Long attemptId;

    @NotEmpty(message = "Answers are required")
    private Map<Integer, String> answers; // questionNumber -> answer
}