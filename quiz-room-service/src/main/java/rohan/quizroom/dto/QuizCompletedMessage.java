package rohan.quizroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizCompletedMessage {
    private String roomCode;
    private String winnerUsername;
    private List<FinalRanking> finalRankings;
}
