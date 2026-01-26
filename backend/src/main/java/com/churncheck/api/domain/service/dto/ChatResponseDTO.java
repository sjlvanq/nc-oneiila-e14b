package com.churncheck.api.domain.service.dto;

public record ChatResponseDTO(
    String response,
    String conversationId,
    String timestamp
) {}
