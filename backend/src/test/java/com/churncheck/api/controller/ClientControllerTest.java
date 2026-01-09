package com.churncheck.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class ClientControllerTest {
    
    @Autowired
    private ClientService clientService;
    
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientService)).build();
    }
    
    @Test
    void shouldCreateClientSuccessfully() throws Exception {
        // Given
        ClientCreateRequestDTO dto = new ClientCreateRequestDTO(
            "John Doe", true, Gender.MALE, true, 1L, true, "555-1234", 
            30, 12, true, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(50.5)
        );
        
        Client client = createTestClient();
        client.setClientName("John Doe"); // Set the expected name
        client.setClientPhone("555-1234"); // Set the expected phone
        ClientResponseDTO response = new ClientResponseDTO(client);
        
        // Mock the service method
        ClientService mockService = org.mockito.Mockito.mock(ClientService.class);
        when(mockService.createFromDto(any(ClientCreateRequestDTO.class))).thenReturn(response);
        
        // Create controller with mock service
        ClientController controller = new ClientController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value("John Doe"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.clientPhone").value("555-1234"))
                .andExpect(jsonPath("$.nearLocation").value(true))
                .andExpect(jsonPath("$.age").value(30));
    }
    
    @Test
    void shouldReturn400WhenInvalidData() throws Exception {
        // Given - nombre vacío
        String invalidJson = "{\"clientName\":\"\",\"active\":true,\"gender\":\"MALE\",\"nearLocation\":true,\"partnerId\":1,\"promoFriends\":true,\"clientPhone\":\"555-1234\",\"age\":30,\"contractPeriod\":12,\"groupVisits\":true,\"avgClassFrequencyTotal\":10,\"avgClassFrequencyCurrentMonth\":1,\"avgAdditionalChargesTotal\":50.5}";
        
        // Use standalone setup with real controller to test JSON validation
        ClientController controller = new ClientController(clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldReturn400WhenMissingRequiredFields() throws Exception {
        // Given - JSON incompleto
        String incompleteJson = "{\"clientName\":\"John Doe\"}";
        
        // Use standalone setup with real controller to test JSON validation
        ClientController controller = new ClientController(clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incompleteJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldGetClientPredictionSuccessfully() throws Exception {
        // Given
        Long clientId = 1L;
        Client client = createTestClient();
        PredictionResponseDTO prediction = new PredictionResponseDTO((byte)0, 0.25, Instant.now());
        ClientFullResponseDTO fullResponse = new ClientFullResponseDTO(client, prediction);
        
        // Mock the service method
        ClientService mockService = org.mockito.Mockito.mock(ClientService.class);
        when(mockService.predictChurn(clientId)).thenReturn(fullResponse);
        
        // Create controller with mock service
        ClientController controller = new ClientController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(get("/clients/{id}/prediction", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.clientPhone").value("123-456-7890"))
                .andExpect(jsonPath("$.churn").value(0))
                .andExpect(jsonPath("$.probability").value(0.25))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    
    @Test
    void shouldReturn400WhenPredictionFails() throws Exception {
        // Given
        Long clientId = 999L;
        
        // Mock the service method to throw exception
        ClientService mockService = org.mockito.Mockito.mock(ClientService.class);
        when(mockService.predictChurn(clientId)).thenThrow(new RuntimeException("Client not found"));
        
        // Create controller with mock service
        ClientController controller = new ClientController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(get("/clients/{id}/prediction", clientId))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldReturn400WhenPredictionThrowsException() throws Exception {
        // Given
        Long clientId = 1L;
        
        // Mock the service method to throw exception
        ClientService mockService = org.mockito.Mockito.mock(ClientService.class);
        when(mockService.predictChurn(clientId)).thenThrow(new IllegalArgumentException("Invalid client data"));
        
        // Create controller with mock service
        ClientController controller = new ClientController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // When & Then
        mockMvc.perform(get("/clients/{id}/prediction", clientId))
                .andExpect(status().isBadRequest());
    }
    
    private Client createTestClient() {
        Client client = new Client();
        client.setId(1L);
        client.setClientName("Test Client");
        client.setActive(true);
        client.setGender(Gender.MALE);
        client.setClientPhone("123-456-7890");
        client.setNearLocation(true);
        client.setAge(30);
        return client;
    }
}
