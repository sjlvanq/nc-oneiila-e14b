package com.churncheck.api.infra.clients.dto;

public record AgentRequest(
    String message,
    String conversation_id  // Mapeo exacto al agente Python
) {}
