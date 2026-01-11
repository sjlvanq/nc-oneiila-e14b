package com.churncheck.api.infra.errors.dto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.churncheck.api.infra.errors.ErrorStatusResponseCodes;

public record ErrorStatusResponseDTO(
	Instant timestamp,
	Integer status,
	String desc,
	String message,
	List<ErrorStatusResponseFieldDTO> fields) {
    
    public ErrorStatusResponseDTO {
        timestamp = Instant.now();
        Objects.requireNonNull(status, "Código de estado del error no puede ser null");
        Objects.requireNonNull(desc, "Descripción del código de estado del error no puede ser null");
        fields = (fields == null) ? List.of() : List.copyOf(fields);
    }
    
	public ErrorStatusResponseDTO(ErrorStatusResponseCodes errorStatusResponseCode, String message){
		this(null, errorStatusResponseCode.getStatus(), errorStatusResponseCode.getDesc(), message, null);
	}

    public ErrorStatusResponseDTO(ErrorStatusResponseCodes errorStatusResponseCode, String message, List<ErrorStatusResponseFieldDTO> fields){
        this(null, errorStatusResponseCode.getStatus(), errorStatusResponseCode.getDesc(), message, fields);
    }
    
}