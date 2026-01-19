package com.churncheck.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.churncheck.api.domain.client.dto.StatisticsDTO;
import com.churncheck.api.infra.errors.dto.ErrorStatusResponseDTO;
import com.churncheck.api.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Statistics",
        description = "Provides global business metrics and aggregated client data."
    )

@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
        //, @ApiResponse(responseCode = "403", description = "Access denied. Insufficient permissions", content = @Content(schema = @Schema(implementation = ErrorStatusResponseDTO.class)))
})

@RestController
@RequestMapping(value = "/api/stats", produces = "application/json")
public class StatisticsController {

    private final ClientService clientService;

    public StatisticsController(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @Operation(summary = "Get statistics")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully") })
    @GetMapping
    public ResponseEntity<StatisticsDTO> getGlobalStats() {
        StatisticsDTO stats = clientService.getGlobalStats();;
        return ResponseEntity.ok(stats);
    }
}
