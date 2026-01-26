package com.churncheck.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@ExtendWith(MockitoExtension.class)
class ClientPredictionMapperTest {
    @InjectMocks
    private ClientPredictionMapper mapper;

    @Test
    void shouldMapMaleClientCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");

        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);

        // Then
        assertNotNull(result);
        assertEquals(0, result.gender());
        assertEquals((byte)1, result.phone());
        assertEquals((byte)1, result.nearLocation());
        assertEquals((byte)0, result.partner());
        assertEquals((byte)1, result.promoFriends());
        assertEquals((byte)1, result.groupVisits());
        assertEquals(30, result.age());
        assertEquals(12, result.contractPeriod());
    }

    @Test
    void shouldMapFemaleClientCorrectly(){
        // Given
        Client client = createTestClient(Gender.FEMALE, "123-456-7890");
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);

        // Then
        assertNotNull(result);
        assertEquals(1, result.gender());
        assertEquals((byte)1, result.phone());
        assertEquals((byte)1, result.nearLocation());
        assertEquals((byte)0, result.partner());
        assertEquals((byte)1, result.promoFriends());
        assertEquals((byte)1, result.groupVisits());
        assertEquals(30, result.age());
        assertEquals(12, result.contractPeriod());
    }

    @Test
    void shouldHandleNullPhoneCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, null);
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals((byte)0, result.phone());
        assertEquals(0, result.gender());
        assertEquals((byte)1, result.nearLocation());
        assertEquals((byte)0, result.partner());
        assertEquals((byte)1, result.promoFriends());
        assertEquals((byte)1, result.groupVisits());
        assertEquals(30, result.age());
        assertEquals(12, result.contractPeriod());
    }

    @Test
    void shouldCalculateLifetimeCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        // Client registered 6 months ago
        setRegistrationDate(client, LocalDate.now().minusMonths(6));
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(6, result.lifetime(), "Lifetime should be 6 months");
    }

    @Test
    void shouldCalculateMonthsToEndContractCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        // Contract of 12 months started 6 months ago, 6 months remaining
        setContractStartDate(client, LocalDate.now().minusMonths(6));
        client.setContractPeriod(12);
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(6, result.monthToEndContract(), "Should have 6 months to end contract");
    }

    @Test
    void shouldCalculateNegativeMonthsToEndContractForExpiredContract(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        // Contract of 12 months started 15 months ago, should give 0 months (minimum)
        setContractStartDate(client, LocalDate.now().minusMonths(15));
        client.setContractPeriod(12);
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(0, result.monthToEndContract(), "Expired contract should give 0 months (minimum)");
    }

    @Test
    void shouldMapAdditionalChargesCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        
        // Create charges from different months
        ArrayList<AdditionalCharge> charges = new ArrayList<>();
        
        // Month 1: two charges of $100 and $200 = average $150
        LocalDate month1Date = LocalDate.now().minusMonths(2);
        charges.add(createCharge(month1Date, new BigDecimal("100")));
        charges.add(createCharge(month1Date, new BigDecimal("200")));
        
        // Month 2: one charge of $300 = average $300
        LocalDate month2Date = LocalDate.now().minusMonths(1);
        charges.add(createCharge(month2Date, new BigDecimal("300")));
        
        setAdditionalCharges(client, charges);
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("450.00"), result.avgAdditionalChargesTotal(), 
            "The monthly average should be ($150 + $300) = $450");
    }

    @Test
    void shouldHandleEmptyAdditionalChargesCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        setAdditionalCharges(client, new ArrayList<>());
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.avgAdditionalChargesTotal(), 
            "No charges should give zero average");
    }

    @Test
    void shouldMapBooleanFieldsToByteCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals((byte)1, result.phone(), "Non-null phone should map to 1");
        assertEquals((byte)1, result.nearLocation(), "nearLocation true should map to 1");
        assertEquals((byte)0, result.partner(), "partner null should map to 0");
        assertEquals((byte)1, result.promoFriends(), "promoFriends true should map to 1");
        assertEquals((byte)1, result.groupVisits(), "groupVisits true should map to 1");
    }

    @Test
    void shouldMapFalseBooleanFieldsCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, null);
        setNearLocation(client, false);
        setPromoFriends(client, false);
        setGroupVisits(client, false);
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals((byte)0, result.phone(), "Null phone should map to 0");
        assertEquals((byte)0, result.nearLocation(), "nearLocation false should map to 0");
        assertEquals((byte)0, result.partner(), "partner null should map to 0");
        assertEquals((byte)0, result.promoFriends(), "promoFriends false should map to 0");
        assertEquals((byte)0, result.groupVisits(), "groupVisits false should map to 0");
    }

    @Test
    void shouldMapOtherGenderCorrectly(){
        // Given
        Client client = createTestClient(Gender.MALE, "123-456-7890");
        
        // When
        PredictionRequestDTO result = mapper.toPredictionRequest(client);
        
        // Then
        assertNotNull(result);
        assertEquals(0, result.gender(), "OTHER gender should map to 0");
    }

    private Client createTestClient(Gender gender, String phone) {
        Client client = new Client();
        client.setId(1L);
        client.setGender(gender);
        setNearLocation(client, true);
        setPromoFriends(client, true);
        client.setClientPhone(phone);
        client.setContractPeriod(12);
        setGroupVisits(client, true);
        client.setAge(30);
        setContractStartDate(client, LocalDate.now().minusMonths(6));
        setRegistrationDate(client, LocalDate.now().minusMonths(6));
        client.setActive(true);
        return client;
    }

    private AdditionalCharge createCharge(LocalDate date, BigDecimal amount) {
        AdditionalCharge charge = new AdditionalCharge();
        setChargeDate(charge, date);
        setAmount(charge, amount);
        return charge;
    }

    private void setRegistrationDate(Client client, LocalDate date) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("registrationDate");
            field.setAccessible(true);
            field.set(client, date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new RuntimeException("Error setting registrationDate", e);
        }
    }

    private void setContractStartDate(Client client, LocalDate date) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("contractStartDate");
            field.setAccessible(true);
            field.set(client, date);
        } catch (Exception e) {
            throw new RuntimeException("Error setting contractStartDate", e);
        }
    }

    private void setChargeDate(AdditionalCharge charge, LocalDate date) {
        try {
            java.lang.reflect.Field field = charge.getClass().getDeclaredField("chargeDate");
            field.setAccessible(true);
            field.set(charge, date);
        } catch (Exception e) {
            throw new RuntimeException("Error setting chargeDate", e);
        }
    }

    private void setAmount(AdditionalCharge charge, BigDecimal amount) {
        try {
            java.lang.reflect.Field field = charge.getClass().getDeclaredField("amount");
            field.setAccessible(true);
            field.set(charge, amount);
        } catch (Exception e) {
            throw new RuntimeException("Error setting amount", e);
        }
    }

    private void setAdditionalCharges(Client client, ArrayList<AdditionalCharge> charges) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("additionalCharges");
            field.setAccessible(true);
            field.set(client, charges);
        } catch (Exception e) {
            throw new RuntimeException("Error setting additionalCharges", e);
        }
    }

    private void setNearLocation(Client client, boolean nearLocation) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("nearLocation");
            field.setAccessible(true);
            field.set(client, nearLocation);
        } catch (Exception e) {
            throw new RuntimeException("Error setting nearLocation", e);
        }
    }

    private void setPromoFriends(Client client, boolean promoFriends) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("promoFriends");
            field.setAccessible(true);
            field.set(client, promoFriends);
        } catch (Exception e) {
            throw new RuntimeException("Error setting promoFriends", e);
        }
    }

    private void setGroupVisits(Client client, boolean groupVisits) {
        try {
            java.lang.reflect.Field field = client.getClass().getDeclaredField("groupVisits");
            field.setAccessible(true);
            field.set(client, groupVisits);
        } catch (Exception e) {
            throw new RuntimeException("Error setting groupVisits", e);
        }
    }
}
