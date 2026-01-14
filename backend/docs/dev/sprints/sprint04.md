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
| **Dev C (API)** | **Endpoints Analíticos** | *En revisión*. |

---

## Tareas "Extras" (Ampliación de Alcance)

| Desarrollador | Tareas Extra |
| --- | --- |
| **Dev A (Infra)** | **Monitoreo Básico:** Configurar logs estructurados (Logback) y habilitar endpoint `/actuator/health` de Spring Boot Actuator para health checks. Configurar script de backup automático de la base de datos H2. |
| **Dev B (Lógica)** | **Invalidación Manual de Caché:** Añadir endpoint `DELETE /clients/{id}/prediction-cache` para forzar recalculación de predicción. |

---

## Nuevos archivos de código
```
├── java
│   └── com
│       └── churncheck
│           └── api
│               ├── domain
│               │   └── client
│               │       └── Client.java                    [* devB] -> Nuevos campos de caché
│               └── service
│                   └── ChurnService.java                  [* devB] -> Lógica de caché
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

* **Despliegue OCI (Dev A):** El pipeline de GitHub Actions elimina despliegues manuales propensos a errores humanos. El servicio systemd garantiza reinicio automático ante fallos. H2 en modo file mantiene persistencia entre reinicios sin necesidad de servidor de BD separado.

* **Ventajas de H2:** Simplicidad operativa (un solo archivo `.mv.db`), backups triviales (copiar archivo), sin costo adicional de infraestructura, compatible con perfiles de desarrollo (in-memory) y producción (file).

---

Consulta [sprint04-details.md](sprint04-details.md) para instrucciones detalladas de implementación.