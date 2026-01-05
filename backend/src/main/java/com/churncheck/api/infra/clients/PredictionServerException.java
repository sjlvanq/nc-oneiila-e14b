package com.churncheck.api.infra.clients;

public class PredictionServerException extends PredictionException {
    
    private final int statusCode;
    
    public PredictionServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
