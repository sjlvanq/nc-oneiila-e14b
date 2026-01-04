# Notas de implementación - Sprint 2
2026-12-03

Plan del Sprint 2: [enlace](sprint02.md)

## 1. Resumen de Implementación

Se ha completado la integración con el microservicio externo de predicción de *churn*, estableciendo el flujo de comunicación desde la API de Spring Boot hacia el modelo de IA. Se han implementado las capas de infraestructura (`PredictionClient`), servicio (`ChurnService`, `ClientService`) y controladores (`ClientController`) según lo planificado. 

---

## 2. Análisis de Divergencias

Se identifican las siguientes variaciones entre la planificación original y la implementación final:

* **Identificador de búsqueda:** El plan de Sprint especificaba el uso del **DNI** para la recuperación de clientes y el endpoint `/clients/prediction/{dni}`. Sin embargo, el código implementa la búsqueda por **ID (Long)** y el endpoint resultante es `GET /clients/{id}/prediction`. 
* **Query en Repositorio:** No se implementó `findByDni(String dni)`. En su lugar, se utiliza `findById(Long id)`, aprovechando el método estándar de `JpaRepository`. 
* **Estructura de DTOs:** Se añadieron campos adicionales en `PredictionRequestDTO` (como `gender` como Integer, `hasPhone` y `hasGroupVisit`) que no estaban detallados explícitamente en el plan inicial pero son necesarios para el modelo de IA. 

---

## 3. Observaciones Técnicas

* **Seguridad:** Se implementó `SpringDocConfig` para habilitar la autorización mediante Bearer Token (JWT) en la documentación de Swagger. 
* **Pruebas:** Se incluye un test de integración (`PredictionClientTest`) que valida la conectividad con un servidor real bajo el perfil `integration`. 
* **Mapeo de Datos:** El método `mapToPredictionRequest` realiza conversiones de tipos (ej. `Boolean` a `Byte` y `Gender` a `Integer`) para asegurar la compatibilidad con el microservicio de Python/IA. 

---
