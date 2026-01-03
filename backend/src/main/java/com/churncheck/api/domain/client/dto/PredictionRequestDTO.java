package com.churncheck.api.domain.client.dto;

public record PredictionRequestDTO(
    Integer age,
    String gender,
    Integer nearLocation,
    Integer partnerEmployee,
    Integer promoFriends,
    Integer contractPeriod,
    Integer monthToEndContract,
    Integer lifetimeMonths,
    Double avgClassFrequencyTotal,
    Double avgClassFrequencyCurrentMonth
) {
    
}
