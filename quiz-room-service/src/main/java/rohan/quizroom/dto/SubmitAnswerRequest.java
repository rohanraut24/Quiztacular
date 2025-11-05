package rohan.quizroom.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SubmitAnswerRequest {

    @NotBlank(message = "Room code is required")
    private String roomCode;

    @NotNull(message = "Question number is required")
    @Min(value = 1, message = "Invalid question number")
    private Integer questionNumber;

    @NotBlank(message = "Answer is required")
    private String answer;

    @NotNull(message = "Time taken is required")
    @Min(value = 0, message = "Invalid time")
    private Long timeTakenMs;
}

