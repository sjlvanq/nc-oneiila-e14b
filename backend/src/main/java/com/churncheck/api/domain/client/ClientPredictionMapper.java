package com.churncheck.api.domain.client;

import org.springframework.stereotype.Component;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {
        return new PredictionRequestDTO(
            client.getAge(),
            client.getContractPeriod(),
            client.getMonthToEndContract(),
            client.getLifetime(),
            client.getGroupVisit(),
            client.getAvgClassFrequencyTotal(),
            client.getAvgClassFrequencyCurrentMonth(),
            client.getAvgAdditionalChargesTotal()
        );
    }
    
}
