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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

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
        
        predictionClient = new PredictionClient(restClientBuilder, properties);
    }
    
    @Test
    void shouldHandle400BadRequest() {
        // Given
        when(restClient.post())
                .thenThrow(new PredictionClientException("Client error: 400 - Failed to get prediction", 400));
        
        // When & Then
        PredictionException exception = assertThrows(PredictionException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Failed to get prediction", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(PredictionClientException.class, exception.getCause().getClass());
    }
    
    @Test
    void shouldHandle500InternalServerError() {
        // Given
        when(restClient.post())
                .thenThrow(new PredictionServerException("Server error: 500 - ML service unavailable", 500));
        
        // When & Then
        PredictionException exception = assertThrows(PredictionException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Failed to get prediction", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(PredictionServerException.class, exception.getCause().getClass());
    }
    
    @Test
    void shouldHandleTimeout() {
        // Given
        when(restClient.post())
                .thenThrow(new ResourceAccessException("Request timeout"));
        
        // When & Then
        PredictionException exception = assertThrows(PredictionException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Failed to get prediction", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(ResourceAccessException.class, exception.getCause().getClass());
    }
    
    @Test
    void shouldHandleGenericException() {
        // Given
        when(restClient.post())
                .thenThrow(new RuntimeException("Unexpected error"));
        
        // When & Then
        PredictionException exception = assertThrows(PredictionException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Failed to get prediction", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(RuntimeException.class, exception.getCause().getClass());
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
