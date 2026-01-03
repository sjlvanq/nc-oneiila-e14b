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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientResponseDTO;
import com.churncheck.api.domain.client.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.service.ClientService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.churncheck.api.domain.client.ClientListResponseDTO;




@RestController
@RequestMapping("/clients")
public class ClientController {
    

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateRequestDTO clientCreateRequestDTO, UriComponentsBuilder uriBuilder ) {
        ClientResponseDTO client = clientService.createFromDto(clientCreateRequestDTO);

        URI uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.id()).toUri();
        return ResponseEntity.created(uri).body(client);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientUpdateRequestDTO clientUpdateRequestDTO) {
        ClientResponseDTO client = clientService.updateClient(clientUpdateRequestDTO);

        return ResponseEntity.ok(client);
    }
    
    @GetMapping
    public ResponseEntity<Page<ClientListResponseDTO>> getClientList(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(clientService.findAllByActiveTrue(pageable));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // prediction endpoint

    @GetMapping("/{id}/prediction")
    public ResponseEntity<ClientFullResponseDTO> getClientPrediction(@PathVariable Long id){
        try {
            ClientFullResponseDTO prediction = clientService.predictChurn(id);
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
