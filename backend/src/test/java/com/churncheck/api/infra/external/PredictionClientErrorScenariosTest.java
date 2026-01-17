package com.churncheck.api.infra.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.errors.exceptions.MLServiceBadRequestException;
import com.churncheck.api.infra.errors.exceptions.MLServiceTimeoutException;
import com.churncheck.api.infra.errors.exceptions.MLServiceUnavailableException;

import jakarta.validation.Validator;

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
        when(properties.connectTimeout()).thenReturn(5000);
        when(properties.readTimeout()).thenReturn(10000);
        
        when(restClientBuilder.baseUrl(any(String.class))).thenReturn(restClientBuilder);
        when(restClientBuilder.defaultHeaders(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.requestFactory(any())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        
        predictionClient = new PredictionClient(restClientBuilder, properties, validator);
    }
    
    @Test
    void shouldHandleTimeoutException() {
        // Given
        when(restClient.post())
                .thenThrow(new ResourceAccessException("Connect timed out"));
        
        // When & Then
        MLServiceTimeoutException exception = assertThrows(MLServiceTimeoutException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("No se pudo conectar al servicio de ML", exception.getMessage());
    }
    
    @Test
    void shouldHandleMLServiceUnavailable() {
        // Given
        when(restClient.post())
                .thenThrow(new RuntimeException("Service unavailable"));
        
        // When & Then
        MLServiceUnavailableException exception = assertThrows(MLServiceUnavailableException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Error de comunicación con el servicio de predicción", exception.getMessage());
    }
    
    @Test
    void shouldHandleMLServiceBadRequestOn4xxError() {
        // Given
        when(restClient.post())
                .thenThrow(new org.springframework.web.client.HttpClientErrorException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Bad Request"));
        
        // When & Then
        MLServiceBadRequestException exception = assertThrows(MLServiceBadRequestException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Error de cliente en servicio ML", exception.getMessage());
    }
    
    @Test
    void shouldHandleMLServiceUnavailableOn5xxError() {
        // Given
        when(restClient.post())
                .thenThrow(new org.springframework.web.client.HttpServerErrorException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
        
        // When & Then
        MLServiceUnavailableException exception = assertThrows(MLServiceUnavailableException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("El servicio de ML respondió con error", exception.getMessage());
    }
    
    @Test
    void shouldHandleConnectionRefused() {
        // Given
        when(restClient.post())
                .thenThrow(new ResourceAccessException("Connection refused"));
        
        // When & Then
        MLServiceTimeoutException exception = assertThrows(MLServiceTimeoutException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("No se pudo conectar al servicio de ML", exception.getMessage());
    }
    
    @Test
    void shouldHandleNullResponseFromMLService() {
        // Given
        RestClient.RequestBodyUriSpec requestBodyUriSpec = org.mockito.Mockito.mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = org.mockito.Mockito.mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = org.mockito.Mockito.mock(RestClient.ResponseSpec.class);
        
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(eq(PredictionResponseDTO.class))).thenReturn(null);
        
        // When & Then
        MLServiceUnavailableException exception = assertThrows(MLServiceUnavailableException.class, () -> 
                predictionClient.predict(createTestRequest()));
        
        assertEquals("Error de comunicación con el servicio de predicción", exception.getMessage());
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