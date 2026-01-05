# Sprint 3

**Inicio:** 2026-01-05 | **Fin:** 2026-01-12

## Resumen

El objetivo de este sprint es elevar la madurez del proyecto mediante la implementación de un sistema de pruebas robusto, la estandarización de la comunicación de errores y la completitud de la documentación técnica. Se prioriza la estabilidad del sistema mediante el control total de flujos de error y la validación de contratos, transformando la aplicación en una herramienta robusta, predecible, autodescriptiva para otros equipos de desarrollo y confiable para el usuario final.

## Distribución de tareas


| Desarrollador | Área de Foco | Responsabilidades Principales |
| --- | --- | --- |
| **Dev A (Infra)** | **Testing Crítico** | Configurar `MockRestServiceServer` para el `PredictionClient` y asegurar que el sistema soporte fallos (timeouts/500) del servidor de IA. Implementar tests unitarios para la lógica de mapeo en `ChurnService`. El test de integración se limitará al flujo de predicción (sin incluir Login). |
| **Dev B (Lógica)** | **Gestión de Errores** | Crear el `GlobalExceptionHandler` y el `ErrorResponseDTO` estándar. Priorizar la captura automática de errores de validación (`MethodArgumentNotValidException`) para eliminar los `try-catch` de los controladores. |
| **Dev C (Doc)** | **Contratos y Datos** | Aplicar **Bean Validation** a los DTOs de entrada basados en el Diccionario de Datos. Configurar Swagger para que muestre correctamente los esquemas de los DTOs, los códigos de estado básicos (200, 400, 500) y el nuevo modelo de `ErrorResponseDTO`. |

### Tareas "Extras" (Ampliación de Alcance)

| Desarrollador | Tareas Extra |
| --- | --- |
| **Dev A (Infra)** | **Extensión del Test de Integración:** Ampliar el flujo de pruebas para que sea "end-to-end", incluyendo la autenticación (`AuthController / Login`) antes de la predicción. |
| **Dev B (Lógica)** | **Gestión de Errores de Seguridad:** Implementar un `AuthenticationEntryPoint` personalizado para que los fallos de JWT (token expirado o inválido) sigan el mismo formato que los errores de negocio. |
| **Dev C (Doc)** | **Enriquecimiento de Documentación:** Incluir ejemplos JSON específicos en Swagger y formalizar el Diccionario de Datos técnico como un documento o sección integral de la API (Bean Validation + Reglas presentadas en swagger-ui + Respuestas de error descriptivas). |

## Ejemplo de integración del diccionario de datos al código (DevC)

El **Dev C** actuará como validador y traductor técnico de los datos proporcionados por el equipo de Data Science hacia el código y la documentación oficial de la API.

```java
public record PredictionRequestDTO(
    @NotNull(message = "La edad es obligatoria")
    @Min(18) @Max(41) 
    Integer age,

    @NotNull
    @DecimalMin("0.15") @DecimalMax("552.33")
    Double avgAdditionalChargesTotal,

    @NotNull
    @Range(min = 0, max = 1)
    Integer gender
    // ... resto de campos
) {}
```

No olvidar el @Valid en los parámetros de los controladores


### Recomendación Técnica

Para que los entregables sean 100% efectivos, sugiero que el **Dev B** y el **Dev C** acuerden el formato del `ErrorResponseDTO` en la primera hora del Día 1. Un formato estándar recomendado sería:

```json
{
  "timestamp": "2026-01-05T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación",
  "path": "/predict",
  "details": {
    "age": "debe ser menor o igual a 41"
  }
}

```

## Entregables Técnicos

* **Paquete `infra.errors`:** Conteniendo el manejador global y el DTO de error estándar.
* **Suite de Tests:** 100% de cobertura en lógica de negocio (Services y Mappers) y validación de contratos de integración clave.
* **Swagger UI:** Documentación interactiva completa y funcional bajo el endpoint `/swagger-ui.html`.
* **Bean Validation:** DTOs de entrada (`LoginRequestDTO`, `PredictionRequestDTO`) blindados con `@NotNull`, `@Min`, `@Max` y `@NotBlank`.

---

## Análisis de Validez Técnica

* **Gestión de Errores (Dev B):** Actualmente, el `ClientController` utiliza bloques `try-catch` manuales en el endpoint de predicción que devuelven respuestas genéricas `badRequest()` sin detalles. La implementación de un `GlobalExceptionHandler` permitirá eliminar esta lógica dispersa y estandarizar la comunicación con el frontend.
* **Testing y Aislamiento (Dev A):** El código actual cuenta con un test de integración que depende de un "servidor real configurado ad-hoc". La tarea de configurar `MockRestServiceServer` es crítica para permitir pruebas herméticas que simulen fallos de red (timeouts) y errores 500 del servicio de IA sin depender de infraestructura externa.
* **Validación de Datos (Dev C):** Se observa que DTOs fundamentales como `ClientCreateRequestDTO` carecen actualmente de anotaciones de Bean Validation. La formalización del "Diccionario de Datos" mediante `@Min`, `@Max` y `@NotNull` blindará la entrada de datos antes de que lleguen a la capa de servicio.

---

Todo muy bien
