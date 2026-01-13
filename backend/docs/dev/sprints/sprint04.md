# Sprint 4

**Inicio:** 2026-01-13 | **Fin:** 2026-01-20

## Resumen

El objetivo de este sprint es consolidar la madurez operativa del sistema mediante la implementación de optimizaciones de rendimiento, análisis de datos y automatización del despliegue. Se priorizan tres áreas críticas: (1) **Caché de Predicciones** para reducir llamadas redundantes al microservicio de IA, (2) **Endpoints Analíticos** que expongan métricas de negocio calculadas desde la base de datos, y (3) **Despliegue Automatizado en OCI** mediante CI/CD con GitHub Actions, transformando la aplicación en un sistema productivo, escalable y observable.

---

## Distribución de tareas

| Desarrollador | Área de Foco | Responsabilidades Principales |
| --- | --- | --- |
| **Dev A (Infra)** | **Despliegue y CI/CD** | Configurar el entorno en la instancia OCI (Java 17, H2 en modo file). Implementar pipeline de GitHub Actions para compilación, pruebas y despliegue automático. Configurar variables de entorno y secrets. Documentar procedimientos de despliegue y rollback. |
| **Dev B (Lógica)** | **Caché de Predicciones** | Extender la entidad `Client` con campos `last_prediction_churn`, `last_prediction_probability` y `last_prediction_timestamp`. Modificar `ChurnService` para guardar y consultar caché antes de llamar al microservicio. Implementar lógica de invalidación por TTL (24 horas). |
| **Dev C (API)** | **Endpoints Analíticos** | Crear `StatisticsController` con endpoints para estadísticas globales y por cliente. Diseñar queries agregadas en repositorios nuevos (`AdditionalChargeRepository`, `AttendanceRepository`) y extender `ClientRepository`. Documentar en Swagger con ejemplos de respuesta. |

---

## Tareas "Extras" (Ampliación de Alcance)

| Desarrollador | Tareas Extra |
| --- | --- |
| **Dev A (Infra)** | **Monitoreo Básico:** Configurar logs estructurados (Logback) y habilitar endpoint `/actuator/health` de Spring Boot Actuator para health checks. Configurar script de backup automático de la base de datos H2. |
| **Dev B (Lógica)** | **Invalidación Manual de Caché:** Añadir endpoint `DELETE /clients/{id}/prediction-cache` para forzar recalculación de predicción. |
| **Dev C (API)** | **Exportación de Reportes:** Endpoint `GET /statistics/report` que devuelva un CSV con estadísticas generales para análisis externo. |

---

## Estadísticas Propuestas

### Estadísticas Globales (`GET /statistics/global`)

- Total de clientes activos
- Total de clientes con predicción de churn
- Tasa promedio de churn (%)
- Promedio de edad de clientes
- Distribución por género (conteo MALE/FEMALE)
- Ingresos totales por cargos adicionales (último mes)
- Promedio de visitas por cliente (último mes)
- Tasa de renovación de contratos (contratos que finalizan en <30 días)

### Estadísticas por Cliente (`GET /statistics/clients/{id}`)

- Probabilidad de churn (última predicción)
- Antigüedad en meses
- Frecuencia de visitas (total y mes actual)
- Gasto total en cargos adicionales
- Gasto promedio mensual
- Meses restantes de contrato
- Tendencia de asistencia (comparación últimos 3 meses: "increasing", "stable", "decreasing")

---

## Nuevos archivos de código
```
├── java
│   └── com
│       └── churncheck
│           └── api
│               ├── controller
│               │   └── StatisticsController.java          [+ devC]
│               ├── domain
│               │   ├── attendance
│               │   │   └── AttendanceRepository.java      [+ devC]
│               │   ├── charge
│               │   │   └── AdditionalChargeRepository.java [+ devC]
│               │   └── client
│               │       ├── Client.java                    [* devB] -> Nuevos campos de caché
│               │       ├── ClientRepository.java          [* devC] -> Nuevas queries
│               │       └── dto
│               │           ├── GlobalStatisticsDTO.java   [+ devC]
│               │           ├── ClientStatisticsDTO.java   [+ devC]
│               │           └── AttendanceTrendDTO.java    [+ devC]
│               └── service
│                   ├── ChurnService.java                  [* devB] -> Lógica de caché
│                   └── StatisticsService.java             [+ devC]
│
├── resources
│   └── application-production.yaml                        [+ devA]
│
├── .github
│   └── workflows
│       └── deploy.yml                                     [+ devA]
│
└── scripts
    └── backup.sh                                          [+ devA]
```

