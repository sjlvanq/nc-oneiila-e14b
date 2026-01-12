package com.churncheck.api.domain.client.dto;

import com.churncheck.api.domain.client.Gender;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientCreateRequestDTO (

    @NotBlank
    @Size(min = 3, max = 100)
    String clientName,

    @NotNull
    Boolean active,

    @NotNull
    Gender gender,

    @NotNull
    Boolean nearLocation,

    Long partnerId,

    @NotNull
    Boolean promoFriends,

    @NotBlank
    String clientPhone,

    @NotNull
    @Min(18)
    @Max(41)
    Integer age,

    @NotNull
    @Min(1)
    @Max(12)
    Integer contractPeriod,

    @NotNull
    Boolean groupVisits
) {}
