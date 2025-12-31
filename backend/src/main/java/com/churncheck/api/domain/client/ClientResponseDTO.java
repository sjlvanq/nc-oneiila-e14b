package com.churncheck.api.domain.client;

public record ClientResponseDTO(
    Long id,
    String clientName,
    Boolean active,
    Gender gender,
    String clientPhone,
    Integer nearLocation,
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
            client.getAge());
    }
}
