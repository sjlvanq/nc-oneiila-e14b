# AI Agent con Agent Framework y FastAPI

Agente básico implementado con Microsoft Agent Framework y FastAPI para integración con Azure AI Foundry.

## 🚀 Características

- ✅ **Agent Framework** - Microsoft Agent Framework con Azure AI
- ✅ **FastAPI** - API REST moderna y rápida
- ✅ **Azure AI Foundry** - Integración con modelos GPT-4
- ✅ **CORS** - Soporte para frontend
- ✅ **Logging** - Monitoreo completo
- ✅ **Health Check** - Endpoint de diagnóstico

## 📋 Prerrequisitos

1. **Python 3.11+**
2. **uv** - Package manager (ya instalado)
3. **Azure CLI** - Para autenticación
4. **Cuenta Azure** con acceso a Azure AI Foundry

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
cd /workspaces/nc-oneiila-e14b/agent-ai
```

### 2. Instalar dependencias

```bash
uv sync
```

### 3. Configurar variables de entorno

```bash
cp .env.example .env
# Editar .env con tus credenciales de Azure
```

### 4. Autenticarse con Azure CLI

```bash
az login
```

## 🏃‍♂️ Ejecución

### Modo Desarrollo

```bash
uv run python src/main.py
```

### Modo Producción

```bash
uv run uvicorn src.main:app --host 0.0.0.0 --port 8000
```

## 📡 Endpoints

### GET `/`

Endpoint raíz con información del servicio

**Response:**

```json
{
  "message": "AI Agent API",
  "version": "1.0.0",
  "status": "running",
  "endpoints": {
    "chat": "/chat",
    "conversations": "/conversations",
    "health": "/health"
  }
}
```

### GET `/health`

Health check del servicio

**Response:**

```json
{
  "status": "healthy",
  "azure_connected": true,
  "model": "gpt-4.1"
}
```

### POST `/chat`

Endpoint principal de chat

**Request:**

```json
{
  "message": "Hola, ¿cómo estás?",
  "conversation_id": "conv_123"
}
```

**Response:**

```json
{
  "response": "¡Hola! Estoy muy bien, gracias por preguntar. ¿En qué puedo ayudarte hoy?",
  "conversation_id": "conv_123",
  "timestamp": "1234567890.123"
}
```

### POST `/conversations`

Crear nueva conversación

**Response:**

```json
{
  "conversation_id": "conv_1234567890",
  "status": "created"
}
```

### GET `/conversations/{conversation_id}/status`

Obtener estado de conversación

**Response:**

```json
{
  "conversation_id": "conv_123",
  "status": "active",
  "messages": []
}
```

## 🔗 Integración con Java Backend

Para conectar con el backend Java existente:

### 1. Configurar el backend Java

```yaml
# application.yaml
azure:
  foundry:
    project:
      endpoint: http://localhost:8000  # Endpoint del agente Python
```

### 2. Ejemplo de llamada desde Java

```java
// Usar AzureFoundryClient para llamar al agente Python
String response = foundryClient.sendMessage(conversationId, message);
```

## 🛠️ Desarrollo

### Estructura del proyecto

```
agent-ai/
├── src/
│   └── main.py              # Aplicación principal
├── .env.example             # Plantilla de variables de entorno
├── pyproject.toml           # Configuración de uv
└── README.md               # Documentación
```

### Logs

La aplicación usa logging con nivel INFO. Los logs incluyen:

- Inicialización del cliente Azure AI
- Mensajes recibidos y respuestas
- Errores y excepciones
- Estado de conexiones

## 🔍 Monitoreo

### Health Check

```bash
curl http://localhost:8000/health
```

### Ver logs en tiempo real

```bash
uv run python src/main.py --log-level DEBUG
```

## 🚨 Troubleshooting

### Error de autenticación Azure

```bash
# Asegurarse de estar logueado
az login
# Verificar suscripción activa
az account show
```

### Error de conexión

- Verificar endpoint de Azure AI
- Revisar variables de entorno
- Comprobar firewall/network

### Error de modelo

- Verificar nombre del deployment
- Confirmar disponibilidad del modelo
- Revisar cuotas de Azure

## 📚 Referencias

- [Microsoft Agent Framework](https://learn.microsoft.com/en-us/agent-framework/)
- [Azure AI Foundry](https://learn.microsoft.com/en-us/azure/ai-foundry/)
- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)

## 🤝 Contribuciones

1. Fork del repositorio
2. Feature branch
3. Pull request

---

**Versión:** 1.0.0  
**Estado:** Beta - Funcional para pruebas

---

## 🧪 Manual Testing - Azure Foundry Agent

1. Verificar que el servidor está corriendo

```bash
curl http://localhost:8000/
```

2. Probar Health Check

```bash
curl http://localhost:8000/health
```

3. Probar el Chat con Azure Foundry

```bash
# Chat simple sin conversation_id
curl -X POST http://localhost:8000/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hola, ¿cómo estás?"}'

# Chat con conversation_id específico
curl -X POST http://localhost:8000/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "¿Recuerdas nuestra conversación anterior?", "conversation_id": "thread_abc123"}'

# Análisis de cliente específico
curl -X POST http://localhost:8000/analyze/CLIENTE123 \
  -H "Content-Type: application/json" \
  -d '{"message": "Analiza este cliente"}'
```

## 🔍 Qué esperar de las respuestas:

✅ Si funciona con Azure Foundry:

- Respuestas reales de GPT-4.1 con contexto inteligente
- Tiempo de respuesta de 2-5 segundos
- Logs mostrando "Azure AI Agent Client inicializado correctamente"
- Thread IDs reales del servicio (ej: `thread_abc123def456...`)

❌ Si hay errores:

- 500 Internal Server Error - Problemas de autenticación
- Timeout - Problemas de conexión
- Error messages en los logs

## 🧠 **Manejo de Threads (Cómo funciona):**

1. **Chat sin conversation_id**: 
   - Agent Framework crea nuevo thread automáticamente
   - Devuelve thread ID real en la respuesta
   - Siguiente llamada con mismo conversation_id usará ese thread

2. **Chat con conversation_id específico**:
   - Agent Framework usa el thread existente
   - Mantiene contexto completo
   - No crea nuevo thread

3. **Para continuar conversación**:
   - Usa el conversation_id devuelto en la respuesta anterior
   - Agent Framework encontrará el thread correcto automáticamente

---
