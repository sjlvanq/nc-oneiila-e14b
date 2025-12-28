package com.churncheck.api.domain.client;

import java.util.Date;

public record ClientListResponseDTO(
    Long id,
    String clientName,
    String phoneRegistered,
    String nearCity,
    String email,
    Boolean active,
    Date subscriptionDate
) {
    public ClientListResponseDTO(Client client) {
        this(
            client.getId(), 
            client.getClientName(), 
            client.getPhoneRegistered(), 
            client.getNearCity(), 
            client.getEmail(), 
            client.getActive(), 
            client.getSubscriptionDate());
    }
}
