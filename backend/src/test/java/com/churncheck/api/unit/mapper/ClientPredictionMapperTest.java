package com.churncheck.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@ExtendWith(MockitoExtension.class)
class ClientPredictionMapperTest {
    @InjectMocks
    private ClientPredictionMapper mapper;

    @Test
    void shouldMapMaleClientCorrectly(){
        // Given (Arrange)
        Client client = createTestClient(Gender.MALE, "123-456-7890");

        // When (Act)
        PredictionRequestDTO result = mapper.toPredictionRequest(client);

        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(0, result.gender(), "Género MALE debe mapearse a 0");
        assertEquals((byte)1, result.phone(), "Teléfono no nulo debe mapearse a 1");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true debe mapearse a 1");
        assertEquals((byte)0, result.partner(), "partner null debe mapearse a 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true debe mapearse a 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true debe mapearse a 1");
        assertEquals(30, result.age(), "Edad debe ser 30");
        assertEquals(12, result.contractPeriod(), "Contract period debe ser 12");
        // Nota: avgAdditionalChargesTotal no se puede establecer directamente, se omite la validación
    }

    @Test
    void shouldMapFemaleClientCorrectly(){
        // Given (Arrange)
        Client client = createTestClient(Gender.FEMALE, "123-456-7890");
        
        // When (Act)
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(1, result.gender(), "Género FEMALE debe mapearse a 1");
        assertEquals((byte)1, result.phone(), "Teléfono no nulo debe mapearse a 1");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true debe mapearse a 1");
        assertEquals((byte)0, result.partner(), "partner null debe mapearse a 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true debe mapearse a 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true debe mapearse a 1");
        assertEquals(30, result.age(), "Edad debe ser 30");
        assertEquals(12, result.contractPeriod(), "Contract period debe ser 12");
        // Nota: avgAdditionalChargesTotal no se puede establecer directamente, se omite la validación
    }

    @Test
    void shouldHandleNullPhoneCorrectly(){
        // Given (Arrange)
        Client client = createTestClient(Gender.MALE, null);
        
        // When (Act)
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)0, result.phone(), "Teléfono nulo debe mapearse a 0");
        assertEquals(0, result.gender(), "Género MALE debe mapearse a 0");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true debe mapearse a 1");
        assertEquals((byte)0, result.partner(), "partner null debe mapearse a 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true debe mapearse a 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true debe mapearse a 1");
        assertEquals(30, result.age(), "Edad debe ser 30");
        assertEquals(12, result.contractPeriod(), "Contract period debe ser 12");
    }

    @Test
    void shouldHandleNonNullPhoneCorrectly(){
        // Given (Arrange)
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        
        // When (Act)
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)1, result.phone(), "Teléfono no nulo debe mapearse a 1");
        assertEquals(0, result.gender(), "Género MALE debe mapearse a 0");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true debe mapearse a 1");
        assertEquals((byte)0, result.partner(), "partner null debe mapearse a 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true debe mapearse a 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true debe mapearse a 1");
        assertEquals(30, result.age(), "Edad debe ser 30");
        assertEquals(12, result.contractPeriod(), "Contract period debe ser 12");
    }

    @Test
    void shouldMapBooleanFieldsToByteCorrectly(){
        // Given (Arrange)
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        
        // When (Act)
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then (Assert)
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals((byte)1, result.phone(), "Teléfono no nulo debe mapearse a 1");
        assertEquals(0, result.gender(), "Género MALE debe mapearse a 0");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true debe mapearse a 1");
        assertEquals((byte)0, result.partner(), "partner null debe mapearse a 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true debe mapearse a 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true debe mapearse a 1");
        assertEquals(30, result.age(), "Edad debe ser 30");
        assertEquals(12, result.contractPeriod(), "Contract period debe ser 12");
    }

    private Client createTestClient(Gender gender, String phone) {
        Client client = new Client();
        client.setId(1L);
        client.setGender(gender);
        client.setNearLocation(true);
        // Nota: No hay setter para partner, se deja como null por defecto
        client.setPromoFriends(true);
        client.setClientPhone(phone);
        client.setContractPeriod(12);
        client.setGroupVisit(true);
        client.setAge(30);
        client.setContractStartDate(LocalDate.now().minusMonths(6));
        // Nota: No hay setter público para avgAdditionalChargesTotal
        client.setAvgClassFrequencyTotal(new BigDecimal("2.5"));
        client.setAvgClassFrequencyCurrentMonth(new BigDecimal("3.0"));
        client.setActive(true);
        return client;
    }
}
