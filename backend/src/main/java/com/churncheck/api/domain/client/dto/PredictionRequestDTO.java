package com.churncheck.api.domain.client.dto;

import java.math.BigDecimal;

public record PredictionRequestDTO(
    Integer age,
    Integer contractPeriod,
    Integer monthToEndContract,
    Integer lifetime,
    Integer groupVisits,
    BigDecimal avgClassFrequencyTotal,
    BigDecimal avgClassFrequencyCurrentMonth,
    BigDecimal avgAdditionalChargesTotal
) {
    
}
