package com.churncheck.api.domain.client;

import java.util.Date;

public record ClientListResponseDTO(
    Long id,
    String clientName,
    String clientEmail,
    Boolean active,
    Date subscriptionDate,
    Gender gender,
    String clientPhone,
    Integer nearLocation,
    Integer age
) {
    public ClientListResponseDTO(Client client) {
        this(
            client.getId(), 
            client.getClientName(), 
            client.getClientEmail(),
            client.getActive(), 
            client.getSubscriptionDate(),
            client.getGender(),
            client.getClientPhone(), 
            client.getNearLocation(), 
            client.getAge());
    }
}
