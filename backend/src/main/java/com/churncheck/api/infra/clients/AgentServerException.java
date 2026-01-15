package com.churncheck.api.infra.clients;

public class AgentServerException extends AgentException {
    
    private final int statusCode;
    
    public AgentServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
