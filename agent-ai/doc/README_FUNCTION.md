# Churn Agent Azure Function

Microservicio de agente IA usando Azure Functions y Azure AI Agents con arquitectura modular y fallback inteligente.

## Arquitectura Modular

```yml
Frontend → Backend Java (Puente) → Azure Function (Agent) → Azure AI Foundry
         POST /chat              POST /api/chat              → Agent Remoto
                                                              ↓
                                                         Fallback Local
```

### Estructura del Proyecto

```yml
agent-ai/
├── 📁 src/                    # Desarrollo de agentes con Agent Framework
│   ├── agents/                # Definiciones de agentes locales
│   │   └── churn_agent.py     # Agente local con herramientas
│   ├── tools/                 # Herramientas personalizadas
│   │   └── churn_analyzer.py  # Análisis de datos de churn
│   ├── main.py               # Testing local interactivo
│   └── deploy.py             # Deploy a Azure Foundry
├── 🚀 function_app.py         # Azure Function (endpoints HTTP)
├── 🤖 churn_agent.py          # Agente síncrono (fallback + Azure AI Agents)
├── 📦 requirements.txt         # Dependencias Python
├── ⚙️ host.json               # Configuración Azure Functions
├── 🔧 local.settings.json     # Configuración local
└── 📖 README_FUNCTION.md      # Documentación
```

## Endpoints

### POST /api/chat

Endpoint principal compatible con el backend Java simplificado.

**Request:**

```json
{
  "message": "Hola, necesito analizar churn",
  "conversation_id": "conv_12345"
}
```

**Response:**

```json
{
  "response": "Respuesta del agente especializado en churn...",
  "conversation_id": "conv_12345",
  "timestamp": "1640995200.0"
}
```

### GET /api/health

Health check del servicio con estado del agente.

**Response:**

```json
{
  "status": "healthy",
  "service": "Churn Agent Function",
  "version": "1.0.0",
  "agent": {
    "agent_framework_available": true,
    "azure_configured": true,
    "model_deployment": "gpt-4o-mini",
    "agent_name": "ChurnAgent"
  }
}
```

## Desarrollo Local

### Prerrequisitos

- Python 3.11+
- Azure Functions Core Tools
- Azure CLI (para autenticación)
- uv (recomendado para gestión de dependencias)

### Instalación

```bash
# Instalar dependencias con uv
uv add azure-functions azure-ai-agents==1.1.0 azure-ai-projects==1.0.0 azure-identity agent-framework-azure-ai --pre

# O con pip
pip install -r requirements.txt

# Iniciar Azure Function localmente
func start
```

El servicio estará disponible en: http://localhost:7071

### Testing

```bash
# Test del endpoint chat
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hola, necesito analizar churn"}'

# Test health check con estado del agente
curl http://localhost:7071/api/health
```

### Desarrollo de Agentes Locales

```bash
# Testing interactivo del agente local con Agent Framework
uv run python src/main.py

# Deploy del agente local a Azure Foundry
uv run python src/deploy.py

# Testing directo del agente (fallback)
uv run python test_agent.py
```

### Testing del Agente Directamente

```python
# Test del agente síncrono
from churn_agent import ChurnAgent

agent = ChurnAgent()
response = agent.chat("Cuáles son los factores de churn?")
print(response)

# Verificar estado del agente
status = agent.get_status()
print(status)
```

## Deploy a Azure

```bash
# Login a Azure
az login

# Deploy
func azure functionapp publish ChurnAgentFunction
```

## Configuración en Azure

Variables de entorno requeridas en Azure Function App:

### Para Modo Azure AI Agents (Producción)
- `AZURE_AI_PROJECT_ENDPOINT`: Endpoint del proyecto Azure AI Foundry
- `AZURE_AI_AGENT_ID`: ID del agente desplegado en Azure Foundry

### Para Modo Agent Framework (Desarrollo)
- `AZURE_AI_PROJECT_ENDPOINT`: Endpoint del proyecto Azure AI Foundry
- `AZURE_AI_MODEL_DEPLOYMENT_NAME`: Nombre del modelo desplegado (ej: gpt-4o-mini)

