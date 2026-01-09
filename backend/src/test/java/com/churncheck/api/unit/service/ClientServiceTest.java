package com.churncheck.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
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
            null,  // partnerId = null para evitar buscar partner
            true, 
            "555-1234", 
            20, 
            12, 
            true, 
            BigDecimal.TEN, 
            BigDecimal.ONE, 
            BigDecimal.valueOf(50.5)
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
            BigDecimal.valueOf(15.5), 
            BigDecimal.valueOf(2.5), 
            BigDecimal.valueOf(75.0)
        );

        Client savedClient = createTestClient();
        savedClient.setId(2L);
        savedClient.setClientName("Jane Smith");  // Actualizar nombre para coincidir con el DTO
        
        // Mock del partner usando reflection para evitar problemas con setters
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
    void shouldThrowExceptionWhenPhoneIsEmpty(){

        // Given (Arrange)
        ClientCreateRequestDTO clientCreateRequestDTO = new ClientCreateRequestDTO(
            "Invalid Client", 
            true, 
            Gender.MALE, 
            true, 
            null, 
            true, 
            "",  // teléfono vacío
            20, 
            12, 
            true, 
            BigDecimal.TEN, 
            BigDecimal.ONE, 
            BigDecimal.valueOf(50.5)
        );

        // When (Act) & Then (Assert)
        org.junit.jupiter.api.Assertions.assertThrows(
            com.churncheck.api.domain.client.DomainException.class,
            () -> clientService.createFromDto(clientCreateRequestDTO),
            "Expected DomainException for empty phone"
        );
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
