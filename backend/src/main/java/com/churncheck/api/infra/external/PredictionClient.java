package com.churncheck.api.infra.external;

import com.churncheck.api.domain.client.dto.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PredictionClient {
    
    private final String mlServiceUrl;
    
    public PredictionClient(@Value("${ml.service.url:http://localhost:8000}") String mlServiceUrl) {
        this.mlServiceUrl = mlServiceUrl;
    }
    
    public PredictionResponseDTO predict(PredictionRequestDTO request) {
        try {
            // TODO: Implementar llamada real al servicio ML
            // Por ahora, devuelve un mock para que no falle el devC
            
            return new PredictionResponseDTO(
                "Will continue",  // Predicción mock
                0.25,             // Probabilidad mock
                LocalDateTime.now(),
                "mock-v1.0"
            );
            
        } catch (Exception e) {
            // TODO: Manejo específico de errores de conexión
            throw new RuntimeException("Error calling ML service: " + e.getMessage());
        }
    }
}
