package com.churncheck.api.controller;

import com.churncheck.api.domain.service.ChatService;
import com.churncheck.api.domain.service.dto.ChatRequestDTO;
import com.churncheck.api.domain.service.dto.ChatResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat API", description = "Chat con Agente Python")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @PostMapping("/conversations")
    @Operation(summary = "Crear nueva conversación", description = "Crea una nueva conversación con el agente de IA")
    public ResponseEntity<ChatResponseDTO> startConversation() {
        String conversationId = chatService.startConversation();
        return ResponseEntity.ok(new ChatResponseDTO(conversationId, null));
    }
    
    @PostMapping("/conversations/{conversationId}/messages")
    @Operation(summary = "Enviar mensaje", description = "Envía un mensaje a una conversación existente")
    public ResponseEntity<ChatResponseDTO> sendMessage(
            @PathVariable String conversationId,
            @RequestBody @Valid ChatRequestDTO request) {
        
        String response = chatService.sendMessage(conversationId, request.message());
        return ResponseEntity.ok(new ChatResponseDTO(conversationId, response));
    }
}
