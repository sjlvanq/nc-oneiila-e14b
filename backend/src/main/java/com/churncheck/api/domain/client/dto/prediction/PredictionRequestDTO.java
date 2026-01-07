package com.churncheck.api.domain.client.dto.prediction;

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
    Integer monthToEndContract,
    Integer lifetime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal avgClassFrequencyTotal,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    BigDecimal avgClassFrequencyCurrentMonth
) {
    
}
