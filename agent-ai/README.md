# AI Agent con Azure Functions y Agent Framework

Agente especializado en análisis de churn implementado con Microsoft Agent Framework y Azure Functions para integración con Azure AI Foundry.

## 🚀 Características

- ✅ **Azure Functions** - Microservicio serverless escalable
- ✅ **Agent Framework** - Microsoft Agent Framework con Azure AI
- ✅ **Arquitectura Modular** - Agente separado de Azure Function
- ✅ **Azure AI Foundry** - Integración con modelos GPT-4
- ✅ **Fallback Inteligente** - Funciona sin Azure AI configurado
- ✅ **Health Check** - Endpoint de diagnóstico completo
- ✅ **Especializado en Churn** - Análisis de abandono de clientes

## 📋 Prerrequisitos

1. **Python 3.11+**
2. **Azure Functions Core Tools** - Para desarrollo local
3. **Azure CLI** - Para autenticación
4. **Cuenta Azure** con acceso a Azure AI Foundry

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
cd /workspaces/nc-oneiila-e14b/agent-ai
```

### 2. Instalar dependencias

```bash
pip install -r requirements.txt
```

### 3. Configurar variables de entorno

```bash
# Crear local.settings.json para desarrollo local
cp local.settings.json.example local.settings.json
# Editar con tus credenciales de Azure
```

### 4. Autenticarse con Azure CLI

```bash
az login
```

## 🏃‍♂️ Ejecución

### Modo Desarrollo (Local)

```bash
func start
```

### Modo Producción (Azure)

```bash
# Deploy a Azure Functions
func azure functionapp publish <function-app-name>
```

## 📡 Endpoints

### GET `/api/health`

Health check del servicio con estado del agente

**Response:**

```json
{
  "status": "healthy",
  "service": "Churn Agent Function",
  "version": "1.0.0",
  "agent": {
    "agent_framework_available": true,
    "azure_configured": true,
    "model_deployment": "gpt-4.1",
    "agent_name": "ChurnAgent"
  }
}
```

### POST `/api/chat`

Endpoint principal de chat especializado en churn

**Request:**

```json
{
  "message": "Cuáles son los factores de riesgo de churn?",
  "conversation_id": "conv_123"
}
```

**Response:**

```json
{
  "response": "Puedo analizar el riesgo de churn de clientes. Los principales factores incluyen:\n\n1. **Antigüedad**: Clientes con < 12 meses tienen 2x más riesgo\n2. **Contrato**: Mes a mes = 3x más riesgo vs anual\n3. **Cargos**: > $100 mensuales aumenta riesgo 40%\n4. **Uso**: Baja frecuencia indica desinterés\n\n¿Qué cliente específico te gustaría que analice?",
  "conversation_id": "conv_123",
  "timestamp": "1769280559.4716215"
}
```

## 🔗 Configuración del Endpoint

### Endpoint del Agente

El agente AI está disponible en el siguiente endpoint:

```
http://172.17.0.1:7071/api
```

### Endpoints Disponibles

- **POST /api/chat** - Chat con el agente especializado
- **GET /api/health** - Health check del servicio

### Ejemplo de Integración

Para integrar este agente con cualquier plataforma frontend o backend:

```javascript
// Ejemplo con JavaScript/TypeScript
const response = await fetch('http://172.17.0.1:7071/api/chat', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    message: "Cuáles son los factores de riesgo de churn?",
    conversation_id: "optional_conversation_id"
  })
});

const result = await response.json();
console.log(result.response);
```

```python
# Ejemplo con Python
import requests

response = requests.post('http://172.17.0.1:7071/api/chat', json={
    "message": "Cuáles son los factores de riesgo de churn?",
    "conversation_id": "optional_conversation_id"
})

result = response.json()
print(result['response'])
```

## 🛠️ Desarrollo

### Estructura del proyecto

```
agent-ai/
├── 🚀 function_app.py         # Azure Function (endpoints HTTP)
├── 🤖 churn_agent.py          # Agente síncrono modular
├── 📦 requirements.txt         # Dependencias Python
├── ⚙️ host.json              # Configuración Azure Functions
├── 🔧 local.settings.json    # Configuración local
└── 📖 README.md              # Documentación
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
curl http://172.17.0.1:7071/api/health
```

### Ver logs en tiempo real

```bash
func start --verbose
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
- Revisar local.settings.json
- Comprobar firewall/network

### Error de modelo

- Verificar nombre del deployment
- Confirmar disponibilidad del modelo
- Revisar cuotas de Azure

## 📚 Referencias

- [Microsoft Agent Framework](https://learn.microsoft.com/en-us/agent-framework/)
- [Azure AI Foundry](https://learn.microsoft.com/en-us/azure/ai-foundry/)
- [Azure Functions](https://learn.microsoft.com/en-us/azure/azure-functions/)
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/)

## 🤝 Contribuciones

1. Fork del repositorio
2. Feature branch
3. Pull request

---

**Versión:** 1.0.0  
**Estado:** Production-Ready ✅

---

## 🧪 Manual Testing - Azure Function Agent

### 1. Verificar que la Azure Function está corriendo

```bash
curl http://172.17.0.1:7071/api/health
```

### 2. Probar Health Check

```bash
curl http://172.17.0.1:7071/api/health
```

### 3. Probar el Chat con Azure Functions

```bash
# Chat básico
curl -X POST http://172.17.0.1:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hola"}'

# Chat especializado en churn
curl -X POST http://172.17.0.1:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Cuáles son los factores de riesgo de churn?", "conversation_id": "test123"}'
```

### 4. Probar integración con cualquier cliente

```bash
# Directamente al agente
curl -X POST http://172.17.0.1:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hola"}'
```

## 🔍 Qué esperar de las respuestas:

✅ Si funciona con Azure Functions:

- Respuestas especializadas en análisis de churn
- Tiempo de respuesta de 2-5 segundos
- Logs mostrando inicialización del agente
- Fallback inteligente si Azure AI no está configurado

❌ Si hay errores:

- 500 Internal Server Error - Problemas de configuración
- Timeout - Problemas de conexión
- Error messages en los logs de Azure Functions

## 🧠 **Arquitectura Modular (Cómo funciona):**

1. **Azure Function (`function_app.py`)**:
   - Recibe requests HTTP de cualquier cliente
   - Parsea JSON y valida inputs
   - Llama al agente modular

2. **Agente Modular (`churn_agent.py`)**:
   - Inicializa Agent Framework con Azure AI
   - Maneja fallback inteligente sin Azure AI
   - Especializado en análisis de churn

3. **Cliente (Cualquier plataforma)**:
   - Envía requests HTTP al endpoint del agente
   - Recibe respuestas JSON estructuradas
   - Puede ser frontend, backend móvil, etc.

---
