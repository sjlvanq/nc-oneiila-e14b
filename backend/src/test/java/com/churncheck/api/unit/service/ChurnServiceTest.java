package com.churncheck.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.external.PredictionClient;
import com.churncheck.api.service.ChurnService;

@ExtendWith(MockitoExtension.class)
class ChurnServiceTest {

    @Mock
    private PredictionClient predictionClient;

    @Mock
    private com.churncheck.api.domain.client.ClientPredictionMapper mapper;

    @Mock 
    private ClientRepository clientRepository;
    
    @InjectMocks
    private ChurnService churnService;

    @Test
    void shouldPredictSuccessfully(){
        // Given (Arrange)
        Client client = createTestClient();
        PredictionRequestDTO expectedRequest = new PredictionRequestDTO(
            0,                              // gender (MALE)
            1,                              // nearLocation (true)
            0,                              // partner (null)
            1,                              // promoFriends (true)
            1,                              // phone (no nulo)
            12,                               // contractPeriod
            1,                               // groupVisits (true)
            30,                               // age
            new BigDecimal("50.50"),          // avgAdditionalChargesTotal
            6,                                // monthToEndContract (calculado)
            6,                                // lifetime (calculado)
            new BigDecimal("2.5"),           // avgClassFrequencyTotal
            new BigDecimal("3.0")            // avgClassFrequencyCurrentMonth
        );
        
        PredictionResponseDTO expectedResponse = new PredictionResponseDTO((byte)1, 0.85, Instant.now());
        
        when(mapper.toPredictionRequest(client)).thenReturn(expectedRequest);
        when(predictionClient.predict(expectedRequest)).thenReturn(expectedResponse);
        
        // When (Act)
        PredictionResponseDTO result = churnService.predict(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)1, result.churn(), "Churn debe ser 1");
        assertEquals(0.85, result.probability(), "Probability debe ser 0.85");
        assertNotNull(result.timestamp(), "Timestamp no debe ser nulo");
    }
    
    @Test
    void shouldHandleFemaleClientPrediction(){
        // Given (Arrange)
        Client client = createTestClient();
        client.setGender(Gender.FEMALE);
        
        PredictionRequestDTO expectedRequest = new PredictionRequestDTO(
            1,                              // gender (FEMALE)
            1,                              // nearLocation (true)
            0,                              // partner (null)
            1,                              // promoFriends (true)
            1,                              // phone (no nulo)
            12,                               // contractPeriod
            1,                               // groupVisits (true)
            30,                               // age
            new BigDecimal("50.50"),          // avgAdditionalChargesTotal
            6,                                // monthToEndContract (calculado)
            6,                                // lifetime (calculado)
            new BigDecimal("2.5"),           // avgClassFrequencyTotal
            new BigDecimal("3.0")            // avgClassFrequencyCurrentMonth
        );
        
        PredictionResponseDTO expectedResponse = new PredictionResponseDTO((byte)0, 0.25, Instant.now());
        
        when(mapper.toPredictionRequest(client)).thenReturn(expectedRequest);
        when(predictionClient.predict(expectedRequest)).thenReturn(expectedResponse);
        
        // When (Act)
        PredictionResponseDTO result = churnService.predict(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)0, result.churn(), "Churn debe ser 0 para FEMALE");
        assertEquals(0.25, result.probability(), "Probability debe ser 0.25");
        assertNotNull(result.timestamp(), "Timestamp no debe ser nulo");
    }
    
    @Test
    void shouldHandleNullPhoneInPrediction(){
        // Given (Arrange)
        Client client = createTestClient();
        client.setClientPhone(null);
        
        PredictionRequestDTO expectedRequest = new PredictionRequestDTO(
            0,                              // gender (MALE)
            1,                              // nearLocation (true)
            0,                              // partner (null)
            1,                              // promoFriends (true)
            0,                              // phone (nulo)
            12,                               // contractPeriod
            1,                               // groupVisits (true)
            30,                               // age
            new BigDecimal("50.50"),          // avgAdditionalChargesTotal
            6,                                // monthToEndContract (calculado)
            6,                                // lifetime (calculado)
            new BigDecimal("2.5"),           // avgClassFrequencyTotal
            new BigDecimal("3.0")            // avgClassFrequencyCurrentMonth
        );
        
        PredictionResponseDTO expectedResponse = new PredictionResponseDTO((byte)1, 0.95, Instant.now());
        
        when(mapper.toPredictionRequest(client)).thenReturn(expectedRequest);
        when(predictionClient.predict(expectedRequest)).thenReturn(expectedResponse);
        
        // When (Act)
        PredictionResponseDTO result = churnService.predict(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)1, result.churn(), "Churn debe ser 1");
        assertEquals(0.95, result.probability(), "Probability debe ser 0.95");
        assertNotNull(result.timestamp(), "Timestamp no debe ser nulo");
    }
    
    private Client createTestClient() {
        Client client = new Client();
        client.setId(1L);
        client.setGender(Gender.MALE);
        client.setNearLocation(true);
        client.setPromoFriends(true);
        client.setClientPhone("123-456-7890");
        client.setContractPeriod(12);
        client.setGroupVisit(true);
        client.setAge(30);
        client.setContractStartDate(LocalDate.now().minusMonths(6));
        client.setActive(true);
        return client;
    }
}
