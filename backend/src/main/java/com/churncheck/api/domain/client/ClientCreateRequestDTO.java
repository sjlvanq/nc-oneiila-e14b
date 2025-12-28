package com.churncheck.api.domain.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

public record ClientCreateRequestDTO(
    @NotBlank
    String clientName,

    Date subscriptionDate,

    @Email
    @NotBlank
    String email,

    @NotBlank
    String gender,

    @NotBlank
    String phoneRegistered,

    String nearCity,

    String age

) {
    
}
