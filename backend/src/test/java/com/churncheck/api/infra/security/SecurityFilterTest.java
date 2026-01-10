package com.churncheck.api.infra.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.churncheck.api.domain.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {
    
    @Mock
    private TokenService tokenService;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private SecurityFilter securityFilter;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain chain;
    
    @Test
    void shouldAuthenticateValidToken() throws ServletException, IOException {
        // Given
        String validToken = "valid.jwt.token";
        String userEmail = "user@example.com";
        com.churncheck.api.domain.user.User mockUser = mock(com.churncheck.api.domain.user.User.class);
        
        // Mockear todos los métodos necesarios para AuthUser
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn(userEmail);
        when(mockUser.getPasswordHash()).thenReturn("encodedPassword");
        when(mockUser.getActive()).thenReturn(true);
        when(mockUser.getRoles()).thenReturn(java.util.Collections.emptySet());
        
        when(request.getHeader("Authorization")).thenReturn("BEARER " + validToken);
        when(tokenService.getSubject(validToken)).thenReturn(userEmail);
        when(userRepository.findByEmailWithRoles(userEmail)).thenReturn(java.util.Optional.of(mockUser));
        
        // When
        SecurityContextHolder.getContext().setAuthentication(null);
        securityFilter.doFilterInternal(request, response, chain);
        
        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        // El nombre en la autenticación es el email del usuario
        assertEquals(userEmail, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(request, response);
        verify(userRepository).findByEmailWithRoles(userEmail);
    }
    
    @Test
    void shouldRejectInvalidToken() throws ServletException, IOException {
        // Given
        String invalidToken = "invalid.jwt.token";
        
        when(request.getHeader("Authorization")).thenReturn("BEARER " + invalidToken);
        when(tokenService.getSubject(invalidToken))
                .thenThrow(new RuntimeException("Invalid JWT"));
        
        // When & Then - El SecurityFilter real deja que la excepción se propague
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            securityFilter.doFilterInternal(request, response, chain);
        });
        
        // Verificar que la excepción sea la esperada
        assertEquals("Invalid JWT", exception.getMessage());
        
        // La autenticación debe permanecer nula
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        
        // El chain no debe ser llamado porque la excepción detiene el flujo
        verify(chain, never()).doFilter(request, response);
    }
    
    @Test
    void shouldHandleMissingAuthorizationHeader() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);
        
        // When
        SecurityContextHolder.getContext().setAuthentication(null);
        securityFilter.doFilterInternal(request, response, chain);
        
        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldHandleInvalidAuthorizationHeader() throws ServletException, IOException {
        // Given
        String invalidHeader = "INVALID_HEADER token123";
        
        when(request.getHeader("Authorization")).thenReturn(invalidHeader);
        
        // When
        SecurityContextHolder.getContext().setAuthentication(null);
        securityFilter.doFilterInternal(request, response, chain);
        
        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
    
    @Test
    void shouldHandleUserNotFound() throws ServletException, IOException {
        // Given
        String validToken = "valid.jwt.token";
        String userEmail = "nonexistent@example.com";
        
        when(request.getHeader("Authorization")).thenReturn("BEARER " + validToken);
        when(tokenService.getSubject(validToken)).thenReturn(userEmail);
        when(userRepository.findByEmailWithRoles(userEmail)).thenReturn(java.util.Optional.empty());
        
        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> 
                securityFilter.doFilterInternal(request, response, chain));
    }
}
