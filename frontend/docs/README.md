# Documentación de desarrollo - Frontend - ChurnCheck

* [Frontend - README.md](../README.md)

-----

<img src="assets/churncheck-frontend-dev.png" height="320" alt="Equipo Frontend en Acción">

-----

## Arquitectura y Guías Técnicas

* [Documentación de Consumo de API](dev/consumo-api.md)
* [Gestión de Autenticación (JWT)](dev/auth-jwt.md)
* [Backend - Referencia de API (Autogenerado)](../../backend/docs/specs/api-reference.md)

-----

## Setup y pruebas

### Ejecución del entorno de desarrollo

```
# Instalar las dependencias
npm install

# Iniciar el servidor
npm run dev

# Para acceso desde otros dispositivos en la red:
# npm run dev -- --host
```

-----

## Sprints

| Nro. Sprint | Plan | Resumen | Notas de implementación |
| --- | --- | --- | --- |
| 1 | [Plan-s01](dev/sprints/sprint01.md) | Cimientos y Seguridad: Unificación de búsqueda por DNI, integración de maqueta Iris, arquitectura de AuthContext (JWT) y pantalla de Login. | [Impl.](dev/sprints/sprint01-end.md) |
| 2 | [Plan-s02](dev/sprints/sprint02.md) | Funcionalidad Operativa y Dashboard: Implementación de métricas globales, buscador de predicción de Churn por DNI y sistema de navegación dinámica con lógica de acceso. | [Impl.](dev/sprints/sprint02-end.md) |
| 3 | [Plan-s03](dev/sprints/sprint03.md) | Analítica Detallada e Infraestructura: Despliegue multi-entorno, visualización de históricos (asistencia/gastos) mediante gráficos, motor de recomendaciones preventivas por DNI y optimización móvil. | [Impl.](dev/sprints/sprint03-end.md) |

-----

* [README.md del proyecto](../../README.md)
* [CONTRIBUTING.md](../../docs/CONTRIBUTING.md)
