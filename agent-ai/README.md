# Churn Agent - AI con Azure Functions y MCP

Agente especializado en análisis de churn con acceso dinámico a datos de clientes mediante MCP Server.

## 🚀 Características

- **Azure Functions** - Microservicio serverless
- **Agent Framework** - Microsoft Agent Framework con Azure AI
- **MCP Server** - Acceso dinámico a datos de clientes
- **Datos en Tiempo Real** - Análisis basado en CSV actualizable
- **API REST** - Fácil integración con cualquier plataforma

## 🛠️ Instalación Rápida

```bash
# 1. Instalar dependencias
uv add pandas python-dotenv

# 2. Configurar variables de entorno
cp .env.local.example .env.local
# Editar con tus credenciales de Azure

# 3. Autenticarse
az login

# 4. Iniciar servicios
func start
```

## 🌐 Uso

### Health Check

```bash
curl http://localhost:7071/api/health
```

### Chat con Cliente

```bash
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "analiza cliente DNI-1012"}'
```

### Listar Clientes

```bash
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "muéstrame todos los clientes"}'
```

## 📊 Datos Disponibles

- **15 clientes** con análisis de riesgo
- **Factores dinámicos**: antigüedad, contrato, ubicación
- **Niveles de riesgo**: BAJO, MEDIO, ALTO
- **Recomendaciones** personalizadas

## 🏗️ Arquitectura

```
Cliente → Azure Function → Agente AI → MCP Server → CSV Datos
```

## 📚 Documentación Técnica

Para detalles técnicos avanzados, ver: **[README_FUNCTION.md](./doc/README_FUNCTION.md)**

---
