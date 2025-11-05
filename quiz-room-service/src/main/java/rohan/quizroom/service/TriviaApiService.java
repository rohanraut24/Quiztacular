package rohan.quizroom.service;

import rohan.quizroom.dto.TriviaApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriviaApiService {

    private final RestTemplate restTemplate;

    @Value("${trivia.api.base-url}")
    private String triviaApiBaseUrl;

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

    public Integer getCategoryId(String categoryName) {
        return switch (categoryName.toLowerCase()) {
            case "general" -> 9;
            case "books" -> 10;
            case "film" -> 11;
            case "music" -> 12;
            case "musicals" -> 13;
            case "television" -> 14;
            case "videogames", "video games" -> 15;
            case "boardgames", "board games" -> 16;
            case "science", "science & nature" -> 17;
            case "computers" -> 18;
            case "mathematics" -> 19;
            case "mythology" -> 20;
            case "sports" -> 21;
            case "geography" -> 22;
            case "history" -> 23;
            case "politics" -> 24;
            case "art" -> 25;
            case "celebrities" -> 26;
            case "animals" -> 27;
            case "vehicles" -> 28;
            case "comics" -> 29;
            case "gadgets" -> 30;
            case "anime", "japanese anime & manga" -> 31;
            case "cartoon", "cartoons", "cartoon & animations" -> 32;
            default -> null;
        };
    }
}