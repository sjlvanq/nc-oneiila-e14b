# Notas de implementación - Sprint 4

2026-01-20

Plan del Sprint 4: [enlace](sprint04.md)

## 1. Resumen de Implementación

Se ha completado el Sprint 4 centrándose en la **operatividad productiva** y la **optimización de recursos**. Los hitos principales incluyen la automatización del despliegue mediante CI/CD con GitHub Actions, la implementación de una caché de predicciones para reducir latencia y costos de API, y la normalización del flujo de consulta de clientes utilizando el **DNI** como identificador único, corrigiendo así las desviaciones técnicas de ciclos anteriores.

---

## 2. Análisis de cumplimiento por área

### Infraestructura y CI/CD (Dev A)

* **Pipeline de CI:** Se implementó `ci-pipeline.yml` con **Java 21**, incluyendo caché de dependencias Maven y generación de artefactos JAR.
* **Pipeline de CD:** Se configuró el flujo de despliegue automatizado hacia Docker Hub y el servidor remoto mediante SSH y **Docker Compose**.
* **Gestión de Secretos:** Integración de secretos de GitHub para la inyección dinámica de variables de entorno en el despliegue.

### Optimización y Persistencia (Dev B)

* **Caché de Predicciones:** Se extendió la entidad `Client` y se modificó `ChurnService` para almacenar resultados. El sistema ahora sirve datos locales si la última predicción tiene menos de 24 horas.
* **Lógica de Negocio:** Implementación de la validación de expiración de caché para balancear la frescura de los datos con el rendimiento del sistema.

### Refactorización y Calidad (Dev C)

* **Estandarización de Identificadores:** Se revirtió el uso de IDs numéricos en el flujo de predicción. Ahora el sistema utiliza el endpoint `GET /clients/prediction/{dni}` y el método `findByDni` del repositorio.
* **Refinado de Excepciones:** Se introdujo `EntityNotFoundException` para manejar búsquedas fallidas de DNI, mejorando los códigos de respuesta hacia el frontend.
* **Mantenimiento:** Normalización de archivos de configuración mediante `.editorconfig` (XML y SQL).

---

## 3. Análisis de Divergencias Técnicas

Se identifican las siguientes variaciones entre lo planificado y lo implementado físicamente en los flujos de trabajo:

* **Persistencia de Datos:** El plan especificaba H2 en "modo archivo" para persistencia. Sin embargo, el workflow actual configura la base de datos en **modo memoria** (`jdbc:h2:mem:`). Esto implica volatilidad de datos ante reinicios.
* **Gestión del Proceso:** Se sustituyó el uso de servicios nativos de Linux (`systemd`) por una arquitectura basada en **contenedores Docker**, mejorando la portabilidad pero modificando los requisitos del host.
* **Versión del SDK:** Se migró a **Java 21** (Temurin), a pesar de que el plan inicial estipulaba la versión 17.
* **Cobertura de Tests:** El pipeline de CI omite los tests de integración mediante el flag `-Dtest="!IntegrationTest"`, priorizando la velocidad de build sobre la validación completa.

---

## 4. Entregables Técnicos Completados

* **Workflows de GitHub:** Archivos `deploy.yml` y `ci.yml` operativos en la rama `backend/prod/v1.0`.
* **Esquema de Datos:** Campos de auditoría de predicción añadidos a la tabla `clients`.
* **Documentación:** `README.md` actualizado con instrucciones de ejecución por perfiles de Maven.
* **Suite de Pruebas:** `ClientServiceTest` actualizado para validar la lógica de caché y búsqueda por DNI.

---

## 5. Próximos Pasos Sugeridos

* **Hotfix de Persistencia:** Modificar el `docker-compose.yml` y el workflow de despliegue para montar un volumen persistente y cambiar la URL de conexión a modo archivo.
* **Reactivación de Tests:** Integrar los tests de integración en el pipeline de CI una vez estabilizado el entorno de base de datos.
