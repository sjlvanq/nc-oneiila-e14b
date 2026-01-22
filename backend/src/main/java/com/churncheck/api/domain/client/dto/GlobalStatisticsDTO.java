package com.churncheck.api.domain.client.dto;

public record GlobalStatisticsDTO(
    Long total,
    Long active,
    Double averageAge     
) {}
