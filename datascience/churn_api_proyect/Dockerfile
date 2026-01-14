# Usamos una imagen ligera de Python
FROM python:3.9-slim

# Evita que Python genere archivos .pyc
ENV PYTHONDONTWRITEBYTECODE=1
# Salida de logs inmediata
ENV PYTHONUNBUFFERED=1

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los requerimientos primero (para aprovechar caché de Docker)
COPY requirements.txt .

# Instalamos dependencias
RUN pip install --no-cache-dir -r requirements.txt

# Copiamos el resto del código
COPY . .

# Exponemos el puerto
EXPOSE 8000

# Variable de entorno para indicarle a la app dónde están los modelos
# (Ajustado a la estructura de carpetas dentro del contenedor)
ENV MODEL_PATH=/app/model/churn_pipeline_prod.joblib
ENV METADATA_PATH=/app/model/metadata.json

# Comando para ejecutar la API
CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8000"]
