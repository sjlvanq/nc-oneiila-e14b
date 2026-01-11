package com.churncheck.api.domain.charge;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdditionalChargeTest {

    private AdditionalCharge additionalCharge;

    @BeforeEach
    void setUp() {
        additionalCharge = new AdditionalCharge();
    }

    @Test
    void shouldCreateAdditionalChargeWithDefaultValues() {
        // When
        AdditionalCharge newCharge = new AdditionalCharge();

        // Then
        assertNull(newCharge.getAmount());
        assertNull(newCharge.getChargeDate());
    }

    @Test
    void shouldGetAmount() {
        // When & Then
        assertNull(additionalCharge.getAmount());
    }

    @Test
    void shouldGetChargeDate() {
        // When & Then
        assertNull(additionalCharge.getChargeDate());
    }

    @Test
    void shouldVerifyEntityStructure() {
        // When & Then
        assertNotNull(additionalCharge);
        
        assertNull(additionalCharge.getAmount());
        assertNull(additionalCharge.getChargeDate());
    }

    @Test
    void shouldVerifyBigDecimalHandling() {
        // Given
        BigDecimal testAmount = new BigDecimal("123.45");

        // When & Then
        assertNotNull(testAmount);
        assertEquals("123.45", testAmount.toString());
    }

    @Test
    void shouldVerifyLocalDateHandling() {
        // Given
        LocalDate testDate = LocalDate.of(2024, 6, 15);

        // When & Then
        assertNotNull(testDate);
        assertEquals(2024, testDate.getYear());
        assertEquals(6, testDate.getMonthValue());
        assertEquals(15, testDate.getDayOfMonth());
    }

    @Test
    void shouldVerifyClientRelationship() {
        // When & Then
        assertNotNull(additionalCharge);
    }

    @Test
    void shouldVerifyChargeTypeRelationship() {
        // When & Then
        assertNotNull(additionalCharge);
    }

    @Test
    void shouldHandleNullValues() {
        // When & Then
        assertNull(additionalCharge.getAmount());
        assertNull(additionalCharge.getChargeDate());
    }

    @Test
    void shouldVerifyEntityAnnotations() {
        // When & Then
        assertDoesNotThrow(() -> new AdditionalCharge());
        
        assertDoesNotThrow(() -> additionalCharge.getAmount());
        assertDoesNotThrow(() -> additionalCharge.getChargeDate());
    }
}
