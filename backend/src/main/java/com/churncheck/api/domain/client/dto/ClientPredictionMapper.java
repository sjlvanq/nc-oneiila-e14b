package com.churncheck.api.domain.client.dto;

import org.springframework.stereotype.Component;
import com.churncheck.api.domain.client.Client;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {
        return new PredictionRequestDTO(
            client.getAge(),
            client.getNearLocation(),
            client.getPartnerEmployee(),
            client.getContractPeriod(),
            client.getMonthToEndContract(),
            client.getLifetimeMonths(),
            client.getAvgClassFrequencyTotal(),
            client.getAvgClassFrequencyCurrentMonth()
        );
    }
    
}
