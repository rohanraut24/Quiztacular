package rohan.quizroom.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinRoomRequest {

    @NotBlank(message = "Room code is required")
    private String roomCode;
}
