package com.churncheck.api.domain.client;

public record ClientCreateRequestDTO (
    String clientName,
    Boolean active,
    Gender gender,
    Integer nearLocation,
    Integer partnerEmployee,
    Integer promoFriends,
    String clientPhone,
    Integer age,
    Integer contractPeriod,
    Integer monthToEndContract,
    Integer lifetimeMonths,
    Double avgClassFrequencyTotal,
    Double avgClassFrequencyCurrentMonth) {
}
