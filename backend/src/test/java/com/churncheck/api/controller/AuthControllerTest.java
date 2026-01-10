package com.churncheck.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.churncheck.api.infra.security.AuthUser;
import com.churncheck.api.infra.security.AuthUserService;
import com.churncheck.api.infra.security.LoginRequestDTO;
import com.churncheck.api.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

class AuthControllerTest {
    
    private AuthController authController;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private TokenService tokenService;
    private AuthenticationManager authenticationManager;
    private AuthUserService authUserService;
    
    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        authenticationManager = mock(AuthenticationManager.class);
        authUserService = mock(AuthUserService.class);
        objectMapper = new ObjectMapper();
        
        authController = new AuthController(tokenService, authenticationManager, authUserService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }
    
    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Given
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "password123");
        String expectedToken = "jwt-token-example";
        AuthUser mockUser = mock(AuthUser.class);
        Authentication mockAuth = mock(Authentication.class);
        
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(mockUser);
        doAnswer(invocation -> null).when(authUserService).validateAccess(any());
        when(tokenService.createToken(any())).thenReturn(expectedToken);
        
        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));
    }
    
    @Test
    void shouldReturn401ForInvalidCredentials() throws Exception {
        // Given
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "wrong-password");
        
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any());
        
        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void shouldReturn400ForInvalidEmail() throws Exception {
        // Given
        String invalidJson = "{\"email\":\"invalid-email\",\"password\":\"password123\"}";
        
        // When & Then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
    
    @RestControllerAdvice
    static class TestExceptionHandler extends ResponseEntityExceptionHandler {
        
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
