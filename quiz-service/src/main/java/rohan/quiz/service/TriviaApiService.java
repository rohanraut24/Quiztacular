package rohan.quiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rohan.quiz.dto.TriviaApiResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriviaApiService {

    private final RestTemplate restTemplate;

    @Value("${trivia.api.base-url}")
    private String triviaApiBaseUrl;

    /**
     * Fetch quiz from Open Trivia DB API using RestTemplate
     *
     * @param categoryId Category ID from Open Trivia DB
     * @param difficulty easy, medium, hard
     * @param amount Number of questions
     * @return TriviaApiResponse
     */
    public TriviaApiResponse fetchQuiz(Integer categoryId, String difficulty, Integer amount) {
        log.info("Fetching quiz from Trivia API - Category: {}, Difficulty: {}, Amount: {}",
                categoryId, difficulty, amount);

        try {
            StringBuilder urlBuilder = new StringBuilder(triviaApiBaseUrl);
            urlBuilder.append("?amount=").append(amount);
            urlBuilder.append("&difficulty=").append(difficulty.toLowerCase());
            urlBuilder.append("&type=multiple");

            if (categoryId != null && categoryId > 0) {
                urlBuilder.append("&category=").append(categoryId);
            }

            String url = urlBuilder.toString();
            log.debug("Trivia API URL: {}", url);

            TriviaApiResponse response = restTemplate.getForObject(url, TriviaApiResponse.class);

            if (response == null || response.getResponseCode() != 0) {
                throw new RuntimeException("Failed to fetch quiz from Trivia API");
            }

            log.info("Successfully fetched {} questions from Trivia API",
                    response.getResults().size());

            return response;

        } catch (Exception e) {
            log.error("Error fetching quiz from Trivia API: {}", e.getMessage());
            throw new RuntimeException("Unable to generate quiz. Please try again later.");
        }
    }

    /**
     * Get category ID from category name
     */
    public Integer getCategoryId(String categoryName) {
        return switch (categoryName.toLowerCase()) {
            case "general" -> 9;
            case "computers" -> 18;
            case "mathematics" -> 19;
            case "sports" -> 21;
            case "geography" -> 22;
            case "history" -> 23;
            case "politics" -> 24;
            case "animals" -> 27;
            case "science" -> 17;
            case "music" -> 12;
            default -> null; // Any category
        };
    }
}