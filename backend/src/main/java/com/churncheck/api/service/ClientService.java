package com.churncheck.api.service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.domain.partner.Partner;
import com.churncheck.api.domain.partner.PartnerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final ChurnService churnService;

    public ClientService(
            ClientRepository clientRepository, 
            PartnerRepository partnerRepository,
            ChurnService churnService) {
        this.clientRepository = clientRepository;
        this.partnerRepository = partnerRepository;
        this.churnService = churnService;
    }

    // Crear cliente desde DTO (alineado al gym churn dataset)
    public ClientResponseDTO createFromDto(ClientCreateRequestDTO dto) {
        Partner partner = null;
        if (dto.partnerId() != null) {
            partner = partnerRepository.findById(dto.partnerId())                    
                .orElseThrow(() -> new EntityNotFoundException("Partner not found"));
        }
        Client client = Client.createFromDto(dto, partner);
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
    	Client client = clientRepository.findById(clientId).orElseThrow();
    	PredictionResponseDTO prediction = churnService.predict(client);

        return new ClientFullResponseDTO(client, prediction);
    }
}
