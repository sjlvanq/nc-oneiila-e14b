package com.churncheck.api.service;

import org.springframework.stereotype.Service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;
import com.churncheck.api.infra.clients.dto.PredictionRequestDTO;

@Service
public class ChurnService {

    private final PredictionClient predictionClient;

    public ChurnService(ClientRepository clientRepository,
                        PredictionClient predictionClient) {
        this.predictionClient = predictionClient;
    }

    public PredictionResponseDTO predict(Client client) {
        PredictionRequestDTO request = mapToPredictionRequest(client);
        PredictionResponseDTO response = predictionClient.predict(request);
        return response;
    }

    private PredictionRequestDTO mapToPredictionRequest(Client client) {

        Integer gender =
            client.getGender() == Gender.MALE ?  0 :
            client.getGender() == Gender.FEMALE ?  1 :  0;

        byte hasPhone = client.getClientPhone() != null ? (byte) 1 : (byte) 0;
        byte hasGroupVisit = client.getGroupVisit() != null ? (byte) 1 : (byte) 0;
        
        // Sugerencia en 65820dcdfb98b80a7331e0b43c27a4bfa134ab3f:
        // client.getGender() != null ? client.getGender().toString() : "UNKNOWN",
        // client.getAvgClassFrequencyTotal() != null ? client.getAvgClassFrequencyTotal().doubleValue() : 0.0,
        // client.getAvgClassFrequencyCurrentMonth() != null ? client.getAvgClassFrequencyCurrentMonth().doubleValue() : 0.0
        
        return new PredictionRequestDTO(
        gender,                                         // Integer
        client.getNearLocation().byteValue(),            // Byte
        client.getPartnerEmployee().byteValue(),         // Byte
        client.getPromoFriends().byteValue(),            // Byte
        hasPhone,                                        // Byte
        client.getContractPeriod(),				          // Integer
        hasGroupVisit,             						// Byte
        client.getAge(),                                 // Integer
        client.getAvgAdditionalChargesTotal(),            // Double
        client.getMonthToEndContract(),                   // Integer
        client.getLifetimeMonths(),                       // Integer
        client.getAvgClassFrequencyTotal().doubleValue(), // Double
        client.getAvgClassFrequencyCurrentMonth().doubleValue() // Double
        );
   }
}
