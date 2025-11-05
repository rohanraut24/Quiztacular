package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfo {
    private String username;
    private String avatarUrl;
    private Integer currentScore;
    private Integer correctAnswers;
    private Boolean isReady;
    private Boolean isHost;
    private Boolean isConnected;
}