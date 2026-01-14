package com.churncheck.api.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientPredictionMapper;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.clients.PredictionClient;

@Service
public class ChurnService {
    
    private static final Logger logger = Logger.getLogger(ChurnService.class.getName());

    private final PredictionClient predictionClient;
    private final ClientPredictionMapper mapper;
    private final ClientRepository clientRepository;
    
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    public ChurnService(PredictionClient predictionClient, ClientPredictionMapper mapper,
            ClientRepository clientRepository) {
        this.predictionClient = predictionClient;
        this.mapper = mapper;
        this.clientRepository = clientRepository;
    }

    public PredictionResponseDTO predict(Client client) {
        // Verificar si existe caché válido
        if (isCacheValid(client)) {
            
            logger.info("Returning cached prediction for client " +
                    client.getId() + " (cached at: " +
                    client.getLastPredictionTimestamp()+")");
            
            return new PredictionResponseDTO(
                client.getLastPredictionChurn(),
                client.getLastPredictionProbability(),
                client.getLastPredictionTimestamp()
            );
        }
        
        logger.info("Fetching new prediction for client " + client.getId() + " (cache miss or expired)");
        PredictionRequestDTO request = mapper.toPredictionRequest(client);
        PredictionResponseDTO response = predictionClient.predict(request);

        // Guardar en caché
        client.setLastPredictionChurn(response.churn());
        client.setLastPredictionProbability(response.probability());
        client.setLastPredictionTimestamp(response.timestamp());
        clientRepository.save(client);
        
        logger.info("Prediction cached for client " + client.getId() + 
                    ": churn=" + response.churn() + 
                    ", probability=" + response.probability());
        
        return response;   
    }
    
    private boolean isCacheValid(Client client) {
        if (client.getLastPredictionTimestamp() == null) {
            return false;
        }
        
        Instant now = Instant.now();
        Instant cacheExpiry = client.getLastPredictionTimestamp().plus(CACHE_TTL);
        
        return now.isBefore(cacheExpiry);
    }
}
