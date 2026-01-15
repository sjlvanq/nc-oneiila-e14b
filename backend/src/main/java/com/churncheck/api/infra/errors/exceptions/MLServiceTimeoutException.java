package com.churncheck.api.infra.errors.exceptions;

public class MLServiceTimeoutException extends RuntimeException {
    public MLServiceTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
