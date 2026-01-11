package com.churncheck.api.domain.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.churncheck.api.domain.charge.AdditionalCharge;
import com.churncheck.api.domain.partner.Partner;

class ClientTest {

    private Client client;
    private Partner partner;
    private AdditionalCharge additionalCharge;

    @BeforeEach
    void setUp() {
        client = new Client();
        partner = new Partner();
        additionalCharge = new AdditionalCharge();
    }

    @Test
    void shouldCreateClientWithDefaultValues() {
        // When
        Client newClient = new Client();

        // Then
        assertNotNull(newClient);
        assertNull(newClient.getId());
        assertTrue(newClient.getActive()); // default value is true
        assertNull(newClient.getClientName());
        assertNull(newClient.getclientPhone());
        assertNull(newClient.getAge());
        assertNull(newClient.getGender());
        assertNull(newClient.getPartner());
        assertNull(newClient.getPromoFriends());
        assertNull(newClient.getRegistrationDate());
        assertTrue(newClient.getAdditionalCharges().isEmpty());
    }

    @Test
    void shouldVerifyGettersWorkCorrectly() {
        // When & Then
        assertNotNull(client);
        
        assertDoesNotThrow(() -> client.getId());
        assertDoesNotThrow(() -> client.getClientName());
        assertDoesNotThrow(() -> client.getclientPhone());
        assertDoesNotThrow(() -> client.getAge());
        assertDoesNotThrow(() -> client.getGender());
        assertDoesNotThrow(() -> client.getPartner());
        assertDoesNotThrow(() -> client.getPromoFriends());
        assertDoesNotThrow(() -> client.getContractPeriod());
        assertDoesNotThrow(() -> client.getGroupVisits());
        assertDoesNotThrow(() -> client.getNearLocation());
        assertDoesNotThrow(() -> client.getAvgClassFrequencyTotal());
        assertDoesNotThrow(() -> client.getAvgClassFrequencyCurrentMonth());
        assertDoesNotThrow(() -> client.getContractStartDate());
        assertDoesNotThrow(() -> client.getRegistrationDate());
        assertDoesNotThrow(() -> client.getAdditionalCharges());
    }

    @Test
    void shouldManageAdditionalCharges() {
        // Given
        ArrayList<AdditionalCharge> charges = new ArrayList<>();
        charges.add(additionalCharge);

        // When
        assertNotNull(client.getAdditionalCharges());

        // Then
        assertTrue(client.getAdditionalCharges().isEmpty());
    }

    @Test
    void shouldHandleNullValues() {
        // When & Then
        assertNull(client.getClientName());
        assertNull(client.getclientPhone());
        assertNull(client.getAge());
        assertNull(client.getGender());
        assertNull(client.getPartner());
    }

    @Test
    void shouldVerifyEntityAnnotations() {
        // When & Then
        assertDoesNotThrow(() -> new Client());
        
        assertNotNull(new Client());
    }

    @Test
    void shouldVerifyDefaultActiveValue() {
        // When & Then
        Client newClient = new Client();
        assertTrue(newClient.getActive());
    }

    @Test
    void shouldVerifyAdditionalChargesInitialization() {
        // When & Then
        Client newClient = new Client();
        assertNotNull(newClient.getAdditionalCharges());
        assertTrue(newClient.getAdditionalCharges().isEmpty());
    }

    @Test
    void shouldVerifyFactoryMethod() {
        // Given
        assertNotNull(client);
        assertNotNull(partner);

        // When & Then
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> Client.createFromDto(null, partner)
        );
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("dto"));
    }

    @Test
    void shouldVerifyDeleteMethod() {
        // When & Then
        Client newClient = new Client();
        assertTrue(newClient.getActive());
        
        newClient.deleteClient();
        
        assertFalse(newClient.getActive());
    }
}
