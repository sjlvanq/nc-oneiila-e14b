package com.churncheck.api.service;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Crear cliente desde DTO (alineado al gym churn dataset)
    public Client createFromDto(ClientCreateRequestDTO dto) {
        Client client = new Client();

        client.setClientName(dto.clientName());
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
        return clientRepository.save(client);
    }

    // Listar todos los clientes
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
