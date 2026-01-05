package com.churncheck.api.infra.clients;

public class PredictionClientException extends PredictionException {
    
    private final int statusCode;
    
    public PredictionClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
