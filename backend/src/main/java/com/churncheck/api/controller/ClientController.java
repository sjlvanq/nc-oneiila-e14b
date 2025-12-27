package com.churncheck.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientResponseDTO;

@RestController
@RequestMapping("/clients")
public class ClientController {
    

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientCreateRequestDTO clientCreateRequestDTO) {
        Client client = clientRepository.save(new Client(clientCreateRequestDTO));
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(
            client.getId(),
            client.getName(),
            client.getEmail(),
            client.getActive(),
            client.getSubscriptionDate()
        );
        return ResponseEntity.ok(clientResponseDTO);
    }
}
