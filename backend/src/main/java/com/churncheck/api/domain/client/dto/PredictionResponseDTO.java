package com.churncheck.api.domain.client.dto;

import java.time.Instant;

public record PredictionResponseDTO(
	    Integer churn,
	    Double probability,
	    Instant timestamp
) {}
