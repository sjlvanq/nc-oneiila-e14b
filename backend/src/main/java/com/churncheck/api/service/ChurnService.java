package com.churncheck.api.service;

import org.springframework.stereotype.Service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;

@Service
public class ChurnService {

    private final PredictionClient predictionClient;
    private final ClientPredictionMapper mapper;

    public ChurnService(PredictionClient predictionClient, ClientPredictionMapper mapper) {
        this.predictionClient = predictionClient;
        this.mapper = mapper;
    }

    public PredictionResponseDTO predict(Client client) {
        PredictionRequestDTO request = mapper.toPredictionRequest(client);
        return predictionClient.predict(request);
    }

}
