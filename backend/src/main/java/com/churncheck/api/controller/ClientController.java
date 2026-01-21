package com.churncheck.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.churncheck.api.domain.client.dto.ClientCreateRequestDTO;
import com.churncheck.api.domain.client.dto.ClientFullResponseDTO;
import com.churncheck.api.domain.client.dto.ClientListResponseDTO;
import com.churncheck.api.domain.client.dto.ClientResponseDTO;
import com.churncheck.api.domain.client.dto.ClientUpdateRequestDTO;
import com.churncheck.api.domain.client.dto.statistics.ClientStatisticsDTO;
import com.churncheck.api.infra.errors.dto.ErrorStatusResponseDTO;
import com.churncheck.api.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Clients",
    description = "Client management and churn prediction endpoints"
)

@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
        /*
        ,
        @ApiResponse(responseCode = "403", description = "Access denied. Insufficient permissions",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
        */
    })

@RestController
@RequestMapping(value = "/clients", produces = "application/json")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }


    @Operation(summary = "Create client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client created successfully",
            content = @Content(schema = @Schema(implementation = ClientResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
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
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Client not found",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @PutMapping
    @Transactional
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientUpdateRequestDTO clientUpdateRequestDTO) {
        ClientResponseDTO client = clientService.updateClient(clientUpdateRequestDTO);

        return ResponseEntity.ok(client);
    }

    @Operation(summary = "List active clients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ClientListResponseDTO>> getClientList(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(clientService.findAllByActiveTrue(pageable));
    }
    
    @Operation(summary = "Get client by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "400", description = "Invalid ID format provided",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Client not found with the given ID",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Delete client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Client deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found",
            content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
   
    @Operation(summary = "Get churn prediction for client by DNI")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Prediction generated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid DNI or prediction error",
        content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class))),
    @ApiResponse(responseCode = "404", description = "Client not found",
        content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @GetMapping("/prediction/{dni}")
    public ResponseEntity<ClientFullResponseDTO> getClientPredictionByDni(@PathVariable String dni){
        ClientFullResponseDTO prediction = clientService.predictChurnByDni(dni);
        return ResponseEntity.ok(prediction);
    }

    @Operation(summary = "Get statistics for client by ID")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Statistics generated successfully"),
    /*
    @ApiResponse(responseCode = "400", description = "Invalid ID",
        content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class))),
    */
    @ApiResponse(responseCode = "404", description = "Client not found",
        content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
    })
    @GetMapping("/clients/statistics/{id}")
    public ResponseEntity<ClientStatisticsDTO> getClientPredictionByDni(@PathVariable Long id){
        ClientStatisticsDTO statistics = clientService.getClientStatistics(id);
        return ResponseEntity.ok(statistics);
    }

}
