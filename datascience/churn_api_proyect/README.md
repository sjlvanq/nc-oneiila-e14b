# API ChurnInsight Gym

Una API basada en FastAPI para predicción de deserción de clientes en servicios de membresía de gimnasio.

## Descripción General del Proyecto

Esta API proporciona predicción en tiempo real de deserción de miembros de gimnasio utilizando un modelo de aprendizaje automático pre-entrenado. Aprovecha XGBoost con pipelines de scikit-learn para predicciones precisas e incluye capacidades de procesamiento por lotes.

## Características

- Endpoint de predicción única con umbral de decisión personalizable
- Procesamiento de predicciones por lotes
- Seguimiento de estadísticas en tiempo real
- Endpoint de metadatos del modelo
- Endpoint de verificación de salud
- Containerización con Docker para implementación fácil
- Coerción de tipos automática para características de entrada
- Marcas de tiempo ISO 8601 en todas las respuestas

## Estructura del Proyecto

```
churn_api_project/
├── src/
│   └── main.py              # Aplicación FastAPI
├── model/
│   ├── churn_pipeline_prod.joblib    # Modelo entrenado
│   └── metadata.json        # Metadatos del modelo
├── Dockerfile              # Configuración de Docker
├── requirements.txt        # Dependencias de Python
└── Readme.md              # Este archivo
```

## Requisitos

- Python 3.9+
- FastAPI 0.109.0
- Uvicorn 0.27.0
- scikit-learn 1.4.0
- XGBoost 2.0.3
- Pandas 2.2.0
- NumPy 1.26.3

Consulta `requirements.txt` para la lista completa de dependencias.

## Instalación

### Configuración Local

1. Clona o descarga el proyecto:
   ```bash
   cd churn_api_project
   ```

2. Instala las dependencias:
   ```bash
   pip install -r requirements.txt
   ```

3. Ejecuta la API:
   ```bash
   uvicorn src.main:app --reload --host 0.0.0.0 --port 8000
   ```

La API estará disponible en `http://localhost:8000`

### Configuración con Docker

1. Construye la imagen de Docker:
   ```bash
   docker build -t churn-api .
   ```

2. Ejecuta el contenedor:
   ```bash
   docker run -p 8000:8000 churn-api
   ```

## Documentación de API

### Verificación de Salud
- **Endpoint:** GET `/health`
- **Descripción:** Verifica que la API está en ejecución y el modelo está cargado
- **Respuesta:**
  ```json
  {
    "status": "ok",
    "model_loaded": true
  }
  ```

### Metadatos del Modelo
- **Endpoint:** GET `/metadata`
- **Descripción:** Obtiene la configuración del modelo e información de características
- **Respuesta:** Retorna el contenido de metadata.json

### Estadísticas
- **Endpoint:** GET `/stats`
- **Descripción:** Obtiene estadísticas de uso de la API
- **Respuesta:**
  ```json
  {
    "total_evaluated": 150,
    "total_pred_churn": 45,
    "last_request_at": "2026-01-14T12:30:00Z"
  }
  ```

### Predicción Única
- **Endpoint:** POST `/predict`
- **Content-Type:** application/json
- **Cuerpo de la Solicitud:**
  ```json
  {
    "data": {
      "avgClassFrequencyTotal": 8.5,
      "avgClassFrequencyCurrentMonth": 7.2,
      "lifetime": 12,
      "age": 35
    },
    "threshold": 0.5
  }
  ```
- **Respuesta:**
  ```json
  {
    "churn": 0,
    "probability": 0.3456,
    "timestamp": "2026-01-14T12:30:00Z"
  }
  ```

### Predicción por Lotes
- **Endpoint:** POST `/batch_predict`
- **Content-Type:** application/json
- **Cuerpo de la Solicitud:**
  ```json
  {
    "data": [
      {
        "avgClassFrequencyTotal": 8.5,
        "avgClassFrequencyCurrentMonth": 7.2,
        "lifetime": 12,
        "age": 35
      },
      {
        "avgClassFrequencyTotal": 3.2,
        "avgClassFrequencyCurrentMonth": 1.5,
        "lifetime": 6,
        "age": 28
      }
    ],
    "threshold": 0.5
  }
  ```
- **Respuesta:**
  ```json
  {
    "count": 2,
    "timestamp": "2026-01-14T12:30:00Z",
    "results": [
      {
        "churn": 0,
        "probability": 0.3456,
        "timestamp": "2026-01-14T12:30:00Z"
      },
      {
        "churn": 1,
        "probability": 0.7823,
        "timestamp": "2026-01-14T12:30:00Z"
      }
    ],
    "summary": {
      "mean_probability": 0.5640,
      "churn_count": 1
    }
  }
  ```

