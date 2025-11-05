package rohan.quizroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class TriviaApiResponse {

    @JsonProperty("response_code")
    private Integer responseCode;

    private List<TriviaQuestion> results;

    @Data
    public static class TriviaQuestion {
        private String category;
        private String type;
        private String difficulty;
        private String question;

        @JsonProperty("correct_answer")
        private String correctAnswer;

        @JsonProperty("incorrect_answers")
        private List<String> incorrectAnswers;
    }
}
