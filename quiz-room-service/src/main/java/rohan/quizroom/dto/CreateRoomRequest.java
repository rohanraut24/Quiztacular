package rohan.quizroom.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    @Size(min = 3, max = 50, message = "Room name must be between 3 and 50 characters")
    private String roomName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @Min(value = 5, message = "Minimum 5 questions")
    @Max(value = 20, message = "Maximum 20 questions")
    private Integer totalQuestions = 10;

    @Min(value = 2, message = "Minimum 2 players")
    @Max(value = 10, message = "Maximum 10 players")
    private Integer maxPlayers = 10;
}