"""
Agente AI con Agent Framework y FastAPI
Integración con Azure AI Foundry para chat según el plan de implementación
"""

import os
import logging
import time
from typing import Optional, Dict, Any

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import uvicorn

# Agent Framework imports
from agent_framework import ChatAgent
from agent_framework.azure import AzureOpenAIAssistantsClient
from azure.identity import AzureCliCredential

# Configuración de logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Configuración desde variables de entorno
AZURE_AI_PROJECT_ENDPOINT = os.getenv("AZURE_AI_PROJECT_ENDPOINT")
AZURE_AI_MODEL_DEPLOYMENT_NAME = os.getenv("AZURE_AI_MODEL_DEPLOYMENT_NAME")
AZURE_AI_API_KEY = os.getenv("AZURE_AI_API_KEY")
AZURE_OPENAI_ENDPOINT = os.getenv("AZURE_OPENAI_ENDPOINT")

# Pydantic models para la API
class ChatRequest(BaseModel):
    message: str
    conversation_id: Optional[str] = None

class ChatResponse(BaseModel):
    response: str
    conversation_id: str
    timestamp: str

# FastAPI app
app = FastAPI(
    title="ChurnCheck AI Agent API",
    description="Agente AI especializado en análisis de churn con Azure Foundry",
    version="1.0.0"
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000", "http://localhost:8080"],  # Frontend URLs
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Herramientas del agente
def get_client_data(client_id: str) -> dict:
    """Obtener información del cliente desde API Java backend"""
    # Simulación - en implementación real, llamar a la API Java
    return {
        "client_id": client_id,
        "name": "Cliente Ejemplo",
        "tenure": 24,
        "monthly_charges": 89.50,
        "contract_type": "month-to-month",
        "payment_method": "electronic_check"
    }

def analyze_churn_pattern(client_data: dict) -> str:
    """Analizar patrones de churn del cliente"""
    # Simulación de análisis
    risk_factors = []
    
    if client_data.get("tenure", 0) < 12:
        risk_factors.append("baja antigüedad (< 12 meses)")
    
    if client_data.get("monthly_charges", 0) > 100:
        risk_factors.append("cargos mensuales elevados")
    
    if client_data.get("contract_type") == "month-to-month":
        risk_factors.append("contrato mes a mes (alto riesgo)")
    
    if risk_factors:
        return f"Alto riesgo de churn: {', '.join(risk_factors)}"
    else:
        return "Bajo riesgo de churn: cliente estable"

# Agente especializado en churn
class ChurnAgent:
    def __init__(self):
        self.agent = None
        self._initialize_agent()
    
    def _initialize_agent(self):
        """Inicializar el agente con Agent Framework"""
        try:
            self.agent = ChatAgent(
                chat_client=AzureOpenAIAssistantsClient(credential=AzureCliCredential()),
                instructions="""
                Eres un agente especializado en análisis de churn de clientes.
                Tu objetivo es analizar el riesgo de que un cliente abandone el servicio.
                Usa las herramientas disponibles para obtener datos del cliente y analizar patrones.
                Responde en español de forma profesional y concisa.
                Proporciona recomendaciones específicas para reducir el churn.
                """,
                tools=[get_client_data, analyze_churn_pattern],
            )
            logger.info("Agente de churn inicializado correctamente")
        except Exception as e:
            logger.error(f"Error inicializando agente: {e}")
            raise
    
    async def analyze_client(self, client_id: str) -> ChatResponse:
        """Analizar churn de un cliente específico"""
        try:
            if not self.agent:
                self._initialize_agent()
            
            message = f"Analiza el riesgo de churn del cliente {client_id}"
            result = await self.agent.run(message)
            
            return ChatResponse(
                response=result.text,
                conversation_id="default",
                timestamp=str(time.time())
            )
            
        except Exception as e:
            logger.error(f"Error analizando cliente {client_id}: {e}")
            raise HTTPException(status_code=500, detail=str(e))
    
    async def chat(self, message: str, conversation_id: Optional[str] = None) -> ChatResponse:
        """Chat general con el agente"""
        try:
            if not self.agent:
                self._initialize_agent()
            
            # Manejo de threads persistente como en consola
            if conversation_id and conversation_id in active_threads:
                # Reutilizar thread existente
                thread = active_threads[conversation_id]
                logger.info(f"Continuando conversación con thread: {conversation_id}")
            elif conversation_id:
                # Intentar crear thread con ID específico
                try:
                    from agent_framework import AgentThread
                    thread = AgentThread(service_thread_id=conversation_id)
                    active_threads[conversation_id] = thread
                    logger.info(f"Creando nuevo thread con ID: {conversation_id}")
                except Exception as e:
                    logger.warning(f"No se pudo crear thread con ID {conversation_id}: {e}")
                    # Crear nuevo thread automáticamente
                    thread = self.agent.get_new_thread()
                    conversation_id = f"thread_{int(time.time())}"
                    active_threads[conversation_id] = thread
                    logger.info(f"Creando nuevo thread automático: {conversation_id}")
            else:
                # Crear nuevo thread automáticamente
                thread = self.agent.get_new_thread()
                conversation_id = f"thread_{int(time.time())}"
                active_threads[conversation_id] = thread
                logger.info(f"Creando nuevo thread automático: {conversation_id}")
            
            # Ejecutar agente con thread persistente
            result = await self.agent.run(message, thread=thread)
            
            return ChatResponse(
                response=result.text,
                conversation_id=conversation_id,
                timestamp=str(time.time())
            )
            
        except Exception as e:
            logger.error(f"Error en chat: {e}")
            raise HTTPException(status_code=500, detail=str(e))

# Instancia global del agente
churn_agent = ChurnAgent()

# Almacenamiento global de threads activos
active_threads: Dict[str, Any] = {}

@app.on_event("startup")
async def startup_event():
    """Evento de startup de la aplicación"""
    logger.info("Iniciando ChurnCheck AI Agent API...")
    logger.info(f"Endpoint: {AZURE_AI_PROJECT_ENDPOINT}")
    logger.info(f"Model: {AZURE_AI_MODEL_DEPLOYMENT_NAME}")
    
    # Probar conexión con Azure
    try:
        test_response = await churn_agent.chat("Test de conexión")
        logger.info("Conexión con Azure AI establecida correctamente")
        logger.info(f"Test response: {test_response.response[:100]}...")
    except Exception as e:
        logger.error(f"Error conectando con Azure AI: {e}")

@app.get("/")
async def root():
    """Endpoint raíz"""
    return {
        "message": "ChurnCheck AI Agent API",
        "version": "1.0.0",
        "status": "running",
        "endpoints": {
            "chat": "/chat",
            "analyze": "/analyze/{client_id}",
            "health": "/health"
        }
    }

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    try:
        test_response = await churn_agent.chat("Health check")
        return {
            "status": "healthy",
            "azure_connected": True,
            "model": AZURE_AI_MODEL_DEPLOYMENT_NAME,
            "agent_ready": True
        }
    except Exception as e:
        logger.error(f"Health check failed: {e}")
        return {
            "status": "unhealthy",
            "azure_connected": False,
            "error": str(e),
            "agent_ready": False
        }


@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """Endpoint principal de chat"""
    try:
        logger.info(f"Mensaje recibido: {request.message}")
        
        response = await churn_agent.chat(
            message=request.message,
            conversation_id=request.conversation_id
        )
        
        logger.info(f"Respuesta generada: {response.response[:100]}...")
        return response
        
    except Exception as e:
        logger.error(f"Error en chat: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/analyze/{client_id}")
async def analyze_client(client_id: str):
    """Analizar churn de un cliente específico"""
    try:
        logger.info(f"Analizando cliente: {client_id}")
        
        response = await churn_agent.analyze_client(client_id)
        
        logger.info(f"Análisis completado para cliente {client_id}")
        return response
        
    except Exception as e:
        logger.error(f"Error analizando cliente {client_id}: {e}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    # Ejecutar el servidor
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        log_level="info"
    )