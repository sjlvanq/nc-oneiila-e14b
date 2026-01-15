package com.churncheck.api.service;

import com.churncheck.api.domain.service.ChatService;
import com.churncheck.api.infra.clients.AgentClient;
import com.churncheck.api.infra.clients.AgentException;
import com.churncheck.api.infra.clients.AgentClientException;
import com.churncheck.api.infra.clients.AgentServerException;
import com.churncheck.api.infra.errors.ChatServiceException;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    
    private final AgentClient agentClient;
    
    public ChatServiceImpl(AgentClient agentClient) {
        this.agentClient = agentClient;
    }
    
    @Override
    public String startConversation() {
        try {
            return agentClient.createConversation();
        } catch (AgentClientException e) {
            throw new ChatServiceException("Error al crear conversación: " + e.getMessage(), e.getStatusCode(), e);
        } catch (AgentServerException e) {
            throw new ChatServiceException("Servicio de agente no disponible: " + e.getMessage(), e.getStatusCode(), e);
        } catch (AgentException e) {
            throw new ChatServiceException("Error interno del servicio de chat: " + e.getMessage(), 500, e);
        }
    }
    
    @Override
    public String sendMessage(String conversationId, String message) {
        try {
            return agentClient.sendMessage(conversationId, message);
        } catch (AgentClientException e) {
            throw new ChatServiceException("Error al enviar mensaje: " + e.getMessage(), e.getStatusCode(), e);
        } catch (AgentServerException e) {
            throw new ChatServiceException("Servicio de agente no disponible: " + e.getMessage(), e.getStatusCode(), e);
        } catch (AgentException e) {
            throw new ChatServiceException("Error interno del servicio de chat: " + e.getMessage(), 500, e);
        }
    }
    
    @Override
    public String getConversationStatus(String conversationId) {
        return "active"; // Simplificado por ahora
    }
}
