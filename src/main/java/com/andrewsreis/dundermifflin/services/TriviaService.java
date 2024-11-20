package com.andrewsreis.dundermifflin.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TriviaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TriviaService.class);

    private final String triviaEndpoint;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TriviaService(String triviaEndpoint) {
        this.triviaEndpoint = triviaEndpoint;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getTrivia(String name) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(triviaEndpoint)
                .queryParam("name", name);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    null,
                    String.class
            );
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("trivia").asText();
        } catch (Exception e) {
            LOGGER.error("There is no Trivia for this character: {}", name, e);
            return "There is no Trivia for this character";  // fallback to empty trivia
        }
    }
}
