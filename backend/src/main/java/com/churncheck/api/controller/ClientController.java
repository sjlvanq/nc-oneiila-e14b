package com.churncheck.api.controller;

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

import com.churncheck.api.domain.client.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.ClientRepository;
import com.churncheck.api.domain.client.Client;
import org.springframework.web.bind.annotation.PutMapping;
import com.churncheck.api.domain.client.ClientUpdateRequestDTO;

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
    public void createClient(@RequestBody ClientCreateRequestDTO clientCreateRequestDTO) {
        clientRepository.save(new Client(clientCreateRequestDTO));
    }

    @PutMapping
    @Transactional
    public void updateClient(@RequestBody ClientUpdateRequestDTO clientUpdateRequestDTO) {
        var client = clientRepository.getReferenceById(clientUpdateRequestDTO.id());
        client.updateClientData(clientUpdateRequestDTO);
    }
    
    @GetMapping
    public Page<ClientListResponseDTO> getClientList(@PageableDefault(size = 10, sort = "clientName") Pageable pageable) {
        return clientRepository.findAllByActiveTrue(pageable).map(ClientListResponseDTO::new);
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        var client = clientRepository.getReferenceById(id);
        client.deleteClient();
        return ResponseEntity.noContent().build();
    }
}
