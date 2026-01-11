# Refactorización del Modelo de Datos (Extra)

**Fecha:** 10/01/2026 
**Estado:** Finalizado / Integrado en Core

> **Contexto:** Aunque el equipo de Data trabaja con un dataset estructurado de forma estática, el equipo de Backend ha decidido implementar un **Modelo de Datos Dinámico**. El objetivo es aportar realismo técnico a la simulación, transformando una base de datos plana en un sistema transaccional capaz de generar dinámicamente la información que el modelo de IA requiere.

### 1. Abstracción y Adaptación del Contrato

*  **Mantenimiento del Contrato:** Se mantiene la compatibilidad con el microservicio de IA, pero los datos enviados ya no son campos fijos, sino el resultado de procesar el historial real del cliente.
*  **Cálculo Dinámico de Variables:** Se eliminaron los campos estáticos `lifetime`, `monthToEndContract`, `avg_class_frequency_total` y `avg_class_frequency_current_month` de la tabla de clientes.
*  **Lógica en Mapper:** El `ClientPredictionMapper` ahora calcula estas variables en tiempo de ejecución basándose en la fecha de registro y el historial de asistencia.
*  **Tipado para IA:** Los flags de negocio se transforman de `Boolean` a `Byte` (0/1) justo antes del envío para cumplir con los requisitos del modelo.

### 2. Implementación de Granularidad Transaccional

*  **Sistema de Cargos Reales:** Sustitución del campo estático de gastos adicionales por una relación `@OneToMany` con la entidad `AdditionalCharge`.
*  **Análisis de Tendencias:** La lógica analítica en el Mapper asegura que la IA reciba tendencias actualizadas basadas en transacciones reales de cargos adicionales.

### 3. Modelo Dinámico de Asistencia y Actividades

*  **Registro de Check-ins:** Se implementó la tabla `attendance` para capturar cada visita individual del cliente, eliminando la dependencia de promedios manuales.
* **Cálculo de Frecuencia:**
* **Frecuencia Total:** Calculada como el total de visitas dividido por las semanas transcurridas desde el registro.
* **Frecuencia Mensual:** Calculada dinámicamente sobre el mes actual para detectar cambios recientes en el comportamiento.
* **Gestión de Actividades Grupales:** Se agregaron las entidades `group_activities` y `group_activity_attendance` para trackear la participación en clases específicas, permitiendo un análisis más profundo del engagement.

### 4. Categorización y Tipificación (ChargeType)

* **Normalización de Gastos:** Se utiliza la entidad `ChargeType` para clasificar cargos (ej. 'Massage Therapy', 'Dietary supplements').
* **Mejora Semántica:** Se renombró el campo `description` a `name` en la entidad `ChargeType` para mayor claridad en el dominio.
* **Desacoplamiento:** Cada cargo está vinculado a un tipo mediante una relación `@ManyToOne`, facilitando la auditoría y futuros reportes por categoría.

### 5. Escalabilidad de Negocio e Integridad

* **Entidad Partner:** Se profesionalizó la relación corporativa migrando de un flag numérico a una entidad relacional `Partner`.
* **Refactorización de Tipos de Datos:** Los flags de negocio (`near_location`, `promo_friends`, `group_visit`) ahora utilizan tipos `Boolean` reales en lugar de enteros, mejorando la integridad de los datos en la base de datos.

