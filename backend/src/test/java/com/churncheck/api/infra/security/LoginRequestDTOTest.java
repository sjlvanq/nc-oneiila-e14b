package com.churncheck.api.infra.security;

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
class LoginRequestDTOTest {
    
    private Validator validator;
    
    public LoginRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    @Test
    void shouldValidateCorrectCredentials() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("user@example.com", "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void shouldRejectInvalidEmail() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("invalid-email", "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("Email"));
    }
    
    @Test
    void shouldRejectBlankEmail() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("", "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("NotBlank"));
    }
    
    @Test
    void shouldRejectNullEmail() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO(null, "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("NotBlank"));
    }
    
    @Test
    void shouldRejectBlankPassword() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("user@example.com", "");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("NotBlank"));
    }
    
    @Test
    void shouldRejectNullPassword() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("user@example.com", null);
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("NotBlank"));
    }
    
    @Test
    void shouldRejectEmailWithSpaces() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO(" user@example.com ", "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("Email"));
    }
    
    @Test
    void shouldRejectEmailWithoutDomain() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO("user@", "password123");
        
        // When
        Set<ConstraintViolation<LoginRequestDTO>> violations = 
            validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessageTemplate().contains("Email"));
    }
}
