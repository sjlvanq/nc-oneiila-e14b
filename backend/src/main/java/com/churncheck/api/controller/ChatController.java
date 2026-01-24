package com.churncheck.api.controller;

import com.churncheck.api.domain.service.dto.ChatRequestDTO;
import com.churncheck.api.domain.service.dto.ChatResponseDTO;
import com.churncheck.api.infra.config.AgentProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat API", description = "Chat con Agente Python")
public class ChatController {
    
    private final RestClient agentClient;
    
    public ChatController(RestClient.Builder restClientBuilder, AgentProperties properties) {
        this.agentClient = restClientBuilder
                .baseUrl(properties.getEndpoint())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    @PostMapping
    @Operation(summary = "Chat con Agente IA", description = "Envía un mensaje al agente de IA y obtiene respuesta")
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody @Valid ChatRequestDTO request) {
        // Llamar directamente al agente Python
        ChatResponseDTO response = agentClient.post()
                .uri("/chat")
                .body(request)
                .retrieve()
                .body(ChatResponseDTO.class);
        
        return ResponseEntity.ok(response);
    }
}
