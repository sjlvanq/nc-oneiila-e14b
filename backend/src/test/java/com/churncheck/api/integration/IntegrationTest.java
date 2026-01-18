package com.churncheck.api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.sql.init.mode=always",
    "external.prediction.connect-timeout=4000",
    "external.prediction.connection-timeout=4000",
    "external.prediction.read-timeout=4000",
    "server.port=0"
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
        // When & Then: Todos los endpoints protegidos deben retornar 401
        
        // Test 1: GET /clients sin autenticación
        org.springframework.web.client.HttpClientErrorException exception1 = null;
        try {
            restTemplate.getForEntity(createURL("/clients"), String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            exception1 = (org.springframework.web.client.HttpClientErrorException) e;
        }
        
        // Test 2: POST /clients sin autenticación
        org.springframework.web.client.HttpClientErrorException exception2 = null;
        try {
            restTemplate.postForEntity(createURL("/clients"), new ClientCreateRequestDTO(
                "Test", true, Gender.MALE, true, 1L, true, 
                "555-1234", LocalDate.of(2000, 1, 1), 12, true), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            exception2 = e;
        }
        
        // Test 3: GET /clients/1/prediction sin autenticación
        org.springframework.web.client.HttpClientErrorException exception3 = null;
        try {
            restTemplate.getForEntity(createURL("/clients/1/prediction"), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            exception3 = e;
        }
        
        // Then: Verificar que todos retornan 401 UNAUTHORIZED
        assertNotNull(exception1);
        assertEquals(HttpStatus.UNAUTHORIZED, exception1.getStatusCode());
        
        assertNotNull(exception2);
        assertEquals(HttpStatus.UNAUTHORIZED, exception2.getStatusCode());
        
        assertNotNull(exception3);
        assertEquals(HttpStatus.UNAUTHORIZED, exception3.getStatusCode());
        
        // Test 4: Credenciales inválidas
        LoginRequestDTO loginRequest = new LoginRequestDTO("nonexistent@example.com", "wrongpassword");
        
        org.springframework.web.client.HttpClientErrorException loginException = null;
        try {
            restTemplate.postForEntity(createURL("/login"), loginRequest, String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            loginException = e;
        }
        
        // Then: Login rechazado debe retornar 401
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