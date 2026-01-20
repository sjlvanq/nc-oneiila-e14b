package com.churncheck.api.domain.client.dto;

import java.time.LocalDate;
import java.time.Period;

import com.churncheck.api.domain.client.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record ClientCreateRequestDTO (

    @NotBlank
    @Size(min = 3, max = 100)
    String clientName,
    
    @NotBlank
    String dni,

    @NotNull
    Gender gender,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate,

    @NotBlank
    String clientPhone,

    @NotNull
    Boolean nearLocation,

    Long partnerId,

    @NotNull
    Boolean promoFriends,
    
    @NotNull
    @Min(1)
    @Max(12)
    Integer contractPeriod,

    @NotNull
    Boolean groupVisits,
    
    @NotNull
    Boolean active
) {
    @JsonIgnore
    @Schema(hidden = true)
    @AssertTrue(message = "La edad debe estar entre 18 y 41 años")
    public boolean isAgeValid() {
        if (birthDate == null) return false;
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= 18 && age <= 41;
    }    
}
