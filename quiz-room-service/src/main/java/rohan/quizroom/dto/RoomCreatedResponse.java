package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreatedResponse {
    private String roomCode;
    private String roomName;
    private String category;
    private String difficulty;
    private Integer totalQuestions;
    private Integer maxPlayers;
    private String message;
}