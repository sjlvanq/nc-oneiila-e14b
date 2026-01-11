package com.churncheck.api.domain.client.dto.prediction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PredictionRequestDTO(
    Integer gender,
    Byte nearLocation,
    Byte partner,
    Byte promoFriends,
    Byte phone,
    Integer contractPeriod,
    Byte groupVisits,
    Integer age,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal avgAdditionalChargesTotal,

    @NotNull(message = "monthToEndContract is required")
    @Min(value = 0, message = "monthToEndContract cannot be negative")
    Integer monthToEndContract,

    @NotNull(message = "lifetime is required")
    @Min(value = 0, message = "lifetime cannot be negative")
    @Max(value = 31, message = "lifetime cannot be greater than 31")
    Integer lifetime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal avgClassFrequencyTotal,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal avgClassFrequencyCurrentMonth
) {
}
