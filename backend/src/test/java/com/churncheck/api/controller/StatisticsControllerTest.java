package com.churncheck.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.churncheck.api.domain.client.dto.GlobalStatisticsDTO;
import com.churncheck.api.infra.errors.GlobalExceptionHandler;
import com.churncheck.api.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks 
    private StatisticsController statisticsController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configuración Standalone con el manejador global de excepciones del proyecto 
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetStatistics() throws Exception {
        // Given
        GlobalStatisticsDTO expectedStats = new GlobalStatisticsDTO(100L, 85L, 28.5);
        when(clientService.getGlobalStats()).thenReturn(expectedStats);

        // When & Then
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(100))
                .andExpect(jsonPath("$.active").value(85))
                .andExpect(jsonPath("$.averageAge").value(28.5));
    }
}