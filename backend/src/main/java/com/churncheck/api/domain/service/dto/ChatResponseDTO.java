package com.churncheck.api.domain.service.dto;

public record ChatResponseDTO(
    String response,
    String conversation_id,
    String timestamp
) {}
