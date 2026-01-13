package com.churncheck.api.infra.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Validator;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PredictionClientErrorScenariosTest {
    
    @Mock
    private RestClient.Builder restClientBuilder;
    
    @Mock
    private RestClient restClient;
    
    @Mock
    private PredictionProperties properties;
    
    @Mock
    private Validator validator;
    
    private PredictionClient predictionClient;
    
    @BeforeEach
    void setUp() {
        when(properties.getBaseUrl()).thenReturn("https://lode.uno");
        when(properties.endpoint()).thenReturn("/churncheck.php");
        when(properties.apiKey()).thenReturn("mock-api-key-12345");
        when(properties.connectionTimeout()).thenReturn(5000);
        when(properties.readTimeout()).thenReturn(10000);
        
        when(restClientBuilder.baseUrl(any(String.class))).thenReturn(restClientBuilder);
        when(restClientBuilder.defaultHeaders(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.requestFactory(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        
        predictionClient = new PredictionClient(restClientBuilder, properties, validator);
    }
    
    @Test
    void shouldHandle400BadRequest() {
        // Given
        String expectedReason = "Client error: 400 - Failed to get prediction";
        when(restClient.post())
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, expectedReason));
        
        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals(expectedReason, exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
    
    @Test
    void shouldHandle500InternalServerError() {
        // Given
        String expectedReason = "Server error: 500 - ML service unavailable";
        when(restClient.post())
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY, expectedReason));
        
        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals(expectedReason, exception.getReason());
        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
    }
    
    @Test
    void shouldHandleTimeoutOrConnectionError() {
        // Given
        when(restClient.post())
                .thenThrow(new ResourceAccessException("Request timeout"));
        
        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
        assertEquals("Error de comunicación con el servicio de predicción", exception.getReason());
    }
    
    @Test
    void shouldHandleGenericRuntimeException() {
        // Given
        when(restClient.post())
                .thenThrow(new RuntimeException("Unexpected error"));
        
        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
        assertEquals("Error de comunicación con el servicio de predicción", exception.getReason());
    }
    
    @Test
    void shouldCreateRestClientSuccessfully() {
        // When
        RestClient result = predictionClient.getRestClient();
        
        // Then
        assertNotNull(result);
    }
    
    private PredictionRequestDTO createTestRequest() {
        return new PredictionRequestDTO(
            1, (byte) 1, (byte) 1, (byte) 1, (byte) 1,
            12, (byte) 1, 30,
            BigDecimal.valueOf(2.5), 24, 30,
            BigDecimal.valueOf(1.8), BigDecimal.valueOf(3.2)
        );
    }
}
