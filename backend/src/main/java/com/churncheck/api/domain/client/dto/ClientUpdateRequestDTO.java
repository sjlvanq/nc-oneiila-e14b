package com.churncheck.api.domain.client.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequestDTO(
    @NotNull
    Long id,

    @Size(min = 3, max = 100)
    String clientName,

    String clientPhone,

    Boolean nearLocation,

    Integer age
) {

}
