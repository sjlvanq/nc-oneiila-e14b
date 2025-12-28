package com.churncheck.api.domain.client;

import jakarta.validation.constraints.NotBlank;

public record ClientUpdateRequestDTO(
    @NotBlank
    Long id,

    String clientName,

    String email,

    String phoneRegistered,

    String nearCity,

    String age
) {

}
