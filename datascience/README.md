
![ORACLE + Alura Latam](../docs/assets/oraclealura-logo.svg)
## Hackathon Oracle Next Education II - Latam

***Desafío intensivo de innovación para participantes de todo Latam.***

powered by ![NoCountry](../docs/assets/nocountry-logo.png)

----

# ChurnCheck - Data Science
## Predicción de Cancelación de Clientes

* Proyecto ChurnInsight
* **Equipo: H12-25-L-Equipo 14-Data Science**

-----

# 🏛️Ecosistema de Data Science (2026)

> **Declaración de Autoridad:** Este repositorio constituye el núcleo analítico y predictivo del proyecto *ChurnCheck*. Ha sido reestructurado bajo estándares de ingeniería de datos de alto nivel para garantizar la integridad, trazabilidad y escalabilidad de los modelos destinados a la prevención de fuga de clientes.


## 🎯 Visión Estratégica
Nuestra misión es transformar datos brutos en inteligencia accionable. El equipo de Data Science opera bajo un marco de trabajo de **"Limpieza Radical y Verdad Única"**, donde cada notebook, script y modelo tiene una ubicación lógica e inamovible, eliminando la deuda técnica y los conflictos de estructura.

### Pilares del Módulo:
* **Gobierno de Datos**: Separación estricta entre estados de datos (Raw vs. Processed).
* **Producción-First**: Modelos empaquetados y documentados para una integración inmediata con Backend.

## 🎯 Objetivo Estratégico del Módulo DS

Ejecutar el ciclo de vida completo de ciencia de datos —desde la ingesta cruda hasta la validación de alto rigor— para el despliegue de modelos de predicción de churn. Nuestra prioridad es la entrega de artefactos analíticos escalables, reproducibles y listos para producción, garantizando una integración fluida con la arquitectura de backend y la infraestructura en la nube.


## 🛠️ Flujo de Trabajo

### 1. Ingesta y Gestión de Datos
- **Raw Data**: [Dataset original](data/raw/gym_churn_us.csv)
- **Processed Data**: [Versiones limpias](data/processed)
- **Documentación**: [Fuentes y diccionarios de datos](reports/)

### 2. Desarrollo y Experimentación
- **Preprocesamiento**: [Notebook de limpieza](notebooks/01.-%20gym_churn_ETL.ipynb)
- **Análisis (EDA)**: [Análisis de Exploración](notebooks/02.-%20gym_churn_EDA.ipynb)
-**Renombramiento de las columnas** [Estandarización de las columnas](notebooks/03.-rename_columns_camelcase.ipynb)
- **Modelado**: [Entrenamiento y validación](notebooks/04.-%20gym_churn_camelcase_train.ipynb)
- **Optimización**: [Evaluación de hiperparámetros y optimización](05.-%20Optimización_de_gym_churn_camelcase_train.ipynb)

### 3. Versionado y Entrega
- **Modelos**: [Archivos .joblib finales](models/)
- **Integración**: [Definición de endpoints y payloads JSON para coordinación con el equipo de Backend.](/deliverables_for_back)
- **Histórico**: [El legado del proyecto se mantiene](/oldmess).

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

```