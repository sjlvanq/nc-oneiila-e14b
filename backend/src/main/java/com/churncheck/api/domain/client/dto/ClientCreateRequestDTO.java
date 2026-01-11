package com.churncheck.api.domain.client.dto;

import com.churncheck.api.domain.client.Gender;

public record ClientCreateRequestDTO (
    String clientName,
    Boolean active,
    Gender gender,
    Boolean nearLocation,
    Long partnerId,
    Boolean promoFriends,
    String clientPhone,
    Integer age,
    Integer contractPeriod,
    Boolean groupVisits
) {}
