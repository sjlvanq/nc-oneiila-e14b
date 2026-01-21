package com.churncheck.api.domain.client.dto.statistics;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AdditionalChargesDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
        BigDecimal totalAmount,
        List<CategoryChargeDTO> breakdown
) {}
