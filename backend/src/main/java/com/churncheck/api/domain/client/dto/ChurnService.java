package com.churncheck.api.service;

import org.springframework.stereotype.Service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Gender;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;
import com.churncheck.api.infra.clients.dto.PredictionRequestDTO;

@Service
public class ChurnService {

    private final ClientRepository clientRepository;
    private final PredictionClient predictionClient;

    public ChurnService(ClientRepository clientRepository,
                        PredictionClient predictionClient) {
        this.clientRepository = clientRepository;
        this.predictionClient = predictionClient;
    }

    public ClientFullResponseDTO predict(Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        PredictionRequestDTO request = mapToPredictionRequest(client);
        PredictionResponseDTO response = predictionClient.predict(request);

        return new ClientFullResponseDTO(client, response);
    }

    private PredictionRequestDTO mapToPredictionRequest(Client client) {

        byte gender =
            client.getGender() == Gender.MALE ? (byte) 0 :
            client.getGender() == Gender.FEMALE ? (byte) 1 : (byte) 0;

        byte hasPhone = client.getClientPhone() != null ? (byte) 1 : (byte) 0;

        return new PredictionRequestDTO(
            gender,
            hasPhone,
            client.getNearLocation().byteValue(),
            client.getPartnerEmployee().byteValue(),
            client.getPromoFriends().byteValue(),
            client.getAge(),
            client.getLifetimeMonths(),
            client.getAvgClassFrequencyTotal().doubleValue(),
            client.getAvgClassFrequencyCurrentMonth().doubleValue()
        );
    }
}