## Configuración

### Variables de Entorno

- `MODEL_PATH`: Ruta al archivo del modelo entrenado (por defecto: `./model/churn_pipeline_prod.joblib`)
- `METADATA_PATH`: Ruta al archivo de metadatos (por defecto: `./model/metadata.json`)
- `THRESHOLD`: Umbral de decisión predeterminado para predicciones (por defecto: `0.5`)

Establece las variables de entorno antes de ejecutar:
```bash
export MODEL_PATH=/app/model/churn_pipeline_prod.joblib
export METADATA_PATH=/app/model/metadata.json
export THRESHOLD=0.5
```

### Umbral Predeterminado

La API utiliza un umbral predeterminado de 0.5 para clasificación. Puedes:
- Establecer un umbral global predeterminado mediante la variable de entorno THRESHOLD
- Especificar un umbral óptimo en metadata.json
- Anular por solicitud mediante el parámetro threshold en el payload del endpoint

## Características del Modelo

El modelo procesa las siguientes características clave:

**Ingeniería de Características:**
- `attendance_trend`: Calculado a partir de la frecuencia del mes actual vs frecuencia total
- `visits_per_lifetime`: Métrica combinada de frecuencia y duración de membresía

**Características de Entrada:**
- avgClassFrequencyTotal
- avgClassFrequencyCurrentMonth
- lifetime
- age (y otros atributos del cliente)

## Ejemplos de Uso

### Cliente Python

```python
import requests

# Predicción única
response = requests.post(
    "http://localhost:8000/predict",
    json={
        "data": {
            "avgClassFrequencyTotal": 8.5,
            "avgClassFrequencyCurrentMonth": 7.2,
            "lifetime": 12,
            "age": 35
        },
        "threshold": 0.5
    }
)
print(response.json())

# Predicción por lotes
response = requests.post(
    "http://localhost:8000/batch_predict",
    json={
        "data": [
            {"avgClassFrequencyTotal": 8.5, "avgClassFrequencyCurrentMonth": 7.2, "lifetime": 12, "age": 35},
            {"avgClassFrequencyTotal": 3.2, "avgClassFrequencyCurrentMonth": 1.5, "lifetime": 6, "age": 28}
        ]
    }
)
print(response.json())
```

### cURL

```bash
# Predicción única
curl -X POST "http://localhost:8000/predict" \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "avgClassFrequencyTotal": 8.5,
      "avgClassFrequencyCurrentMonth": 7.2,
      "lifetime": 12,
      "age": 35
    }
  }'

# Verificación de salud
curl http://localhost:8000/health
```

## Manejo de Errores

- **400 Bad Request:** Faltan campos obligatorios en los datos de entrada
- **500 Internal Server Error:** Error en la inferencia del modelo o error inesperado

Las respuestas de error incluyen detalles sobre lo que salió mal:
```json
{
  "error": "Faltan campos",
  "missing": ["avgClassFrequencyTotal"]
}
```

## Consideraciones de Rendimiento

- Las predicciones únicas se procesan de forma sincrónica
- Las predicciones por lotes permiten el procesamiento eficiente de múltiples clientes
- El modelo se carga una sola vez al iniciar para un rendimiento óptimo
- Las estadísticas se rastrean en memoria durante el tiempo de ejecución

## Resolución de Problemas

### Modelo No Se Carga
- Verifica que las variables de entorno MODEL_PATH y METADATA_PATH sean correctas
- Comprueba que los archivos del modelo existan y sean legibles
- Asegúrate de que la versión de joblib coincida con la utilizada para serializar el modelo

### Errores de Coerción de Tipos
- Los valores de entrada se coercen automáticamente a tipos numéricos cuando es posible
- Los valores no válidos resultarán en NaN y pueden causar errores de predicción
- Consulta metadata.json para los tipos de características esperados

### Puerto Ya en Uso
- Cambia el puerto usando: `uvicorn src.main:app --port 8001`
- O modifica la instrucción CMD en el Dockerfile para implementaciones en contenedor

## Licencia

Este proyecto se proporciona tal cual para predicción de deserción de membresía de gimnasio.

## Soporte

Para problemas, asegúrate de:
1. Que todas las dependencias estén instaladas correctamente
2. Que los archivos del modelo estén en la ubicación correcta
3. Que los datos de entrada coincidan con el formato esperado de metadata.json
4. Que FastAPI y Uvicorn se ejecuten sin errores


