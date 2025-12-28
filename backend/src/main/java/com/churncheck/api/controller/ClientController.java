package com.churncheck.api.controller;

import java.net.URI;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.ClientResponseDTO;
import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.ClientUpdateRequestDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.churncheck.api.domain.client.ClientListResponseDTO;




@RestController
@RequestMapping("/clients")
public class ClientController {
    

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateRequestDTO clientCreateRequestDTO, UriComponentsBuilder uriBuilder ) {
        Client client = clientRepository.save(new Client(clientCreateRequestDTO));

        URI uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientResponseDTO(client));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientUpdateRequestDTO clientUpdateRequestDTO) {
        var client = clientRepository.getReferenceById(clientUpdateRequestDTO.id());
        client.updateClientData(clientUpdateRequestDTO);

        return ResponseEntity.ok(new ClientResponseDTO(client));
    }
    
    @GetMapping
    public ResponseEntity<Page<ClientListResponseDTO>> getClientList(@PageableDefault(size = 10, sort = "clientName") Pageable pageable) {
        return ResponseEntity.ok(clientRepository.findAllByActiveTrue(pageable).map(ClientListResponseDTO::new));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        var client = clientRepository.getReferenceById(id);
        return ResponseEntity.ok(new ClientResponseDTO(client));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        var client = clientRepository.getReferenceById(id);
        client.deleteClient();
        return ResponseEntity.noContent().build();
    }
}
