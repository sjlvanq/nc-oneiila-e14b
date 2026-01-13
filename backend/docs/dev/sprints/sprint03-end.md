# Notas de implementación - Sprint 3
2026-01-13

Plan del Sprint 3: [enlace](sprint03.md)

## 1. Resumen de Implementación

Se ha completado la fase de maduración del proyecto, centrada en la robustez del sistema y la estandarización de la comunicación. Se implementó un manejador global de excepciones, se blindaron los contratos de datos mediante validaciones y se estableció una base sólida de pruebas para la integración con el microservicio de IA.

---

## 2. Análisis de cumplimiento por área

### Testing Crítico (Dev A)
* **Mocking de Cliente:** Se implementó `PredictionClientTest` utilizando `MockRestServiceServer` para simular el comportamiento de `RestClient` sin depender de infraestructura externa.
* **Soporte de Fallos:** Se validaron escenarios de éxito, errores de cliente (4xx), errores de servidor (5xx) y excepciones de red como timeouts.
* **Aislamiento:** Las pruebas de integración se centraron exclusivamente en el flujo de predicción, garantizando la independencia del sistema de autenticación según lo planificado.

### Gestión de Errores (Dev B)
* **GlobalExceptionHandler:** Se centralizó la captura de excepciones (incluyendo `ResponseStatusException` y `MethodArgumentNotValidException`) para proporcionar respuestas estandarizadas mediante `ErrorResponseDTO`.
* **Simplificación de Controladores:** Se eliminaron los bloques `try-catch` manuales en `ClientController`, delegando la gestión de estados HTTP a la infraestructura global.
* **Propagación de Estados:** El `PredictionClient` ahora propaga correctamente los errores específicos del microservicio de IA, mejorando la trazabilidad de fallos externos.

### Contratos y Datos (Dev C)
* **Bean Validation:** Se aplicaron restricciones (`@NotBlank`, `@Min`, `@Max`, `@Email`) en `LoginRequestDTO`, `ClientCreateRequestDTO` y `PredictionRequestDTO` para asegurar la integridad de los datos de entrada.
* **Documentación:** Se actualizó la configuración de Swagger para describir correctamente los esquemas de los DTOs y los posibles códigos de error.

---

## 3. Observaciones y Divergencias Técnicas

* **Lógica de Mapeo:** Aunque se implementó la lógica compleja en `ClientPredictionMapper` (promedios de cargos y frecuencias), los tests unitarios específicos para estos cálculos no se detallan en la suite de `PredictionClientTest`, quedando como una observación para el mantenimiento futuro.
* **Uso de RestClient:** Se optó por la nueva interfaz `RestClient` de Spring 6.1, lo que facilitó la integración con `MockRestServiceServer` y simplificó el manejo de excepciones mediante `onStatus`.
* **Validaciones de Edad:** Se implementó una validación personalizada mediante `@AssertTrue` en los DTOs de cliente para garantizar que la edad se mantenga entre 18 y 41 años, alineándose con el diccionario de datos del modelo ML.