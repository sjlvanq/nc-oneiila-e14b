
![ORACLE + Alura Latam](../docs/assets/oraclealura-logo.svg)
## Hackathon Oracle Next Education II - Latam

***Desafío intensivo de innovación para participantes de todo Latam.***

powered by ![NoCountry](../docs/assets/nocountry-logo.png)

----

# 📘 Equipo Data Science - Churncheck

## 📂 Estructura del Proyecto `ml` (sugerencia)

```yml
ml/
├── data/
│   ├── raw/                # Datos originales sin procesar
│   ├── processed/          # Datos limpios/listos para modelado
│   └── external/           # Datos externos (ej. Kaggle, UCI, OpenML)
│
├── notebooks/
│   ├── exploration/        # Análisis exploratorio (EDA)
│   ├── preprocessing/      # Limpieza y transformación
│   └── modeling/           # Experimentos de modelos
│
├── src/
│   ├── preprocessing/      # Scripts de limpieza y feature engineering
│   ├── models/             # Entrenamiento y definición de modelos
│   ├── evaluation/         # Métricas y validación
│   └── utils/              # Funciones auxiliares (logging, config, etc.)
│
├── config/
│   ├── params.yaml         # Hiperparámetros de entrenamiento
│   └── paths.yaml          # Rutas de datasets y outputs
│
├── experiments/
│   ├── logs/               # Logs de entrenamiento
│   └── results/            # Métricas, gráficos, reportes
│
├── models/
│   ├── saved/              # Modelos entrenados (pickle, joblib, h5)
│   └── registry/           # Versionado de modelos para producción
│
├── deployment/
│   ├── oci_scripts/        # Scripts para OCI (ADS SDK, CLI, Terraform)
│   ├── docker/             # Dockerfiles para empaquetar el modelo
│   └── manifests/          # Configuración de despliegue (YAML, JSON)
│
├── docs/
│   ├── README.md           # Documentación principal
│   └── reports/            # Informes técnicos o ejecutivos
│
└── validation/
    ├── sanity_checks/      # Validaciones rápidas (tipos de datos, nulos)
    └── model_checks/       # Validación de métricas antes del deploy
```

---

## 🎯 Objetivo del Equipo DS

Construir, entrenar y validar modelos de **predicción de churn**, asegurando que estén listos para ser desplegados en **OCI Data Science** como servicios escalables y reproducibles.

---

## 🛠️ Flujo de Trabajo

1. **Ingesta de datos**  
   - Guardar datasets en `data/raw/`.  
   - Documentar fuentes en `docs/reports/`.

2. **Preprocesamiento**  
   - Notebooks en `notebooks/preprocessing/`.  
   - Scripts definitivos en `src/preprocessing/`.

3. **Modelado y evaluación**  
   - Experimentos en `notebooks/modeling/`.  
   - Métricas en `experiments/results/`.  
   - Validaciones en `validation/model_checks/`.

4. **Empaquetado y despliegue**  
   - Dockerfiles en `deployment/docker/`.  
   - Scripts OCI (ADS SDK, Terraform) en `deployment/oci_scripts/`.  
   - Configuración en `deployment/manifests/`.

5. **Versionado de modelos**  
   - Guardar modelos en `models/saved/`.  
   - Registrar versiones listas en `models/registry/`.

---

## 📑 Buenas Prácticas

- **Reproducibilidad**: usar `config/params.yaml` para hiperparámetros y `paths.yaml` para rutas.  
- **Validación previa al deploy**: correr scripts en `validation/` para asegurar consistencia y métricas mínimas.  
- **Documentación continua**: actualizar `docs/README.md` y reportes en cada sprint.  
- **Automatización**: preferir pipelines reproducibles (ej. `Makefile`, `bash`, `ADS pipelines`).  
- **Integración con backend**: coordinar endpoints y payloads JSON para predicciones.  

---
