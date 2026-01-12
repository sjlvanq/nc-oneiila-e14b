package com.churncheck.api.domain.client.dto;

import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUpdateRequestDTO(

    @NotNull
    @Min(1)
    Long id,

    @Size(min = 3, max = 100)
    String clientName,

    String clientPhone,

    Boolean nearLocation,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate
) {

    @AssertTrue(message = "Si se proporciona la fecha de nacimiento, la edad debe estar entre 18 y 41 años")
    public boolean isAgeValid() {
        if (birthDate == null) {
            return true; 
        }
        int edad = Period.between(birthDate, LocalDate.now()).getYears();
        return edad >= 18 && edad <= 41;
    }
}
