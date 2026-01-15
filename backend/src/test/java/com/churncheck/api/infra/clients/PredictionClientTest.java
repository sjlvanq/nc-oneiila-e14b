package com.churncheck.api.infra.clients;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.external.PredictionClient;
import com.churncheck.api.infra.external.PredictionProperties;

class PredictionClientTest {
    
    private RestClient.Builder restClientBuilder;
    private PredictionProperties properties;
    private Validator validator;
    
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
    
    @Test
    void shouldThrowConstraintViolationExceptionForInvalidRequest() {
        // Given
        PredictionClient client = new PredictionClient(restClientBuilder, properties, validator);
        PredictionRequestDTO invalidRequest = new PredictionRequestDTO(
            null, null, null, null, null, null, null, null, 
            null, null, null, null, null
        );
        
        // When & Then
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> client.predict(invalidRequest)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Solicitud de predicción inválida"));
        assertFalse(exception.getConstraintViolations().isEmpty());
    }
    
    @Test
    void shouldValidateRequestWithNullValues() {
        // Given
        PredictionClient client = new PredictionClient(restClientBuilder, properties, validator);
        PredictionRequestDTO requestWithNulls = new PredictionRequestDTO(
            0, (byte)1, (byte)0, (byte)1, (byte)1, 12, (byte)1, 30, 
            new java.math.BigDecimal("50.50"), null, 6, 
            new java.math.BigDecimal("2.5"), new java.math.BigDecimal("3.0")
        );
        
        // When & Then
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> client.predict(requestWithNulls)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Solicitud de predicción inválida"));
    }
    
    @Test
    void shouldValidateRequestWithNegativeValues() {
        // Given
        PredictionClient client = new PredictionClient(restClientBuilder, properties, validator);
        PredictionRequestDTO requestWithNegatives = new PredictionRequestDTO(
            0, (byte)1, (byte)0, (byte)1, (byte)1, 12, (byte)1, 30, 
            new java.math.BigDecimal("50.50"), -5, 6, 
            new java.math.BigDecimal("2.5"), new java.math.BigDecimal("3.0")
        );
        
        // When & Then
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> client.predict(requestWithNegatives)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Solicitud de predicción inválida"));
    }
    
    @Test
    void shouldValidateRequestWithExtremeValues() {
        // Given
        PredictionClient client = new PredictionClient(restClientBuilder, properties, validator);
        PredictionRequestDTO requestWithExtremes = new PredictionRequestDTO(
            0, (byte)1, (byte)0, (byte)1, (byte)1, 12, (byte)1, 30, 
            new java.math.BigDecimal("50.50"), 6, 50, 
            new java.math.BigDecimal("2.5"), new java.math.BigDecimal("3.0")
        );
        
        // When & Then
        ConstraintViolationException exception = assertThrows(
            ConstraintViolationException.class,
            () -> client.predict(requestWithExtremes)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Solicitud de predicción inválida"));
    }
    
    @Test
    void shouldCreateClientWithNullApiKey() {
        // Given
        PredictionProperties propertiesWithoutApiKey = new PredictionProperties(
            "https://api.example.com", 443, "/predict",
            null, 5000, 5000, 10000, 3
        );
        
        // When
        PredictionClient client = new PredictionClient(restClientBuilder, propertiesWithoutApiKey, validator);
        
        // Then
        assertNotNull(client);
        assertNotNull(client.getRestClient());
    }
    
    @Test
    void shouldCreateClientWithEmptyApiKey() {
        // Given
        PredictionProperties propertiesWithEmptyApiKey = new PredictionProperties(
            "https://api.example.com", 443, "/predict",
            "", 5000, 5000, 10000, 3
        );
        
        // When
        PredictionClient client = new PredictionClient(restClientBuilder, propertiesWithEmptyApiKey, validator);
        
        // Then
        assertNotNull(client);
        assertNotNull(client.getRestClient());
    }
    
    @Test
    void shouldCreateClientWithDifferentPorts() {
        // Given
        PredictionProperties propertiesWithDifferentPort = new PredictionProperties(
            "localhost", 8080, "/predict",
            "test-key", 1000, 2000, 3000, 5
        );
        
        // When
        PredictionClient client = new PredictionClient(restClientBuilder, propertiesWithDifferentPort, validator);
        
        // Then
        assertNotNull(client);
        assertNotNull(client.getRestClient());
        assertEquals("localhost:8080", propertiesWithDifferentPort.getBaseUrl());
    }
}
