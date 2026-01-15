package com.churncheck.api.infra.clients;

import com.churncheck.api.infra.clients.dto.AgentRequest;
import com.churncheck.api.infra.config.AgentProperties;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AgentClient {

    private static final Logger logger = Logger.getLogger(AgentClient.class.getName());

    private final RestClient restClient;
    private final AgentProperties properties;
    private final ObjectMapper objectMapper;

    public AgentClient(RestClient.Builder restClientBuilder, AgentProperties properties) {
        this.properties = properties;
        this.objectMapper = new ObjectMapper();

        this.restClient = restClientBuilder
                .baseUrl(properties.getEndpoint())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(createRequestFactory())
                .build();
    }

    private HttpComponentsClientHttpRequestFactory createRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(properties.getTimeout());
        factory.setReadTimeout(properties.getTimeout());
        return factory;
    }

    public String createConversation() {
        logger.info("Creating new conversation with agent: " + properties.getEndpoint());
        
        try {
            // El agente Python espera un mensaje para iniciar conversación
            var request = new AgentRequest("Hola", "");  // Mensaje inicial para crear conversación
            String jsonResponse = restClient.post()
                    .uri("/chat")  // El agente Python espera /chat para crear conversaciones
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Client error: %d - Failed to create conversation", statusCode);
                        logger.severe(message);
                        throw new AgentClientException(message, statusCode);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Server error: %d - Agent service unavailable", statusCode);
                        logger.severe(message);
                        throw new AgentServerException(message, statusCode);
                    })
                    .body(String.class);
            
            // Parsear la respuesta JSON para extraer conversation_id
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("conversation_id").asText();
        } catch (Exception e) {
            logger.severe("Failed to create conversation: " + e.getMessage());
            throw new AgentException("Failed to create conversation", e);
        }
    }

    public String sendMessage(String conversationId, String chatMessage) {
        logger.info("Sending message to conversation: " + conversationId);
        
        var request = new AgentRequest(chatMessage, conversationId);
        
        try {
            return restClient.post()
                    .uri("/chat")
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String errorMessage = String.format("Client error: %d - Failed to send message", statusCode);
                        logger.severe(errorMessage);
                        throw new AgentClientException(errorMessage, statusCode);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String errorMessage = String.format("Server error: %d - Agent service unavailable", statusCode);
                        logger.severe(errorMessage);
                        throw new AgentServerException(errorMessage, statusCode);
                    })
                    .body(String.class);
        } catch (Exception e) {
            logger.severe("Failed to send message: " + e.getMessage());
            throw new AgentException("Failed to send message", e);
        }
    }
}
