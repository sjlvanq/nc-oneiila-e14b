#!/bin/bash

# Script de despliegue para ChurnCheck API con H2

echo "🚀 Iniciando despliegue de ChurnCheck API..."

# Detener y eliminar contenedores existentes
echo "🛑 Deteniendo contenedores existentes..."
docker-compose down

# Construir y levantar los servicios
echo "🔨 Construyendo y levantando servicios..."
docker-compose up --build -d

# Esperar a que el servicio esté listo
echo "⏳ Esperando a que el servicio esté listo..."
sleep 30

# Verificar el estado
echo "📊 Verificando estado del despliegue..."
docker-compose ps

# Mostrar logs recientes
echo "📋 Mostrando logs recientes..."
docker-compose logs --tail=50 backend

echo "✅ Despliegue completado!"
echo "🌐 API disponible en: http://localhost:8080"
echo "🔧 H2 Console disponible en: http://localhost:8080/h2-console"
echo "📚 Swagger UI disponible en: http://localhost:8080/swagger-ui.html"