package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveLeaderboardUpdate {
    private String roomCode;
    private List<PlayerScore> rankings;
}