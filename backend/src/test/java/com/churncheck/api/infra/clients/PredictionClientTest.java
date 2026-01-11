package com.churncheck.api.infra.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import jakarta.validation.Validation;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;

class PredictionClientTest {
    
    private RestClient.Builder restClientBuilder;
    private PredictionProperties properties;
    private jakarta.validation.Validator validator;
    
    @BeforeEach
    void setUp() {
        restClientBuilder = RestClient.builder();
        properties = new PredictionProperties(
            "https://mock.echoapi.com", 443, "/mock/590aa3d9c002000/predict",
            "mock-api-key-12345", 5000, 5000, 10000, 3
        );
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    @Test
    void shouldCreateClientSuccessfully() {
        // When
        PredictionClient client = new PredictionClient(restClientBuilder, properties, validator);
        
        // Then
        assertNotNull(client);
        assertNotNull(client.getRestClient());
    }
    
    @Test
    void shouldReturnCorrectBaseUrl() {
        // When
        String baseUrl = properties.getBaseUrl();
        
        // Then
        assertEquals("https://mock.echoapi.com", baseUrl);
    }
    
    @Test
    void shouldReturnCorrectBaseUrlWithPort() {
        // Given
        PredictionProperties testProperties = new PredictionProperties(
            "api.example.com", 8080, "/predict",
            "test-key", 5000, 5000, 10000, 3
        );
        
        // When
        String baseUrl = testProperties.getBaseUrl();
        
        // Then
        assertEquals("api.example.com:8080", baseUrl);
    }
    
    @Test
    void shouldCreatePredictionRequestDTO() {
        // Given
        PredictionRequestDTO request = new PredictionRequestDTO(
            0, (byte)1, (byte)0, (byte)1, (byte)1, 12, (byte)1, 30, 
            new java.math.BigDecimal("50.50"), 6, 6, 
            new java.math.BigDecimal("2.5"), new java.math.BigDecimal("3.0")
        );
        
        // Then
        assertNotNull(request);
        assertEquals(0, request.gender());
        assertEquals((byte)1, request.nearLocation());
    }
    
    @Test
    void shouldCreatePredictionResponseDTO() {
        // Given
        PredictionResponseDTO response = new PredictionResponseDTO(
            (byte)1, 0.92, java.time.Instant.now()
        );
        
        // Then
        assertNotNull(response);
        assertEquals((byte)1, response.churn());
        assertEquals(0.92, response.probability());
        assertNotNull(response.timestamp());
    }
}
