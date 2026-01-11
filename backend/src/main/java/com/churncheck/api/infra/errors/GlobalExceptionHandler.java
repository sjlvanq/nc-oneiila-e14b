package com.churncheck.api.infra.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.churncheck.api.infra.errors.dto.ErrorStatusResponseDTO;
import com.churncheck.api.infra.errors.dto.ErrorStatusResponseFieldDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.FORBIDDEN_403,
                        "Acceso denegado"));
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleAccessDenied(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.UNAUTHORIZED_401, 
                        "Email o contraseña incorrecto"));
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorStatusResponseDTO> handleNotValid(BindException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.BAD_REQUEST_400,
                        "No se ha podido procesar la solicitud",
                        ex.getAllErrors().stream().map(ErrorStatusResponseFieldDTO::new).toList()));
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.NOT_FOUND_404,
                        String.join(" ", "La entidad solicitada no existe")));
                        //ex.getMessage()));
    }
}
