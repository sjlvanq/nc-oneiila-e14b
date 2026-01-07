package com.churncheck.api.infra.errors.dto;

import java.time.Instant;
import java.util.List;

import com.churncheck.api.infra.errors.ErrorStatusResponseCodes;

public record ErrorStatusResponseDTO(
	Instant timestamp,
	Integer status,
	ErrorStatusResponseCodes error,
	String message,
	List<ErrorStatusResponseFieldDTO> fields
) {
	public ErrorStatusResponseDTO(ErrorStatusResponseCodes errorStatusResponseCode, String message){
		this(
			Instant.now(), 
			errorStatusResponseCode.getStatus(), 
			errorStatusResponseCode,
			message, 
			List.of()
		);
	}
}