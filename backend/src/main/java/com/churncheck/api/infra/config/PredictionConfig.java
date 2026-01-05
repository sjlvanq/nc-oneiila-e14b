package com.churncheck.api.infra.config;

import com.churncheck.api.infra.clients.PredictionProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PredictionProperties.class)
public class PredictionConfig {
    // Configuration class for Prediction properties
}
