package com.churncheck.api.domain.client.dto;
import java.time.LocalDateTime;

public record PredictionResponseDTO(
    String prediction,
    Double probability,
    LocalDateTime timestamp,
    String modelVersion
) {

}
