package com.churncheck.api.infra.external;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;

import com.churncheck.api.domain.client.dto.prediction.PredictionRequestDTO;
import com.churncheck.api.domain.client.dto.prediction.PredictionResponseDTO;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

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
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(properties.connectionTimeout());
        factory.setReadTimeout(properties.readTimeout());
        return factory;
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
            return restClient.post()
                    .uri(properties.endpoint())
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Client error: %d - Failed to get prediction", statusCode);
                        logger.severe(message);
                        throw new ResponseStatusException(response.getStatusCode(), "Error de cliente en servicio ML");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, response) -> {
                        int statusCode = response.getStatusCode().value();
                        String message = String.format("Server error: %d - ML service unavailable", statusCode);
                        logger.severe(message);
                        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "El servicio de ML no está disponible");
                    })
                    .body(PredictionResponseDTO.class);
            
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "El servicio de ML respondió sin contenido");
            }
            return response;

        } catch (RestClientResponseException e) {
            // error con código HTTP desde el servicio ML -> propagar con mensaje claro
            HttpStatus mapped = HttpStatus.resolve(e.getStatusCode().value());
            if (mapped == null) mapped = HttpStatus.BAD_GATEWAY;

            if (mapped.is4xxClientError()) {
                throw new ResponseStatusException(mapped, "Error de cliente en servicio ML", e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "El servicio de ML respondió con error", e);
            }
        } catch (ResourceAccessException e) {
            // timeouts / host inaccesible
            logger.severe("No se pudo conectar al servicio de ML: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "No se pudo conectar al servicio de ML", e);
        } catch (RestClientException e) {
            logger.severe("Error de comunicación con servicio de ML: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error de comunicación con el servicio de predicción", e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error inesperado: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error de comunicación con el servicio de predicción", e);
        }
    }
}
