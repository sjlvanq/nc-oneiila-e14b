package com.churncheck.api.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.churncheck.api.infra.external.PredictionProperties;

@Configuration
@EnableConfigurationProperties(PredictionProperties.class)
public class PredictionConfig {
    // Configuration class for Prediction properties
}
