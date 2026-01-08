# Documentación de desarrollo - Backend - ChurnCheck

* [Backend - README.md](../README.md)

-----

## Setup y pruebas

* [Consola H2](h2-console.md). Instrucciones de acceso a la consola de H2 in-memory database.
* [Swagger UI](swagger-ui.md). Instrucciones de acceso y autenticación JWT con Swagger UI.

-----

## Sprints

| Nro. Sprint | Plan | Resumen | Notas de implementación |
| --- | --- | --- | --- |
| 1 | [Plan-s01](sprints/sprint01.md) | Implementación de infraestructura de seguridad stateless mediante JWT, autenticación de usuarios desde la base de datos y validación de acceso en cada petición. | [Impl.](sprints/sprint01-end.md) |
| 2 | [Plan-s02](sprints/sprint02.md) / [Plan-s02-ext](sprints/sprint02-details.md) | Consulta de cliente en BD ~~por DNI~~, integración mediante RestClient con microservicio de predicción de Churn y orquestación en capa de servicio. | [Impl.](sprints/sprint02-end.md) |
| Extra | N/A | **Refactorización Core**: Migración de datos estáticos a transaccionales. | [Detalles](sprints/refactor-model-details.md) |
| 3 | [Plan-s03](sprints/sprint03.md) | Testing robusto, manejo global de errores y documentación técnica completa. | *En desarrollo* |

-----

* [README.md del proyecto](../../../README.md)
* [CONTRIBUTING.md](../../../docs/CONTRIBUTING.md)
