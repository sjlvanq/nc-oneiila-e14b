package com.churncheck.api.domain.client.dto;

import java.math.BigDecimal;

import com.churncheck.api.domain.client.Gender;

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
    Integer lifetime,
    Integer groupVisit,
    BigDecimal avgClassFrequencyTotal,
    BigDecimal avgClassFrequencyCurrentMonth,
    BigDecimal avgAdditionalChargesTotal) {
}