---

## Dependencias a instalar en OCI

### Sistema Operativo (Ubuntu 22.04 LTS)
```bash
# Java 17
sudo apt install -y openjdk-17-jdk

# Nginx (reverse proxy)
sudo apt install -y nginx

# Git
sudo apt install -y git

# Herramientas adicionales
sudo apt install -y curl wget unzip
```

### Estructura de directorios
```bash
/opt/churncheck/            # Aplicación
/opt/churncheck/data/       # Base de datos H2
/var/log/churncheck/        # Logs
/opt/churncheck/backups/    # Backups automáticos
```

---

## Configuración de GitHub Secrets

Los siguientes secrets deben configurarse en el repositorio de GitHub:

- `OCI_SSH_KEY`: Clave privada SSH para acceso a la instancia
- `OCI_HOST`: IP pública de la instancia OCI
- `OCI_USER`: Usuario SSH (ej. `backend`)
- `ML_SERVICE_API_KEY`: API Key del servicio de ML (opcional)

---

## Recomendación Técnica

### Orden de Implementación Sugerido

**Día 1-3 (Dev B):** Implementar caché de predicciones. Esto desbloquea optimización inmediata y permite que Dev C trabaje con datos de predicción en cache.

**Día 2-4 (Dev C):** Diseñar y crear repositorios, queries y estadísticas. Puede trabajar en paralelo con Dev B.

**Día 3-6 (Dev A):** Preparar OCI, configurar CI/CD y realizar despliegue de prueba. Requiere coordinación con ambos devs para validar que el código desplegado funcione correctamente.

**Día 7:** Testing integrado en OCI, validación de estadísticas con datos reales y despliegue final a producción.

### Consideraciones de H2 en Producción

- **Modo File:** Usar `jdbc:h2:file:/opt/churncheck/data/churncheck;AUTO_SERVER=TRUE` para persistencia.
- **Backups:** Script cron que copia el archivo `.mv.db` diariamente a las 2 AM.
- **Consola H2:** Mantener deshabilitada en producción (`h2.console.enabled: false`).
- **Performance:** H2 es suficiente para cargas bajas/medias (< 100 requests/min).

### Consideraciones de Seguridad

- **Nginx:** Configurar rate limiting para prevenir DDoS.
- **SSH:** Usar solo autenticación por clave, deshabilitar password.
- **GitHub Secrets:** Rotar periódicamente `OCI_SSH_KEY`.
- **Firewall OCI:** Permitir solo puertos 22 (SSH), 80 (HTTP).

---

## Entregables Técnicos

* **Extensión de Entidad:** Campos `last_prediction_churn`, `last_prediction_probability`, `last_prediction_timestamp` en `Client.java`.
* **Servicio systemd:** Configuración `/etc/systemd/system/churncheck.service` para inicio automático.
* **Pipeline CI/CD:** Archivo `.github/workflows/deploy.yml` funcional con tests y despliegue automático.
* **Endpoints Analíticos:** `/statistics/global` y `/statistics/clients/{id}` documentados en Swagger.
* **Script de Backup:** `/opt/churncheck/backup.sh` con cron configurado para ejecución diaria.
* **Configuración de Producción:** `application-production.yaml` con H2 en modo file.

---

## Análisis de Validez Técnica

* **Caché de Predicciones (Dev B):** Reduce hasta 90% las llamadas al microservicio ML para clientes consultados frecuentemente. El TTL de 24 horas balancea frescura de datos con carga del sistema. La invalidación manual permite recálculo bajo demanda.

* **Estadísticas (Dev C):** Los endpoints analíticos permiten dashboards en frontend sin consultas pesadas repetidas. Las queries agregadas (`COUNT`, `AVG`, `SUM`) son eficientes en H2 y aprovechan índices existentes en `active` y `gender`.

* **Despliegue OCI (Dev A):** El pipeline de GitHub Actions elimina despliegues manuales propensos a errores humanos. El servicio systemd garantiza reinicio automático ante fallos. H2 en modo file mantiene persistencia entre reinicios sin necesidad de servidor de BD separado.

* **Ventajas de H2:** Simplicidad operativa (un solo archivo `.mv.db`), backups triviales (copiar archivo), sin costo adicional de infraestructura, compatible con perfiles de desarrollo (in-memory) y producción (file).

---

Todo listo para el sprint más operativo. Consulta `sprint04-details.md` para instrucciones detalladas de implementación.