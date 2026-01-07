package com.churncheck.api.domain.client;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;

@Component
public class ClientPredictionMapper {

    public PredictionRequestDTO toPredictionRequest(Client client) {
    	Integer gender =
                client.getGender() == Gender.MALE ?  0 :
                client.getGender() == Gender.FEMALE ?  1 :  0;
    	
        Byte hasPhone = (byte) (client.getClientPhone() != null ? 1 : 0);
        Byte hasPartner = (byte) (client.getPartner() != null ? 1 : 0);
        
        Byte isNearLocation = (byte) (client.getNearLocation() ? 1 : 0);
        Byte isGroupVisits = (byte) (client.getGroupVisits() ? 1 : 0);
        Byte isPromoFriends = (byte) (client.getPromoFriends() ? 1 : 0);
        
        LocalDate today = LocalDate.now();
        Integer lifetime = (int) ChronoUnit.MONTHS.between(client.getContractStartDate(), today);
        LocalDate endContractDate = client.getContractStartDate().plusMonths(client.getContractPeriod());
        Integer monthsToEndContract = Period.between(today, endContractDate).getMonths();
        
        return new PredictionRequestDTO(
        		gender,
                isNearLocation,
                hasPartner,
                isPromoFriends,
                hasPhone,
                client.getContractPeriod(),
                isGroupVisits,
                client.getAge(),
                client.getAvgAdditionalChargesTotal(),
                monthsToEndContract,
                lifetime,
                client.getAvgClassFrequencyTotal(),
                client.getAvgClassFrequencyCurrentMonth()
        );
    }
    
}
