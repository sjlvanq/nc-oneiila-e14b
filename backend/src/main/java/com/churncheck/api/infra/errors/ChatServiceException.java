package com.churncheck.api.infra.errors;

public class ChatServiceException extends RuntimeException {
    private final int statusCode;
    
    public ChatServiceException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public ChatServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
