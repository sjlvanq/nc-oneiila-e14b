package com.churncheck.api.infra.clients.dto;

public record PredictionRequestDTO(
    Integer gender,
    Byte nearLocation,
    Byte partner,
    Byte promoFriends,
    Byte phone,
    Integer contractPeriod,
    Byte groupVisits,
    Integer age,
    Double avgAdditionalChargesTotal,
    Integer monthToEndContract,
    Integer lifetime,
    Double avgClassFrequencyTotal,
    Double avgClassFrequencyCurrentMonth	
) {}
