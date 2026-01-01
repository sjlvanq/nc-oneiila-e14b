package com.churncheck.api.infra.clients;

import com.churncheck.api.infra.clients.dto.PredictionRequestDTO;

//import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// Reemplazar por domain.client.dto.PredictionResponseDTO cuando esté disponible
final record PredictionResponseDTO(
        Double probabilidad, String prevision) {}

@Component
public class PredictionClient {

    private final RestClient restClient;
    
    @Value("${external.prediction.endpoint}")
    private String endpoint;
    @Value("${external.prediction.timeout")
    private int timeout;

    public PredictionClient(RestClient.Builder restClientBuilder, 
                            @Value("${external.prediction.host}") String host,
                            @Value("${external.prediction.port}") int port) {
        
        String baseUrl = String.format("%s:%d", host, port);
        
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(timeout);
        requestFactory.setReadTimeout(timeout);
        
        //System.out.println("baseUrl: "+baseUrl);
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public PredictionResponseDTO predict(PredictionRequestDTO requestBody) {
        return restClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("is4xxClientError");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("is5xxServerError");
                })
                .body(PredictionResponseDTO.class);
    }
}