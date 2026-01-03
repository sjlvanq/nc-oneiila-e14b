package com.churncheck.api.domain.client;
import java.time.LocalDateTime;

public record ClientFullResponseDTO(
    // datos del cliente
    Long id,
    String clientName,
    String clientEmail,
    
    // datos de predicción
    String prediction,
    Double probability,
    LocalDateTime timestamp,
    
    // metadatos
    String modelVersion,
    Long processingTimeMs // opcional
) {

}
