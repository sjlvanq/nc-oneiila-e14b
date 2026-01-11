package com.churncheck.api.domain.client.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequestDTO(

    @NotNull
    Long id,

    @Size(min = 3, max = 100)
    String clientName,

    String clientPhone,

    Boolean nearLocation,

    @Min(18)
    @Max(41)
    Integer age
) {
}
