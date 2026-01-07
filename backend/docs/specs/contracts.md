# Especificación de contratos - Backend - ChurnCheck

* [Backend - README.md](../README.md)

-----

## Índice
* [Integración con modelo ML](#integración-con-modelo-ml)
* [Excepciones](#excepciones)

-----

## Integración con modelo ML

Request / Model Input: [PredictionRequestDTO.java](/backend/src/main/java/com/churncheck/api/domain/client/dto/prediction/PredictionRequestDTO.java)

```
{
    "gender": 1|0,
    "nearLocation": 1|0,
    "partner": 1|0,
    "promoFriends": 1|0,
    "phone": 1|0,
    "contractPeriod": 1|6|12,
    "groupVisits": 1|0,
    "age": 18-41,
    "avgAdditionalChargesTotal": 0.15-552.33,
    "monthToEndContract": 1-12,
    "lifetime": 0-31,
    "avgClassFrequencyTotal": 0.00-6.02,
    "avgClassFrequencyCurrentMonth": 0.00-6.15
}
```

Response / Model Output: [PredictionResponseDTO.java](/backend/src/main/java/com/churncheck/api/domain/client/dto/prediction/PredictionResponseDTO.java)


```
{
    "churn": 0|1,
    "probability": 0.00|1.00,    
    "timestamp": ISO 8601
}
```

---

## Excepciones

Response / Error Output: [ErrorStatusResponseDTO.java](/backend/src/main/java/com/churncheck/api/infra/errors/dto/ErrorStatusResponseDTO.java)

```json
{
    "timestamp": "ISO 8601",
    "status": "HTTP Status Code (e.g. 400, 404, 500)",
    "error": "ERROR_CODE_STRING",
    "message": "Descripción amigable del error",
    "fields": [
        {
            "field": "nombre_del_campo",
            "message": "motivo del error de validación"
        }
    ]
}
```

### Detalles de campos de error

[ErrorStatusResponseFieldDTO.java](/backend/src/main/java/com/churncheck/api/infra/errors/dto/ErrorStatusResponseFieldDTO.java)

* **timestamp**: Marca de tiempo exacta en que ocurrió el error.
* **status**: Código numérico de estado HTTP.
* **error**: Código interno de error definido en `ErrorStatusResponseCodes`.
* **message**: Mensaje detallado sobre la excepción.
* **fields**: Lista opcional de errores específicos por campo (utilizado principalmente en validaciones de formularios o DTOs de entrada).
