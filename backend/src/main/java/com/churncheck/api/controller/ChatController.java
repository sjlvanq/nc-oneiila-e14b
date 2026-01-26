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
@Tag(name = "Chat API", description = "Chat with Agent IA")
public class ChatController {
    
    private final RestClient agentClient;
    
    public ChatController(RestClient.Builder restClientBuilder, AgentProperties properties) {
        this.agentClient = restClientBuilder
                .baseUrl(properties.getEndpoint())
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .build();
    }
    
    @PostMapping
    @Operation(summary = "Chat with Agent IA", description = "Send a message to the agent IA and get a response")
    public ResponseEntity<ChatResponseDTO> chat(@RequestBody @Valid ChatRequestDTO request) {
        try {
            String jsonBody = String.format("{\"message\": \"%s\", \"conversation_id\": %s}", 
                request.message(), 
                request.conversationId() != null ? "\"" + request.conversationId() + "\"" : "null");
            
            ChatResponseDTO response = agentClient.post()
                    .uri("/chat")
                    .body(jsonBody)
                    .retrieve()
                    .body(ChatResponseDTO.class);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw e;
        }
    }
}
