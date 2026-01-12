package com.churncheck.api.domain.client.dto;

import java.time.LocalDate;
import java.time.Period;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.Gender;

public record ClientResponseDTO(
    Long id,
    String clientName,
    Boolean active,
    Gender gender,
    String clientPhone,
    Boolean nearLocation,
    Integer age
) {
    public ClientResponseDTO(Client client) {
        this(
            client.getId(), 
            client.getClientName(), 
            client.getActive(), 
            client.getGender(),
            client.getClientPhone(),
            client.getNearLocation(),
            client.getAge() != null ? client.getAge() : 
                Period.between(client.getBirthDate(), LocalDate.now()).getYears()
        );
    }
}
