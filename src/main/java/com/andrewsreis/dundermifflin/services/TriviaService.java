package com.andrewsreis.dundermifflin.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class TriviaService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${trivia-api.endpoint}")
    private String endpoint;

    public TriviaService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

    }

    public String getTrivia(String name) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(endpoint)
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
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return "";  // fallback to empty trivia
            }
            throw e;  // rethrow if not a 404 or 500 status
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse trivia response", e);
        }
    }
}