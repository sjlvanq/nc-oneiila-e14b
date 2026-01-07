package com.churncheck.api.domain.client.dto;

import com.churncheck.api.domain.client.Client;
import com.churncheck.api.domain.client.Gender;

public record ClientListResponseDTO(
    Long id,
    String clientName,
    Boolean active,
    Gender gender,
    String clientPhone,
    Integer nearLocation,
    Integer age
) {
    public ClientListResponseDTO(Client client) {
        this(
            client.getId(), 
            client.getClientName(), 
            client.getActive(), 
            client.getGender(),
            client.getClientPhone(), 
            client.getNearLocation(), 
            client.getAge());
    }
}
