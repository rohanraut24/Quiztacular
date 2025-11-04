package rohan.quiz.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuizRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @Min(value = 5, message = "Minimum 5 questions required")
    @Max(value = 50, message = "Maximum 50 questions allowed")
    private Integer amount = 10;
}