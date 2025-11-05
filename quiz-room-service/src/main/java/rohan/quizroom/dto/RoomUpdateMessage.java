package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateMessage {
    private String roomCode;
    private String roomName;
    private String status;
    private Integer currentPlayers;
    private Integer maxPlayers;
    private Integer countdown; // For starting countdown
    private List<PlayerInfo> players;
}
