package com.churncheck.api.infra.clients;

import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import com.churncheck.api.domain.client.dto.PredictionRequestDTO;

import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PredictionClient {

    private static final Logger logger = Logger.getLogger(PredictionClient.class.getName());

    private final RestClient restClient;
    private final PredictionProperties properties;

    public PredictionClient(RestClient.Builder restClientBuilder, PredictionProperties properties) {
        this.properties = properties;
        
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    if (properties.apiKey() != null && !properties.apiKey().isEmpty()) {
                        headers.set("API-Key", properties.apiKey());
                    }
                })
                .requestFactory(createRequestFactory())
                .build();
    }
    
    private HttpComponentsClientHttpRequestFactory createRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(properties.connectionTimeout());
        factory.setReadTimeout(properties.readTimeout());
        return factory;
    }
    
    public PredictionResponseDTO predict(PredictionRequestDTO request) {
        logger.info("Sending prediction request to ML service: " + properties.getBaseUrl());
        
        try {
            return restClient.post()
                    .uri(properties.endpoint())
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Client error: %d - Failed to get prediction", statusCode);
                        logger.severe(message);
                        throw new PredictionClientException(message, statusCode);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Server error: %d - ML service unavailable", statusCode);
                        logger.severe(message);
                        throw new PredictionServerException(message, statusCode);
                    })
                    .body(PredictionResponseDTO.class);
        } catch (Exception e) {
            logger.severe("Failed to get prediction from ML service: " + e.getMessage());
            throw new PredictionException("Failed to get prediction", e);
        }
    }
}