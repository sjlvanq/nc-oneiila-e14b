package com.churncheck.api.domain.client.dto;

import java.time.Instant;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;

public record ClientFullResponseDTO(
        Long id,
        String clientName,
        String clientPhone,
        Integer age,
        Byte churn,
        Double probability,
        Instant timestamp
) {
    public ClientFullResponseDTO(Client client, PredictionResponseDTO response) {
        this(
            client.getId(),
            client.getClientName(),
            client.getClientPhone(),
            client.getAge(),
            response.churn(),
            response.probability(),
            response.timestamp()
        );
    }
}
