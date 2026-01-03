package com.churncheck.api.domain.client.dto;

import com.churncheck.api.domain.client.Client;

public class ClientMapper {
    
    public static PredictionRequestDTO toPredictionRequestDTO(Client client) {
        return new PredictionRequestDTO(
            client.getAge(),
            client.getGender() != null ? client.getGender().toString() : "UNKNOWN",
            client.getNearLocation(),
            client.getPartnerEmployee(),
            client.getPromoFriends(),
            client.getContractPeriod(),
            client.getMonthToEndContract(),
            client.getLifetimeMonths(),
            client.getAvgClassFrequencyTotal() != null ? client.getAvgClassFrequencyTotal().doubleValue() : 0.0,
            client.getAvgClassFrequencyCurrentMonth() != null ? client.getAvgClassFrequencyCurrentMonth().doubleValue() : 0.0
        );
    }
}
