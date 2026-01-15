package com.churncheck.api.infra.external;

import java.util.Set;
import java.util.logging.Logger;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import com.churncheck.api.infra.errors.exceptions.MLServiceBadRequestException;
import com.churncheck.api.infra.errors.exceptions.MLServiceTimeoutException;
import com.churncheck.api.infra.errors.exceptions.MLServiceUnavailableException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;


@Component
public class PredictionClient {

    private static final Logger logger = Logger.getLogger(PredictionClient.class.getName());

    private final RestClient restClient;
    private final PredictionProperties properties;
    private final Validator validator;

   public PredictionClient(RestClient.Builder restClientBuilder,
                        PredictionProperties properties,
                        Validator validator) {
    this.properties = properties;
    this.validator = validator;

        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    if (properties.apiKey() != null && !properties.apiKey().isEmpty()) {
                        headers.set("API-Key", properties.apiKey());
                    }
                })
                .requestFactory(createRequestFactory())
                .build();
    }
    
    private HttpComponentsClientHttpRequestFactory createRequestFactory() {
        @SuppressWarnings("deprecation")
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(properties.connectTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(properties.readTimeout()))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(properties.connectionTimeout())).build();

        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(new PoolingHttpClientConnectionManager()).disableAutomaticRetries().build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
    
    public RestClient getRestClient() {
        return restClient;
    }
    
    public PredictionResponseDTO predict(PredictionRequestDTO request) {

    Set<ConstraintViolation<PredictionRequestDTO>> violations =
            validator.validate(request);

    if (!violations.isEmpty()) {
        throw new ConstraintViolationException("Solicitud de predicción inválida", violations);
    }

    logger.info("Sending prediction request to ML service: " + properties.getBaseUrl());
    
    try {
        PredictionResponseDTO clientResponse = restClient.post()
                .uri(properties.endpoint())
                .body(request)
                .retrieve()
                .body(PredictionResponseDTO.class);

        if (clientResponse == null) {
            throw new MLServiceUnavailableException("El servicio de ML respondió sin contenido");
        }
        return clientResponse;

    } catch (RestClientResponseException e) {
        // Ahora SÍ se ejecutará este bloque
        int statusCode = e.getStatusCode().value();
        logger.severe(String.format("ML service error: %d - %s", statusCode, e.getMessage()));

        HttpStatus mapped = HttpStatus.resolve(statusCode);
        if (mapped == null)
            mapped = HttpStatus.BAD_GATEWAY;

        if (mapped.is4xxClientError()) {
            throw new MLServiceBadRequestException("Error de cliente en servicio ML", e);
        } else {
            throw new MLServiceUnavailableException("El servicio de ML respondió con error", e);
        }
    } catch (ResourceAccessException e) {
        logger.severe("ResourceAccessException caught - About to throw MLServiceTimeoutException");
        logger.severe("No se pudo conectar al servicio de ML: " + e.getMessage());
        MLServiceTimeoutException exception = new MLServiceTimeoutException("No se pudo conectar al servicio de ML", e);
        logger.severe("MLServiceTimeoutException created: " + exception.getClass().getName());
        throw exception;
    } catch (RestClientException e) {
        logger.severe("Error de comunicación con servicio de ML: " + e.getMessage());
        throw new MLServiceUnavailableException("Error de comunicación con el servicio de predicción", e);
    } catch (Exception e) {
        logger.severe("Error inesperado: " + e.getMessage());
        throw new MLServiceUnavailableException("Error de comunicación con el servicio de predicción", e);
    }
    }
}
