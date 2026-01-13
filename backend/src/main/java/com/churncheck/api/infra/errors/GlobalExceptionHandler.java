package com.churncheck.api.infra.errors;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.churncheck.api.infra.errors.dto.ErrorStatusResponseDTO;
import com.churncheck.api.infra.errors.dto.ErrorStatusResponseFieldDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
    
    @ExceptionHandler({ 
        AuthenticationException.class, 
        UsernameNotFoundException.class, 
        BadCredentialsException.class
    })
    public ResponseEntity<ErrorStatusResponseDTO> handleUnauthorized(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorStatusResponseDTO(
                    ErrorStatusResponseCodes.UNAUTHORIZED_401, 
                    "Credenciales inválidas o token no proporcionado"));
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.FORBIDDEN_403,
                        "No tienes los permisos necesarios para realizar esta acción"));
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorStatusResponseDTO> handleNotValid(BindException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.BAD_REQUEST_400,
                        "No se ha podido procesar la solicitud",
                        ex.getAllErrors().stream().map(ErrorStatusResponseFieldDTO::new).toList()));
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleNotValidArgumentType(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.BAD_PATHVARIABLE_400,
                        "Se han recibido parámetros de ruta inválidos en la solicitud",
                        List.of(new ErrorStatusResponseFieldDTO(
                                ex.getParameter().getParameterName(),
                                String.join(" ", "Parámetro", ex.getParameter().getParameterName(), "inválido")))));
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class) //JSON Struct
    public ResponseEntity<ErrorStatusResponseDTO> handleJsonNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.MALFORMED_400,
                        "Se ha recibido una solicitud con formato inválido o tipos de datos incorrectos"));
    }
       
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.NOT_FOUND_404,
                        String.join(" ", "La entidad solicitada no existe")));
                        //ex.getMessage()));
    }
    
    @ExceptionHandler(org.springframework.dao.InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleInvalidDataAccess(InvalidDataAccessApiUsageException ex) {
        String errorMessage = "Error en los parámetros de consulta: " + ex.getMostSpecificCause().getMessage();       
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.INVALID_QUERY_PARAMETER_400,
                        errorMessage));
    }
    
    // --- PredictionClient
       
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        ErrorStatusResponseCodes code;
        
        // Mapeo lógico basado en el status capturado
        if (ex.getStatusCode().equals(HttpStatus.BAD_GATEWAY)) {
            code = ErrorStatusResponseCodes.BAD_GATEWAY_502;
        } else if (ex.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
            code = ErrorStatusResponseCodes.SERVICE_UNAVAILABLE_503;
        } else if (ex.getStatusCode().equals(HttpStatus.GATEWAY_TIMEOUT)) {
            code = ErrorStatusResponseCodes.GATEWAY_TIMEOUT_504;
        } else {
            code = ErrorStatusResponseCodes.INTERNAL_SERVER_ERROR_500;
        }
        return ResponseEntity.status(ex.getStatusCode()).body(
                new ErrorStatusResponseDTO(code, ex.getReason()));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorStatusResponseFieldDTO> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ErrorStatusResponseFieldDTO(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.BAD_REQUEST_400,
                        "Validation error in the submitted data",
                        errors));
    }
    
    // --- Catch-all

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorStatusResponseDTO> handleAllUncaughtException(Exception ex) {
        logger.severe("Unknown error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorStatusResponseDTO(
                        ErrorStatusResponseCodes.INTERNAL_SERVER_ERROR_500,
                        "An unexpected internal server error occurred"));
    }
}
