package com.churncheck.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    
    @Mock
    private ClientService clientService;
    
    @InjectMocks
    private ClientController clientController;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void shouldCreateClient() throws Exception {
        // Given
        ClientCreateRequestDTO dto = new ClientCreateRequestDTO(
            "John Doe",                    // clientName
            true,                          // active
            Gender.MALE,                   // gender
            true,                          // nearLocation
            1L,                            // partnerId
            true,                          // promoFriends
            "555-1234",                    // clientPhone
            30,                            // age
            12,                            // contractPeriod
            true                           // groupVisits
        );
        
        ClientResponseDTO response = new ClientResponseDTO(
            1L, "John Doe", true, Gender.MALE, "555-1234", true, 30
        );
        
        when(clientService.createFromDto(any(ClientCreateRequestDTO.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clientName").value("John Doe"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.clientPhone").value("555-1234"))
                .andExpect(jsonPath("$.age").value(30));
    }
    
    @Test
    void shouldUpdateClient() throws Exception {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "John Updated", "555-9999", true, 35
        );
        
        ClientResponseDTO response = new ClientResponseDTO(
            1L, "John Updated", true, Gender.MALE, "555-9999", true, 35
        );
        
        when(clientService.updateClient(any(ClientUpdateRequestDTO.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(put("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clientName").value("John Updated"))
                .andExpect(jsonPath("$.age").value(35));
    }
    
    @Test
    void shouldGetClientById() throws Exception {
        // Given
        ClientResponseDTO response = new ClientResponseDTO(
            1L, "John Doe", true, Gender.MALE, "555-1234", true, 30
        );
        
        when(clientService.findById(1L)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clientName").value("John Doe"));
    }
    
    @Test
    void shouldDeleteClient() throws Exception {
        // Given - deleteClient returns void, no need to mock return
        
        // When & Then
        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void shouldGetClientPrediction() throws Exception {
        // Given
        ClientFullResponseDTO prediction = new ClientFullResponseDTO(
            1L, "John Doe", "555-1234", (byte)1, 0.85, java.time.Instant.now()
        );
        
        when(clientService.predictChurn(1L)).thenReturn(prediction);
        
        // When & Then
        mockMvc.perform(get("/clients/1/prediction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.clientName").value("John Doe"))
                .andExpect(jsonPath("$.churn").value(1))
                .andExpect(jsonPath("$.probability").value(0.85));
    }
    
    @Test
    void shouldReturn400WhenPredictionFails() throws Exception {
        // Given
        when(clientService.predictChurn(1L)).thenThrow(new RuntimeException("Prediction failed"));
        
        // When & Then
        mockMvc.perform(get("/clients/1/prediction"))
                .andExpect(status().isBadRequest());
    }
}
