package com.churncheck.api.infra.clients;

public class AgentClientException extends AgentException {
    
    private final int statusCode;
    
    public AgentClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
