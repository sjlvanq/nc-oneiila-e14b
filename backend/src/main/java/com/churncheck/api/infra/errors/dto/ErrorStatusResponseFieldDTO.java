package com.churncheck.api.infra.errors.dto;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public record ErrorStatusResponseFieldDTO(String field, String message) {
	public ErrorStatusResponseFieldDTO(ObjectError error) {
		this(
				(error instanceof FieldError fe) ? fe.getField() : error.getObjectName(),
						error.getDefaultMessage()); // error.getCode() returns the validation key
	}
}
