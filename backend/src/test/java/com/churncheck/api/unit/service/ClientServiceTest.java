package com.churncheck.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.service.ChurnService;
import com.churncheck.api.service.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private com.churncheck.api.domain.partner.PartnerRepository partnerRepository;

    @Mock
    private ChurnService churnService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClientSuccessfully(){

        // Given (Arrange)
        ClientCreateRequestDTO clientCreateRequestDTO = new ClientCreateRequestDTO(
            "John Doe", 
            true, 
            Gender.MALE, 
            true, 
            null,  // partnerId = null to avoid searching for partner
            true, 
            "555-1234", 
            20, 
            12, 
            true, 
            new BigDecimal("2.5"), 
            new BigDecimal("3.0")
        );

        Client savedClient = createTestClient();
        
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // When (Act)
        ClientResponseDTO responseDTO = clientService.createFromDto(clientCreateRequestDTO);

        // Then (Assert)
        assertNotNull(responseDTO);
        assertEquals("John Doe", responseDTO.clientName());
        verify(clientRepository).save(any(Client.class));
    }
    
    @Test
    void shouldCreateClientWithPartnerSuccessfully(){

        // Given (Arrange)
        ClientCreateRequestDTO clientCreateRequestDTO = new ClientCreateRequestDTO(
            "Jane Smith", 
            true, 
            Gender.FEMALE, 
            true, 
            1L,
            true, 
            "555-5678", 
            25, 
            24, 
            true, 
            new BigDecimal("1.5"), 
            new BigDecimal("2.0")
        );

        Client savedClient = createTestClient();
        savedClient.setId(2L);
        savedClient.setClientName("Jane Smith");  // Update name to match DTO
        
        // Partner mock using reflection to avoid problems with setters
        com.churncheck.api.domain.partner.Partner mockPartner = new com.churncheck.api.domain.partner.Partner();
        try {
            java.lang.reflect.Field idField = mockPartner.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(mockPartner, 1L);
            
            java.lang.reflect.Field nameField = mockPartner.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(mockPartner, "Test Partner");
        } catch (Exception e) {
            throw new RuntimeException("Error setting partner fields", e);
        }
        
        when(partnerRepository.findById(1L)).thenReturn(java.util.Optional.of(mockPartner));
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // When (Act)
        ClientResponseDTO responseDTO = clientService.createFromDto(clientCreateRequestDTO);

        // Then (Assert)
        assertNotNull(responseDTO);
        assertEquals("Jane Smith", responseDTO.clientName());
        verify(clientRepository).save(any(Client.class));
        verify(partnerRepository).findById(1L);
    }
    
    @Test
    void shouldUpdateClientSuccessfully() {
        // Given
        ClientUpdateRequestDTO updateDTO = new ClientUpdateRequestDTO(
            1L, "Updated Name", "555-9999", true, 25
        );
        
        Client existingClient = createTestClient();
        Client updatedClient = createTestClient();
        updatedClient.setClientName("Updated Name");
        updatedClient.setClientPhone("555-9999");
        updatedClient.setNearLocation(true);
        updatedClient.setAge(25);
        
        when(clientRepository.getReferenceById(1L)).thenReturn(existingClient);
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        // When
        ClientResponseDTO result = clientService.updateClient(updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.clientName());
        assertEquals("555-9999", result.clientPhone());
        assertTrue(result.nearLocation());
        assertEquals(25, result.age());
        verify(clientRepository).getReferenceById(1L);
        verify(clientRepository).save(existingClient);
    }
    
    @Test
    void shouldFindAllActiveClientsSuccessfully() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Client client1 = createTestClient();
        Client client2 = createTestClient();
        client2.setId(2L);
        client2.setClientName("Second Client");
        
        Page<Client> clientPage = new PageImpl<>(java.util.List.of(client1, client2), pageable, 2);
        
        when(clientRepository.findAllByActiveTrue(pageable)).thenReturn(clientPage);

        // When
        Page<ClientListResponseDTO> result = clientService.findAllByActiveTrue(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).clientName());
        assertEquals("Second Client", result.getContent().get(1).clientName());
        verify(clientRepository).findAllByActiveTrue(pageable);
    }
    
    @Test
    void shouldFindClientByIdSuccessfully() {
        // Given
        Client client = createTestClient();
        when(clientRepository.getReferenceById(1L)).thenReturn(client);

        // When
        ClientResponseDTO result = clientService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.clientName());
        assertEquals(1L, result.id());
        verify(clientRepository).getReferenceById(1L);
    }
    
    @Test
    void shouldDeleteClientSuccessfully() {
        // Given
        Client client = createTestClient();
        when(clientRepository.getReferenceById(1L)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // When
        clientService.deleteClient(1L);

        // Then
        verify(clientRepository).getReferenceById(1L);
        verify(clientRepository).save(client);
        // Check that the client has been marked as inactive
        assertTrue(!client.getActive());
    }
    
    @Test
    void shouldPredictChurnSuccessfully() {
        // Given
        Client client = createTestClient();
        PredictionResponseDTO prediction = new PredictionResponseDTO(
            (byte) 1, 
            0.85, 
            java.time.Instant.now()
        );
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(churnService.predict(client)).thenReturn(prediction);

        // When
        ClientFullResponseDTO result = clientService.predictChurn(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.clientName());
        assertEquals((byte) 1, result.churn());
        assertEquals(0.85, result.probability());
        verify(clientRepository).findById(1L);
        verify(churnService).predict(client);
    }
    
    @Test
    void shouldThrowExceptionWhenPredictChurnClientNotFound() {
        // Given
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            java.util.NoSuchElementException.class,
            () -> clientService.predictChurn(999L)
        );
        verify(clientRepository).findById(999L);
    }
    
    private Client createTestClient() {
        Client client = new Client();
        client.setId(1L);
        client.setClientName("John Doe");
        client.setGender(Gender.MALE);
        client.setNearLocation(true);
        client.setPromoFriends(true);
        client.setClientPhone("555-1234");
        client.setContractPeriod(12);
        client.setGroupVisit(true);
        client.setAge(20);
        client.setContractStartDate(java.time.LocalDate.now().minusMonths(6));
        client.setAvgClassFrequencyTotal(BigDecimal.TEN);
        client.setAvgClassFrequencyCurrentMonth(BigDecimal.ONE);
        client.setActive(true);
        return client;
    }
}
