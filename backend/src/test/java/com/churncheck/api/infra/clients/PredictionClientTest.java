package com.churncheck.api.infra.clients;

//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.test.web.client.MockRestServiceServer;
//@RestClientTest(PredictionClient.class)

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.churncheck.api.infra.clients.dto.PredictionRequestDTO;

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
            1, (byte)1, (byte)0, (byte)1, (byte)1, 12, (byte)0, 30, 150.5, 6, 24, 2.5, 3.0
        );
        var response = predictionClient.predict(request);
        assertNotNull(response);
        assertNotNull(response.churn());
        assertNotNull(response.probability());
        assertNotNull(response.timestamp());
        System.out.println("El servidor responde: "+response.toString());
    }
    
}
