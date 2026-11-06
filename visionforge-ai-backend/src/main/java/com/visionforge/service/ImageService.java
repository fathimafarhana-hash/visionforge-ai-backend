
package com.visionforge.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ImageService {

    private final RestClient restClient;
    private final String model;

    public ImageService(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.image-model}") String model
    ) {

        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String generateImage(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "prompt", prompt,
                "size", "1024x1024",
                "quality", "medium",
                "n", 1
        );

        JsonNode response = restClient.post()
                .uri("/images/generations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        if (response == null
                || !response.has("data")
                || response.get("data").isEmpty()) {

            throw new RuntimeException("No image generated");
        }

        return response.get("data")
                .get(0)
                .get("b64_json")
                .asText();
    }
}