## Características del Agente

### Arquitectura Híbrida

- **Azure AI Agents**: Conexión con agentes remotos en Azure Foundry
- **Agent Framework**: Desarrollo local con herramientas personalizadas
- **Fallback inteligente**: Funciona incluso sin Azure configurado
- **Instrucciones especializadas**: Enfoque en análisis de churn
- **Conversaciones stateful**: Manejo automático de conversation_ids

### Modos de Operación

#### 🚀 Modo Producción (Azure AI Agents)
- Agente persistente desplegado en Azure Foundry
- Herramientas personalizadas en la nube
- Escalabilidad automática
- Management centralizado

#### 🔧 Modo Desarrollo (Agent Framework)
- Desarrollo local con `src/agents/churn_agent.py`
- Herramientas personalizadas locales
- Testing interactivo con `src/main.py`
- Deploy a Azure con `src/deploy.py`

#### 🛡️ Modo Fallback
- Respuestas básicas especializadas en churn
- Funciona sin configuración Azure
- Ideal para desarrollo y testing

### Respuestas Especializadas

El agente puede manejar:

- Análisis de factores de riesgo de churn
- Solicitudes de ID de cliente para análisis específico
- Recomendaciones de retención personalizadas
- Explicación de patrones de abandono
- Consultas generales sobre churn
- Análisis con datos reales (cuando hay herramientas)

## Flujo de Desarrollo

### 1. Desarrollo Local
```bash
# Desarrollar agente con Agent Framework
uv run python src/main.py

# Probar herramientas personalizadas
uv run python test_agent.py
```

### 2. Deploy a Azure Foundry
```bash
# Desplegar agente persistente
uv run python src/deploy.py

# Obtener AGENT_ID y configurar en local.settings.json
```

### 3. Producción
```bash
# Iniciar Azure Function con agente remoto
func start

# Deploy a Azure Functions
func azure functionapp publish ChurnAgentFunction
```

## Próximos Pasos

1. **✅ Azure AI Agents**: Configurar endpoint y AGENT_ID para producción
2. **🔧 Herramientas Avanzadas**: Integrar con backend Java para datos reales de clientes
3. **📊 Analytics**: Implementar dashboards de métricas de churn
4. **🔄 Orquestación**: Múltiples agentes especializados (retención, análisis, predicción)
5. **📈 Monitoring**: Configurar Application Insights y alertas
6. **🧪 Testing**: Añadir tests unitarios y de integración
7. **🚀 CI/CD**: Pipeline automático de deploy

## Troubleshooting

### Azure AI Agents no disponible

Si el agente remoto no se inicializa correctamente:

1. Verificar `AZURE_AI_PROJECT_ENDPOINT` en local.settings.json
2. Configurar `AZURE_AI_AGENT_ID` después del deploy
3. Asegurar autenticación con `az login`
4. Revisar permisos en Azure Foundry (rol Azure AI User)

### Agent Framework no disponible

Si el agente local no funciona:

1. Instalar dependencias: `uv add agent-framework-azure-ai --pre`
2. Verificar configuración del modelo en `AZURE_AI_MODEL_DEPLOYMENT_NAME`
3. Revisar logs del Azure Function

### Fallback Mode

El agente funcionará en modo fallback incluso sin configuración Azure, proporcionando respuestas básicas especializadas en churn.

## Estado Actual

✅ **Azure Function**: Funcionando en http://localhost:7071  
✅ **Fallback Mode**: Respuestas especializadas en churn  
✅ **Agent Framework**: Estructura para desarrollo local  
✅ **Deploy Tools**: Scripts para Azure Foundry  
🔄 **Azure AI Agents**: Configurable para producción  

## Ejemplos de Uso

```bash
# Health check
curl http://localhost:7071/api/health

# Chat básico
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hola, necesito analizar churn"}'

# Chat con conversación
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "¿Cuáles son los factores de riesgo?", "conversation_id": "conv_123"}'
```
