package com.churncheck.api.domain.client.dto;

public record PredictionRequestDTO(
    Integer age,
    Integer nearLocation,
    Integer partnerEmployee,
    Integer contractPeriod,
    Integer monthToEndContract,
    Integer lifetimeMonths,
    Double avgClassFrequencyTotal,
    Double avgClassFrequencyCurrentMonth
) {
    
}
