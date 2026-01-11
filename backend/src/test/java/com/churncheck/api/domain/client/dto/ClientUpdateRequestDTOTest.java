package com.churncheck.api.domain.client.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
class ClientUpdateRequestDTOTest {
    
    private Validator validator;
    
    public ClientUpdateRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    @Test
    void shouldValidateCorrectData() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "Valid Name", "555-1234", true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void shouldRejectNullId() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            null, "Valid Name", "555-1234", true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("NotNull"));
    }
    
    @Test
    void shouldRejectShortName() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "AB", "555-1234", true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("Size"));
    }
    
    @Test
    void shouldRejectLongName() {
        // Given
        String longName = "a".repeat(101); // 101 characters
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, longName, "555-1234", true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("Size"));
    }
    
    @Test
    void shouldAcceptNullName() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, null, "555-1234", true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty()); // Name is not required for update
    }
    
    @Test
    void shouldAcceptNullPhone() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "Valid Name", null, true, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty()); // Phone is not required for update
    }
    
    @Test
    void shouldAcceptNullNearLocation() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "Valid Name", "555-1234", null, 30
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty()); // nearLocation is not required
    }
    
    @Test
    void shouldAcceptNullAge() {
        // Given
        ClientUpdateRequestDTO dto = new ClientUpdateRequestDTO(
            1L, "Valid Name", "555-1234", true, null
        );
        
        // When
        Set<ConstraintViolation<ClientUpdateRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty()); // Age is not required for update
    }
}
