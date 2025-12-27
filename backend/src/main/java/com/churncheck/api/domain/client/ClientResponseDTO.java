package com.churncheck.api.domain.client;

import java.util.Date;

public record ClientResponseDTO(
    Long id,
    String name,
    String email,
    Boolean active,
    Date subscriptionDate
) {

}
