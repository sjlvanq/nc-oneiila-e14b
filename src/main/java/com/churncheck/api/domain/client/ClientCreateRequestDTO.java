package com.churncheck.api.domain.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

public record ClientCreateRequestDTO(
    @NotBlank
    String name,

    @Email
    @NotBlank
    String email,

    Date subscriptionDate
) {
    
}
