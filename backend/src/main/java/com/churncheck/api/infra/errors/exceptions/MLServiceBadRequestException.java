package com.churncheck.api.infra.errors.exceptions;

public class MLServiceBadRequestException extends RuntimeException {
    public MLServiceBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
