package com.churncheck.api.service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientListResponseDTO;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.ClientResponseDTO;
import com.churncheck.api.domain.client.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.PredictionResponseDTO;
import com.churncheck.api.domain.client.dto.ClientMapper;
import com.churncheck.api.infra.external.PredictionClient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PredictionClient predictionClient;

    public ClientService(ClientRepository clientRepository, PredictionClient predictionClient) {
        this.clientRepository = clientRepository;
        this.predictionClient = predictionClient;
    }

    // Crear cliente desde DTO (alineado al gym churn dataset)
    public ClientResponseDTO createFromDto(ClientCreateRequestDTO dto) {
        Client client = new Client();

        client.setClientName(dto.clientName());
        client.setActive(dto.active() != null ? dto.active() : true);
        client.setGender(dto.gender());
        client.setNearLocation(dto.nearLocation());
        client.setPartnerEmployee(dto.partnerEmployee());
        client.setPromoFriends(dto.promoFriends());
        client.setClientPhone(dto.clientPhone());
        client.setAge(dto.age());
        client.setContractPeriod(dto.contractPeriod());
        client.setMonthToEndContract(dto.monthToEndContract());
        client.setLifetimeMonths(dto.lifetimeMonths());
        client.setAvgClassFrequencyTotal(dto.avgClassFrequencyTotal());
        client.setAvgClassFrequencyCurrentMonth(dto.avgClassFrequencyCurrentMonth());

        // IMPORTANTE: churn NO se setea aquí (lo calcula el modelo)
        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }

    public ClientResponseDTO updateClient(ClientUpdateRequestDTO clientUpdateRequestDTO){
        Client client = clientRepository.getReferenceById(clientUpdateRequestDTO.id());
        client.updateClientData(clientUpdateRequestDTO);
        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }

    public Page<ClientListResponseDTO> findAllByActiveTrue(Pageable pageable){
        return clientRepository.findAllByActiveTrue(pageable).map(ClientListResponseDTO::new);
    }

    public ClientResponseDTO findById(Long id){
        Client client = clientRepository.getReferenceById(id);
        return new ClientResponseDTO(client);
    }

    public void deleteClient(Long id){
        Client client = clientRepository.getReferenceById(id);
        client.deleteClient();
        clientRepository.save(client);
    }

    public ClientFullResponseDTO predictChurn(Long clientId){

        Client client = clientRepository.getReferenceById(clientId);

        PredictionRequestDTO requestDTO = ClientMapper.toPredictionRequestDTO(client);

        PredictionResponseDTO prediction = predictionClient.predict(requestDTO);

        return new ClientFullResponseDTO(
            client.getId(),
            client.getClientName(),
            client.getClientPhone(),     
            prediction.prediction(),      
            prediction.probability(),      
            prediction.timestamp(),        
            prediction.modelVersion(),
            null     // processingTimeMs opcional
        );
    }
}
