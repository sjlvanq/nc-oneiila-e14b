package com.churncheck.api.domain.client.dto;

public record GlobalStatisticsDTO(
    Long total,
    Long active,
    Long inactive,
    Long highRiskCount,
    Double averageAge     
) {}
