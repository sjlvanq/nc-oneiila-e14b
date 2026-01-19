package com.churncheck.api.domain.client.dto;

public record StatisticsDTO(
    Long total,
    Long active,
    Double averageAge     
) {}
