# Refactorización del Modelo de Datos (Extra)

**Fecha:** 07/01/2026

**Estado:** Finalizado / Integrado en Core

> **Contexto:** Aunque el equipo de Data trabaja con un dataset estructurado de forma estática, el equipo de Backend ha decidido implementar un **Modelo de Datos Dinámico**. El objetivo es aportar realismo técnico a la simulación, transformando una base de datos plana en un sistema transaccional capaz de generar dinámicamente la información que el modelo de IA requiere.

### 1. Abstracción y Adaptación del Contrato

* **Mantenimiento del Contrato:** Se mantiene la compatibilidad con el microservicio de IA, pero los datos enviados ya no son campos fijos, sino el resultado de procesar el historial real del cliente.
* **Cálculo Dinámico de Variables Temporales:** Se eliminaron los campos estáticos `lifetime` y `monthToEndContract`. El Backend ahora los calcula dinámicamente en el `ClientPredictionMapper`.
* **Tipado para IA:** Para cumplir con el contrato de la IA, los flags de negocio se transforman de `Boolean` a `Byte` (0/1) justo antes del envío.

### 2. Implementación de Granularidad Transaccional

* **Sistema de Cargos Reales:** Sustitución del campo estático de sumatoria de promedios mensuales de gastos adicionales por una relación `@OneToMany` con la entidad `AdditionalCharge` y la lógica analítica correspondiente en el Mapper, asegurando que la IA reciba tendencias actualizadas del comportamiento del cliente.

### 3. Categorización y Tipificación (ChargeType)

* **Normalización de Gastos:** Se implementó la entidad `ChargeType` para clasificar la naturaleza de los cargos adicionales. El sistema inicia con categorías predefinidas como 'Massage Therapy' y 'Dietary supplements', permitiendo una expansión futura sin alterar el esquema principal.
* **Desacoplamiento de Servicios:** Cada `AdditionalCharge` está vinculado a un tipo de cargo mediante una relación `@ManyToOne`, permitiendo auditoría y reportes por categoría.

### 4. Escalabilidad de Negocio e Integridad

* **Entidad Partner:** Se profesionalizó la relación corporativa migrando de un flag numérico a una entidad relacional `Partner` vinculada por ID.
* **Refactorización de Tipos de Datos:** Los flags que anteriormente eran enteros (0/1) en la base de datos, como `near_location`, `promo_friends` y `group_visit`, se han migrado a tipos `Boolean` reales para mejorar la semántica del código y la integridad de los datos.
