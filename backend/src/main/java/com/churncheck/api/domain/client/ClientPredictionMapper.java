package com.churncheck.api.domain.client;

import org.springframework.stereotype.Component;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {
    	Integer gender =
                client.getGender() == Gender.MALE ?  0 :
                client.getGender() == Gender.FEMALE ?  1 :  0;
        Integer hasPhone = client.getClientPhone() != null ? 1 : 0;
        return new PredictionRequestDTO(
        		gender,
                client.getNearLocation(),
                client.getPartner(),
                client.getPromoFriends(),
                hasPhone,
                client.getContractPeriod(),
                client.getGroupVisits(),
                client.getAge(),
                client.getAvgAdditionalChargesTotal(),
                client.getMonthToEndContract(),
                client.getLifetime(),
                client.getAvgClassFrequencyTotal(),
                client.getAvgClassFrequencyCurrentMonth()
        );
    }
    
}
