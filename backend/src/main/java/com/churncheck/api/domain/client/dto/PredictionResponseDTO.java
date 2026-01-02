package com.churncheck.api.domain.client.dto;

public record PredictionResponseDTO(
        int prediction,
        double probability
) {}
