package in.Prajwal.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SummaryService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    public SummaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callLLM(String promptText) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                    Map.of("role", "system", "content", "You are a helpful assistant."),
                    Map.of("role", "user", "content", promptText)
                )
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating summary: " + e.getMessage();
        }
    }

    public boolean sendToSlack(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> payload = Map.of("text", message);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(slackWebhookUrl, request, String.class);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
