package com.churncheck.api.domain.client.dto.statistics;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CategoryChargeDTO(
        String type,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
        BigDecimal amount,
        Double percentage
) {}
