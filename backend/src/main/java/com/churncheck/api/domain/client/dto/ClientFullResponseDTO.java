package com.churncheck.api.domain.client.dto;

import com.churncheck.api.domain.client.Client;

public record ClientFullResponseDTO(
        Long id,
        String clientName,
        int prediction,
        double probability
) {
    public ClientFullResponseDTO(Client client, PredictionResponseDTO response) {
        this(
            client.getId(),
            client.getClientName(),
            response.prediction(),
            response.probability()
        );
    }
}
