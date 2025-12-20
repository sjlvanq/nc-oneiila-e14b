# Arquitectura para Agentes de IA Generativa

## Visión General

```mermaid
graph TD
    A[Cliente] -->|Solicitudes HTTP| B[Backend Java]
    B -->|API REST| C[Servicio de Agentes Python]
    C -->|Consulta| D[Azure OpenAI]
    B -->|CRUD| E[Base de Datos H2]
```

**Nota**: Arquitectura simplificada para desarrollo inicial, sin componentes de escalabilidad avanzada.

## 1. Componentes Principales

### 1.1 Capa de Presentación

- **API REST** (Java Spring Boot)
  - Endpoint: `POST /api/ai/chat`
  - Autenticación: API Key
  - Formato: JSON

### 1.2 Servicio de Agentes (Python)

- **Framework**: Agent Framework MAF, FastAPI
- **Patrón**: Agent-based architecture

### 1.3 Base de Conocimiento

- **Almacenamiento**: Base de datos H2
- **Datos**:
  - Historial de interacciones
  - Perfiles de clientes

## 2. Agentes Propuestos

### 2.1 Churn Analyst Agent

- **Responsabilidad**: Analizar riesgo de churn
- **Entradas**: Datos de cliente, historial
- **Salidas**: Análisis predictivo, recomendaciones

### 2.2 Customer Service Agent

- **Responsabilidad**: Asistencia al cliente
- **Capacidades**:
  - Respuestas a preguntas frecuentes
  - Guía de solución de problemas
  - Escalamiento a agente humano

### 2.3 Data Enrichment Agent (poco relevante)

- **Responsabilidad**: Mejorar datos de entrada
- **Funciones**:
  - Enriquecimiento con fuentes externas
  - Normalización de datos
  - Validación de calidad

## 3. Orquestación de Agentes

### 3.1 Arquitectura de Orquestación

```mermaid
graph TB
    %% Node definitions
    Client["Client/Backend"]
    
    subgraph "Microsoft Agent Framework"
        O["Main Orchestrator"]
        
        subgraph "Agents"
            A["Churn Analyst Agent"]
            B["Customer Service Agent"]
            C["Data Enrichment Agent"]
        end
        
        subgraph "Services"
            D["Data Base H2"]
            E["Azure OpenAI"]
        end
    end

    %% Main connections
    Client <-->|Request/Response| O
    
    %% Orchestrator connections
    O -->|1. Delegate tasks| A
    O -->|2. Delegate tasks| B
    O -->|3. Request enrichment| C
    
    %% Knowledge Base interactions
    A <-->|Query/Update| D
    B <-->|Query/Update| D
    C -->|Enhance| D
    
    %% Azure OpenAI connections
    A -->|Query| E
    B -->|Query| E

    %% Styling
    classDef client fill:#e1f5fe,stroke:#01579b,color:#01579b
    classDef orchestrator fill:#e8f5e9,stroke:#2e7d32,color:#1b5e20
    classDef agent fill:#f3e5f5,stroke:#6a1b9a,color:#4a148c
    classDef service fill:#fff3e0,stroke:#e65100,color:#bf360c
    
    class Client client
    class O orchestrator
    class A,B,C agent
    class D,E service
```

### 3.2 Flujo de Trabajo

1. **Recepción de Solicitud**:
   - El orquestador recibe una solicitud del backend Java
   - Valida y parsea la solicitud

2. **Enrutamiento Inteligente**:
   - Determina qué agente(s) deben manejar la solicitud
   - Puede dividir tareas complejas en subtareas

3. **Ejecución Paralela**:
   - Los agentes trabajan de forma concurrente cuando es posible
   - El orquestador maneja las dependencias entre tareas

4. **Consolidación de Resultados**:
   - Agrega y formatea las respuestas
   - Aplica reglas de negocio adicionales
   - Devuelve una respuesta unificada

### 3.3 Ventajas de MAF

- **Gestión de Estado**: Mantiene el contexto entre llamadas
- **Patrones de Reintento**: Manejo automático de fallos
- **Observabilidad**: Monitoreo integrado de agentes
- **Extensibilidad**: Fácil adición de nuevos agentes

## 4. Flujo de Datos

```mermaid
sequenceDiagram
    participant C as Cliente
    participant G as API Gateway
    participant J as Backend Java
    participant A as Agente IA
    participant D as Base de Datos
    
    C->>G: POST /api/ai/chat
    G->>A: Enruta solicitud
    A->>D: Consulta datos cliente
    D-->>A: Retorna datos
    A->>Azure OpenAI: Procesa consulta
    Azure OpenAI-->>A: Respuesta generada
    A-->>G: Respuesta estructurada
    G-->>C: Muestra resultados
```

## 4. Estructura del Repositorio

```yml
nc-oneiila-e14b/
├── backend-java/           # Backend principal (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/churninsight/
│   │   │   │   ├── config/        # Configuraciones
│   │   │   │   ├── controller/    # Controladores REST
│   │   │   │   ├── service/       # Lógica de negocio
│   │   │   │   ├── repository/    # Acceso a datos
│   │   │   │   └── model/         # Entidades y DTOs
│   │   │   └── resources/
│   │   └── test/                  # Pruebas Java
│   └── pom.xml
│
├── ai-agents/              # Servicio de Agentes IA (Python)
│   ├── app/
│   │   ├── __init__.py
│   │   ├── main.py               # Punto de entrada FastAPI
│   │   ├── config.py             # Configuraciones
│   │   ├── agents/               # Implementación de agentes
│   │   │   ├── __init__.py
│   │   │   ├── base_agent.py
│   │   │   ├── churn_analyst.py
│   │   │   └── service_agent.py
│   │   ├── api/                 # Endpoints de la API
│   │   └── models/              # Modelos Pydantic
│   ├── requirements.txt
│   └── tests/                   # Pruebas Python
│
└── doc/                      # Documentación
```

## 5. Requisitos Técnicos

### 5.1 Infraestructura

- Python 3.12+
- Microsoft Agent Framework (MAF)
- FastAPI
- Azure OpenAI Service
- Base de datos H2

### 5.2 Seguridad

- Autenticación por API Key
- Cifrado en tránsito (HTTPS)
- Validación de entrada/salida

## 6. Recursos Adicionales

- [Documentación Azure Foundry](https://learn.microsoft.com/en-us/azure/ai-foundry/what-is-azure-ai-foundry?view=foundry-classic)
- [Documentación Microsoft Agent Framework](https://learn.microsoft.com/en-us/agent-framework/overview/agent-framework-overview)
- [Guía FastAPI](https://fastapi.tiangolo.com/)
- [Patrones de diseño para IA](https://docs.microsoft.com/en-us/azure/architecture/patterns/)
