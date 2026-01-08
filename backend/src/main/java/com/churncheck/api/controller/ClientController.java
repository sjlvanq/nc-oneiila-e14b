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

import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.service.ClientService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Clients",
    description = "Client management and churn prediction endpoints"
)
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }


   @Operation(summary = "Create client")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Client created successfully"),
    @ApiResponse(responseCode = "400", description = "Validation error")
})
    @PostMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateRequestDTO clientCreateRequestDTO, UriComponentsBuilder uriBuilder ) {
        ClientResponseDTO client = clientService.createFromDto(clientCreateRequestDTO);

        URI uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.id()).toUri();
        return ResponseEntity.created(uri).body(client);
    }

    
    @Operation(summary = "Update client")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Client updated successfully"),
    @ApiResponse(responseCode = "400", description = "Validation error")
})

    
    @PutMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientUpdateRequestDTO clientUpdateRequestDTO) {
        ClientResponseDTO client = clientService.updateClient(clientUpdateRequestDTO);

        return ResponseEntity.ok(client);
    }

   @Operation(summary = "List active clients")
@ApiResponse(responseCode = "200", description = "Clients retrieved successfully")

    @GetMapping
    public ResponseEntity<Page<ClientListResponseDTO>> getClientList(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(clientService.findAllByActiveTrue(pageable));
    }
  @Operation(summary = "Get client by ID")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Client found"),
    @ApiResponse(responseCode = "404", description = "Client not found")
})


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

    @Operation(summary = "Get churn prediction for client")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Prediction generated"),
    @ApiResponse(responseCode = "400", description = "Invalid client or prediction error")
})
    
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
