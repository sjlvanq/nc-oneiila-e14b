package com.churncheck.api.domain.client.dto.prediction;

import java.time.Instant;

public record PredictionResponseDTO(
	    String churn,
	    Double probability,
	    Instant timestamp
) {}
