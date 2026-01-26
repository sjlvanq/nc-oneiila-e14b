# Churn Agent - Documentación Técnica Completa

Microservicio de agente IA usando Azure Functions, Agent Framework y MCP Server para análisis dinámico de churn.

## Arquitectura Modular

```yml
Frontend → Azure Function → Agente AI → MCP Server → CSV Datos
         POST /api/chat   (function_app) (churn_agent) (mcp_server)
```

### Estructura del Proyecto

```yml
agent-ai/
├── function_app.py         # Azure Function (endpoints HTTP)
├── churn_agent.py          # Agente síncrono con MCP integration
├── mcp_server.py          # MCP Server para datos dinámicos
├── tools/
│   ├── __init__.py
│   └── client_data.py     # Herramientas MCP para clientes
├── resources/
│   └── data.csv          # Datos de clientes (dinámicos)
├── requirements.txt       # Dependencias Python
├── host.json            # Configuración Azure Functions
├── local.settings.json  # Configuración local
├── .env.example          # Variables de entorno
└── doc/
    └── README_FUNCTION.md
```

## Endpoints

### POST /api/chat

Endpoint principal para chat con el agente especializado en churn.

**Request:**

```json
{
  "message": "analiza cliente DNI-1012",
  "conversation_id": "conv_12345"
}
```

**Response:**

```json
{
  "response": "Aquí tienes el análisis completo del cliente DNI-1012 (Isabella Romano):\n\n### Información del Cliente\n- **Nombre:** Isabella Romano\n- **Teléfono:** 555-1212\n- **Edad:** 36 años\n...\n\n### Análisis de Riesgo de Churn\n- **Nivel de Riesgo:** BAJO (0%)",
  "conversation_id": "conv_12345",
  "timestamp": "28083.541188198"
}
```

### GET /api/health

Health check del servicio con estado del agente y herramientas MCP.

**Response:**

```json
{
  "status": "healthy",
  "service": "Churn Agent Function",
  "version": "2.0.0",
  "agent": {
    "status": "ready",
    "agent_initialized": true,
    "tools_available": [
      "get_client_by_id",
      "analyze_churn_risk",
      "list_clients"
    ]
  }
}
```

## Desarrollo Local

### Prerrequisitos

- Python 3.11+
- Azure Functions Core Tools
- Azure CLI (para autenticación)
- uv (recomendado para gestión de dependencias)

### Instalación Completa

```bash
# 1. Instalar dependencias principales
uv pip install -r requirements.txt

# 2. Dependencias MCP
uv add pandas python-dotenv

# 3. Configurar variables de entorno
cp .env.example .env
# Editar con tus credenciales de Azure

# 4. Autenticarse con Azure
az login

# 5. Iniciar servicios
func start
```

### Testing Completo

```bash
# 1. Health check
curl http://localhost:7071/api/health

# 2. Chat con cliente específico
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "analiza cliente DNI-1012", "conversation_id": "test_1"}'

# 3. Listar todos los clientes
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "muéstrame todos los clientes", "conversation_id": "test_2"}'

# 4. Análisis de riesgo
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "analiza el riesgo de churn del cliente DNI-1002", "conversation_id": "test_3"}'

# 5. Preguntas generales
curl -X POST http://localhost:7071/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "cuáles son los factores de riesgo de churn?", "conversation_id": "test_4"}'
```

## MCP Server - Datos Dinámicos

### Herramientas MCP Disponibles

1. **`get_client_by_id(client_id)`**
   - Busca cliente por DNI en el CSV
   - Retorna información completa del cliente

2. **`analyze_churn_risk(client_id)`**
   - Analiza riesgo de churn basado en datos reales
   - Calcula score y proporciona recomendaciones

3. **`list_clients()`**
   - Lista todos los clientes con nivel de riesgo
   - Clasificación por colores (🟢 Bajo, 🟡 Medio, 🔴 Alto)

### Estructura del CSV

```csv
dni,name,phone,gender,birth_date,near_location,partner_id,promo_friends,registration_date,contract_start_date,contract_period,group_visit,active
DNI-1001,John Doe,555-0101,MALE,1994-01-01,1,1,1,2024-01-01,2024-01-01,24,1,1
DNI-1012,Isabella Romano,555-1212,FEMALE,1989-06-18,1,1,1,2024-02-28,2024-02-28,12,1,1
...
```

### Algoritmo de Riesgo

El riesgo de churn se calcula basado en:

- **Antigüedad** (35%): < 6 meses = alto riesgo
- **Tipo de contrato** (30%): mensual = alto riesgo
- **Ubicación** (15%): lejana = barrera de acceso
- **Visitas grupales** (10%): sin participación = bajo engagement
- **Partner** (5%): sin asignar = menos beneficios
- **Promoción amigos** (5%): sin referidos = bajo compromiso social

## Deploy a Azure

### Configuración en Azure

Variables de entorno requeridas en Azure Function App:

```bash
# Para Azure AI Foundry
AZURE_AI_PROJECT_ENDPOINT=https://<resource>.services.ai.azure.com/
AZURE_AI_MODEL_DEPLOYMENT_NAME=gpt-4.1
AZURE_OPENAI_ENDPOINT=https://<resource>.services.ai.azure.com

# Configuración del servidor
HOST=0.0.0.0
PORT=8000
LOG_LEVEL=info
```

### Deploy Commands

```bash
# Login a Azure
az login

# Deploy a Azure Functions
func azure functionapp publish ChurnAgentFunction

# Verificar deployment
curl https://<function-app>.azurewebsites.net/api/health
```

## Características Técnicas

### Arquitectura Híbrida

- **Azure Functions**: Serverless, escalable, HTTP endpoints
- **Agent Framework**: Microsoft Agent Framework con Azure AI
- **MCP Server**: Model Context Protocol para datos dinámicos
- **Fallback Inteligente**: Funciona sin Azure configurado
- **Datos en Tiempo Real**: CSV actualizable sin re-deploy

### Modos de Operación

#### Modo Producción (Azure AI + MCP)

- Agente con Azure AI Foundry
- MCP Server para datos dinámicos
- Escalabilidad automática
- Management centralizado

#### Modo Fallback

- Respuestas básicas especializadas en churn
- Funciona sin configuración Azure
- Ideal para desarrollo y testing

### Respuestas Especializadas

El agente puede manejar:

- **Análisis de clientes específicos** por DNI
- **Cálculo dinámico de riesgo** basado en datos reales
- **Recomendaciones personalizadas** por nivel de riesgo
- **Listado de clientes** con clasificación
- **Análisis de factores** de riesgo de churn
- **Consultas generales** sobre patrones de abandono

## Próximos Pasos

1. **Base de Datos Real**: Conectar a PostgreSQL/MySQL
2. **Más Herramientas MCP**: Integración con backend Java
3. **Analytics**: Dashboards de métricas de churn en tiempo real
4. **Orquestación**: Múltiples agentes especializados
5. **Monitoring**: Application Insights y alertas
6. **Testing**: Unit tests y tests de integración
7. **CI/CD**: Pipeline automático de deploy
8. **Security**: Autenticación y autorización

---
