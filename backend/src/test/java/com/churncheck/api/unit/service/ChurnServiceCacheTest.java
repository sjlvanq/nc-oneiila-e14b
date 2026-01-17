package com.churncheck.api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.external.PredictionClient;
import com.churncheck.api.service.ChurnService;

@ExtendWith(MockitoExtension.class)
class ChurnServiceCacheTest {

    @Mock
    private PredictionClient predictionClient;

    @Mock
    private ClientPredictionMapper mapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ChurnService churnService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
    }

    @Test
    void shouldUseCacheWhenValid() {
        // Given: Cliente con caché válido (hace 1 hora)
        client.setLastPredictionChurn((byte) 1);
        client.setLastPredictionProbability(0.85);
        client.setLastPredictionTimestamp(Instant.now().minus(1, ChronoUnit.HOURS));

        // When
        PredictionResponseDTO result = churnService.predict(client);

        // Then: No debe llamar al microservicio
        verify(predictionClient, never()).predict(any());
        assertEquals((byte) 1, result.churn());
        assertEquals(0.85, result.probability());
    }

    @Test
    void shouldCallMicroserviceWhenCacheExpired() {
        // Given: Cliente con caché expirado (hace 25 horas)
        client.setLastPredictionChurn((byte) 0);
        client.setLastPredictionProbability(0.25);
        client.setLastPredictionTimestamp(Instant.now().minus(25, ChronoUnit.HOURS));

        PredictionResponseDTO newPrediction = new PredictionResponseDTO(
            (byte) 1, 0.92, Instant.now()
        );

        when(mapper.toPredictionRequest(client)).thenReturn(mock(PredictionRequestDTO.class));
        when(predictionClient.predict(any())).thenReturn(newPrediction);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        PredictionResponseDTO result = churnService.predict(client);

        // Then: Debe llamar al microservicio
        verify(predictionClient, times(1)).predict(any());
        assertEquals((byte) 1, result.churn());
        assertEquals(0.92, result.probability());
    }

    @Test
    void shouldCallMicroserviceWhenNoCacheExists() {
        // Given: Cliente sin caché
        client.setLastPredictionTimestamp(null);

        PredictionResponseDTO newPrediction = new PredictionResponseDTO(
            (byte) 1, 0.75, Instant.now()
        );

        when(mapper.toPredictionRequest(client)).thenReturn(mock(PredictionRequestDTO.class));
        when(predictionClient.predict(any())).thenReturn(newPrediction);
        when(clientRepository.save(any())).thenReturn(client);

        // When
        PredictionResponseDTO result = churnService.predict(client);

        // Then: Debe llamar al microservicio
        verify(predictionClient, times(1)).predict(any());
        verify(clientRepository, times(1)).save(client);
        assertEquals((byte) 1, result.churn());
        assertEquals(0.75, result.probability());
    }
}