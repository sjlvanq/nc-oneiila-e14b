package com.churncheck.api.domain.client;

import java.util.Date;

public record ClientListResponseDTO(
    Long id,
    String clientName,
    String email,
    Boolean active,
    Date subscriptionDate,
    Gender gender,
    String phoneRegistered,
    String nearCity,
    String age
) {
    public ClientListResponseDTO(Client client) {
        this(
            client.getId(), 
            client.getClientName(), 
            client.getEmail(),
            client.getActive(), 
            client.getSubscriptionDate(),
            client.getGender(),
            client.getPhoneRegistered(), 
            client.getNearCity(), 
            client.getAge());
    }
}
