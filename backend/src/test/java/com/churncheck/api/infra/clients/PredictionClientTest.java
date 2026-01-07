package com.churncheck.api.infra.clients;

//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.test.web.client.MockRestServiceServer;
//@RestClientTest(PredictionClient.class)

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;

/**
 * TEST DE INTEGRACIÓN
 * Este test valida la conectividad contra un SERVIDOR REAL CONFIGURADO AD-HOC.
 * Alternativas:
 * - Para pruebas unitarias / slices: RestClientTest
 * - Para un servidor real en localhost: WireMock
 */

@SpringBootTest
@ActiveProfiles("integration") // application-integration.yaml
public class PredictionClientTest {
    @Autowired
    private PredictionClient predictionClient;
    
    @Test
    void shouldReturnPredictionSuccessfully() {
        // Nota: este servidor no discrimina contenido del request
        var request = new PredictionRequestDTO(
            1, 1, 1, 0, 1, 12, 1, 30, 
            new BigDecimal("24.0"), 
            6, 10,
            new BigDecimal("2.5"), 
            new BigDecimal("3.0")
        );
        
        // ✅ El método predict ahora devuelve PredictionResponseDTO
        PredictionResponseDTO response = predictionClient.predict(request);
        
        assertNotNull(response);
        assertNotNull(response.churn());
        assertNotNull(response.probability());
        assertNotNull(response.timestamp());
        System.out.println("El servidor responde: "+response.toString());
    }
    
}
