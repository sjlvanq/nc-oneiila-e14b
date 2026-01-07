package com.churncheck.api.infra.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.churncheck.api.infra.errors.dto.ErrorStatusResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleAccessDenied(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.UNAUTHORIZED_401, 
                        "Email o contraseña incorrecto"));
    }
}
