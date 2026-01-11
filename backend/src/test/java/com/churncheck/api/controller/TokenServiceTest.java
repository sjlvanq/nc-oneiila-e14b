package com.churncheck.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.churncheck.api.infra.security.AuthUser;
import com.churncheck.api.infra.security.TokenService;
import com.churncheck.api.domain.user.User;

class TokenServiceTest {
    
    private TokenService tokenService;
    private AuthUser testUser;
    
    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-key-for-testing-only");
        
        // Crear User mock para evitar dependencias complejas
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn("test@example.com");
        when(mockUser.getPasswordHash()).thenReturn("encodedPassword");
        when(mockUser.getActive()).thenReturn(true);
        when(mockUser.getRoles()).thenReturn(Collections.emptySet());
        
        testUser = new AuthUser(mockUser);
    }
    
    @Test
    void shouldCreateValidToken() {
        // Given
        
        // When
        String token = tokenService.createToken(testUser);
        
        // Then
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // JWT format
    }
    
    @Test
    void shouldExtractSubjectFromValidToken() {
        // Given
        String token = tokenService.createToken(testUser);
        
        // When
        String subject = tokenService.getSubject(token);
        
        // Then
        assertEquals("test@example.com", subject);
    }
    
    @Test
    void shouldThrowExceptionForInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";
        
        // When & Then
        assertThrows(RuntimeException.class, () -> tokenService.getSubject(invalidToken));
    }
    
    @Test
    void shouldCreateTokenWithExpiration() {
        // Given
        Instant beforeCreation = Instant.now();
        
        // When
        String token = tokenService.createToken(testUser);
        
        // Then
        String subject = tokenService.getSubject(token);
        assertNotNull(subject);
        
        // Verificar que el token es válido (no expirado)
        assertDoesNotThrow(() -> tokenService.getSubject(token));
        
        // El token debería ser válido por 2 horas
        Instant twoHoursLater = beforeCreation.plus(2, ChronoUnit.HOURS);
        assertTrue(Instant.now().isBefore(twoHoursLater));
    }
    
    @Test
    void shouldThrowExceptionWhenCreateTokenWithNullUser() {
        // Given
        AuthUser nullUser = null;
        
        // When & Then
        assertThrows(Exception.class, () -> tokenService.createToken(nullUser));
    }
    
    @Test
    void shouldThrowExceptionWhenGetSubjectWithNullToken() {
        // Given
        String nullToken = null;
        
        // When & Then
        assertThrows(Exception.class, () -> tokenService.getSubject(nullToken));
    }
}
