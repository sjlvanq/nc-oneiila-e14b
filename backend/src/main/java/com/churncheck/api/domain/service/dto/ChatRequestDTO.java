package com.churncheck.api.domain.service.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(
    @NotBlank(message = "El mensaje no puede estar vacío")
    String message,
    String conversationId
) {}
