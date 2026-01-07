package com.churncheck.api.service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.domain.client.DomainException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ChurnService churnService;

    public ClientService(ClientRepository clientRepository, ChurnService churnService) {
        this.clientRepository = clientRepository;
        this.churnService = churnService;
    }

    // Crear cliente desde DTO (alineado al gym churn dataset)
    public ClientResponseDTO createFromDto(ClientCreateRequestDTO dto) {
        // ✅ Creación delegada a la entidad
        Client client = Client.createFromDto(dto);
        
        // ✅ Validaciones adicionales del service (cross-entity)
        validateBusinessRules(client);
        
        // ✅ Persistencia delegada al repository
        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }
    
    // Validaciones de negocio que involucran múltiples entidades
    private void validateBusinessRules(Client client) {
        // Reglas que involucran múltiples entidades o recursos externos
        // Por ahora, validación simple - se puede expandir en el futuro
        if (client.getClientPhone() == null || client.getClientPhone().trim().isEmpty()) {
            throw new DomainException("Client phone cannot be empty");
        }
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
