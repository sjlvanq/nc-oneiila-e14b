package com.churncheck.api.domain.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientCreateRequestDTO(
    @NotBlank
    String clientName,

    @Email
    @NotBlank
    String clientEmail,
    
    @NotNull
    Gender gender,

    @NotBlank
    String clientPhone,

    Integer nearLocation,

    @NotNull
    Integer age

) {
    
}
