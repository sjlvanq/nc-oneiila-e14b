package com.churncheck.api.infra.clients;

public class PredictionException extends RuntimeException {
    
    public PredictionException(String message) {
        super(message);
    }
    
    public PredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}
