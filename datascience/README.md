
# 🏛️ChurnCheck: Ecosistema de Data Science (2026)

> **Declaración de Autoridad:** Este repositorio constituye el núcleo analítico y predictivo del proyecto *ChurnCheck*. Ha sido reestructurado bajo estándares de ingeniería de datos de alto nivel para garantizar la integridad, trazabilidad y escalabilidad de los modelos destinados a la prevención de fuga de clientes.


## 🎯 Visión Estratégica
Nuestra misión es transformar datos brutos en inteligencia accionable. El equipo de Data Science opera bajo un marco de trabajo de **"Limpieza Radical y Verdad Única"**, donde cada notebook, script y modelo tiene una ubicación lógica e inamovible, eliminando la deuda técnica y los conflictos de estructura.

### Pilares del Módulo:
* **Gobierno de Datos**: Separación estricta entre estados de datos (Raw vs. Processed).
* **Producción-First**: Modelos empaquetados y documentados para una integración inmediata con Backend.

## 🎯 Objetivo Estratégico del Módulo DS

Ejecutar el ciclo de vida completo de ciencia de datos —desde la ingesta cruda hasta la validación de alto rigor— para el despliegue de modelos de predicción de churn. Nuestra prioridad es la entrega de artefactos analíticos escalables, reproducibles y listos para producción, garantizando una integración fluida con la arquitectura de backend y la infraestructura en la nube.


## 🛠️ Flujo de Trabajo (Estructura 2026)

### 1. Ingesta y Gestión de Datos
- **Raw Data**: Datasets originales en `data/raw/`.
- **Processed Data**: Versiones limpias en `data/processed/`.
- **Documentación**: Fuentes y diccionarios de datos en `reports/`.

### 2. Desarrollo y Experimentación
- **Preprocesamiento**: Notebooks de limpieza en `notebooks/preprocessing/`.
- **Análisis (EDA)**: Exploración en `notebooks/exploration/`.
- **Modelado**: Entrenamiento y validación en `notebooks/training/`.
- **Scripts**: Lógica definitiva en `src/`.

### 3. Versionado y Entrega
- **Modelos**: Archivos `.joblib` finales en `models/`.
- **Integración**: Definición de endpoints y payloads JSON en `deliverables_for_back/` para coordinación con el equipo de Backend.
- **Histórico**: El legado del proyecto se mantiene en `oldmess/`.

---

## 📑 Buenas Prácticas
- **Reproducibilidad**: Instalación de dependencias mediante `requirements.txt`.
- **Validación**: Métricas mínimas aseguradas antes de mover modelos a la carpeta `models/`.
- **Documentación Continua**: Actualización de reportes en cada sprint dentro de `reports/`.
- **Integración**: Comunicación constante de esquemas de datos con Backend para asegurar predicciones precisas.

---

## 🚀 Instalación
```bash
pip install -r requirements.txt