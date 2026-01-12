package com.churncheck.api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.infra.security.LoginRequestDTO;
import com.churncheck.api.ApiApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {ApiApplication.class, TestConfig.class})
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class IntegrationTest {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @Test
    void shouldCreateRestTemplateBean() {
        // Given: Verificar que el bean RestTemplate se crea correctamente
        assertNotNull(restTemplate);
    }
    
    @Test
    void shouldProtectEndpointsWithoutAuthentication() {
        // Given: Sin autenticación
        
        // When & Then: Todos los endpoints protegidos deben retornar 403
        org.springframework.web.client.HttpClientErrorException exception1 = null;
        org.springframework.web.client.HttpClientErrorException exception2 = null;
        org.springframework.web.client.HttpClientErrorException exception3 = null;
        
        try {
            restTemplate.getForEntity(createURL("/clients"), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            exception1 = e;
        }
        
        try {
            restTemplate.postForEntity(createURL("/clients"), new ClientCreateRequestDTO(
                "Test", true, Gender.MALE, true, 1L, true, 
                "555-1234", 30, 12, true
                //, new BigDecimal("2.5"), new BigDecimal("3.0")
                ), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            exception2 = e;
        }
        
        try {
            restTemplate.getForEntity(createURL("/clients/1/prediction"), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            exception3 = e;
        }
        
        // Then
        assertNotNull(exception1);
        assertEquals(HttpStatus.UNAUTHORIZED, exception1.getStatusCode());
        
        assertNotNull(exception2);
        assertEquals(HttpStatus.UNAUTHORIZED, exception2.getStatusCode());
        
        assertNotNull(exception3);
        assertEquals(HttpStatus.UNAUTHORIZED, exception3.getStatusCode());
        
        // Given: Credenciales inválidas
        LoginRequestDTO loginRequest = new LoginRequestDTO("nonexistent@example.com", "wrongpassword");
        
        // When: Intentar login
        org.springframework.web.client.HttpClientErrorException loginException = null;
        try {
            restTemplate.postForEntity(createURL("/login"), loginRequest, String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            loginException = e;
        }
        
        // Then: Login rechazado
        assertNotNull(loginException);
        assertEquals(HttpStatus.UNAUTHORIZED, loginException.getStatusCode());
    }
        
    private String createURL(String path) {
        return "http://localhost:" + port + path;
    }
}

@Configuration
class TestConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
