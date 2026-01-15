package com.churncheck.api.infra.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "external.prediction")
@Validated
public record PredictionProperties(
    @NotBlank String host,
    @Min(1) @Max(65535) int port,
    @NotBlank String endpoint,
    @NotBlank String apiKey,
    @Min(1000) int connectTimeout,
    @Min(1000) int connectionTimeout,
    @Min(1000) int readTimeout,
    @Min(0) int maxRetries
) {
    
    public String getBaseUrl() {
        return String.format("%s:%d", host, port); // "mock.echoapi.com:443"
    }
}
