import logging
import time
import os
from typing import Optional, Dict, Any

# Azure AI Agents imports (como el ejemplo)
from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient
from azure.ai.agents.models import ListSortOrder

# Configuración desde variables de entorno
AZURE_AI_PROJECT_ENDPOINT = os.getenv("AZURE_AI_PROJECT_ENDPOINT")
AZURE_AI_MODEL_DEPLOYMENT_NAME = os.getenv("AZURE_AI_MODEL_DEPLOYMENT_NAME")
AZURE_AI_AGENT_ID = os.getenv("AZURE_AI_AGENT_ID")  # ID del agente remoto

class ChurnAgent:
    """
    Agente de IA especializado en análisis de churn usando Azure AI Agents
    """
    
    def __init__(self):
        self.client = None
        self.agent = None
        self._initialize_client()
    
    def _initialize_client(self):
        """Inicializar el cliente de Azure AI"""
        try:
            if not AZURE_AI_PROJECT_ENDPOINT:
                logging.warning("AZURE_AI_PROJECT_ENDPOINT no configurado, usando fallback")
                return
            
            # Crear cliente como en el ejemplo
            credential = DefaultAzureCredential()
            self.client = AIProjectClient(
                endpoint=AZURE_AI_PROJECT_ENDPOINT,
                credential=credential,
            )
            
            # Si tenemos AGENT_ID, obtenemos el agente remoto
            if AZURE_AI_AGENT_ID:
                self.agent = self.client.agents.get_agent(AZURE_AI_AGENT_ID)
                logging.info(f"Agente remoto {AZURE_AI_AGENT_ID} cargado correctamente")
            else:
                logging.warning("AZURE_AI_AGENT_ID no configurado, usando fallback")
                
        except Exception as e:
            logging.error(f"Error inicializando cliente Azure AI: {e}")
            self.client = None
    
    def chat(self, message: str, conversation_id: Optional[str] = None) -> Dict[str, Any]:
        """
        Procesa un mensaje del usuario y retorna una respuesta del agente
        
        Args:
            message: Mensaje del usuario
            conversation_id: ID de conversación (opcional)
            
        Returns:
            Diccionario con response, conversation_id, y timestamp
        """
        try:
            if not self.client or not self.agent:
                logging.info("Usando fallback responses - Azure AI Agent no disponible")
                return self._fallback_response(message, conversation_id)
            
            # Usar el patrón del ejemplo (síncrono)
            agents = self.client.agents
            
            # Crear o recuperar thread
            if conversation_id:
                thread = agents.threads.get(conversation_id)
            else:
                thread = agents.threads.create()
                conversation_id = thread.id
            
            # Agregar mensaje del usuario
            agents.messages.create(
                thread_id=thread.id,
                role="user",
                content=message,
            )
            
            # Ejecutar el agente
            run = agents.runs.create_and_process(
                thread_id=thread.id,
                agent_id=self.agent.id,
            )
            
            if run.status == "failed":
                logging.error(f"Run falló: {run.last_error}")
                raise RuntimeError(f"Run failed: {run.last_error}")
            
            # Obtener respuesta del asistente
            messages = list(
                agents.messages.list(
                    thread_id=thread.id,
                    order=ListSortOrder.ASCENDING,
                )
            )
            
            reply_text = ""
            for message in reversed(messages):
                if message.role == "assistant" and getattr(message, "text_messages", None):
                    reply_text = message.text_messages[-1].text.value
                    break
            
            return {
                "response": reply_text,
                "conversation_id": conversation_id,
                "timestamp": str(time.time())
            }
            
        except Exception as e:
            logging.error(f"Error en chat con Agent Framework: {e}")
            # Fallback a respuestas básicas
            return self._fallback_response(message, conversation_id)
    
    def _fallback_response(self, message: str, conversation_id: Optional[str] = None) -> Dict[str, Any]:
        """
        Respuestas básicas cuando Agent Framework no está disponible
        """
        message_lower = message.lower()
        
        # Análisis de intenciones básicas
        if any(greeting in message_lower for greeting in ["hola", "buenos días", "buenas", "hi"]):
            response = "¡Hola! Soy el agente de análisis de churn. ¿En qué puedo ayudarte hoy?"
        
        elif "churn" in message_lower or "abandono" in message_lower:
            response = """Puedo analizar el riesgo de churn de clientes. Los principales factores incluyen:
            
1. **Antigüedad**: Clientes con < 12 meses tienen 2x más riesgo
2. **Contrato**: Mes a mes = 3x más riesgo vs anual
3. **Cargos**: > $100 mensuales aumenta riesgo 40%
4. **Uso**: Baja frecuencia indica desinterés

¿Qué cliente específico te gustaría que analice?"""
        
        elif any(client_word in message_lower for client_word in ["cliente", "customer", "usuario"]):
            response = "Para analizar un cliente, necesito su ID único. ¿Puedes proporcionarme el identificador del cliente que quieres evaluar?"
        
        elif any(factor_word in message_lower for factor_word in ["factor", "riesgo", "peligro", "causa"]):
            response = """Los factores de churn más críticos son:

🔴 **Alto Riesgo:**
- Antigüedad < 6 meses
- Contrato mes a mes
- Sin uso en últimos 30 días

🟡 **Riesgo Medio:**
- Antigüedad 6-12 meses
- Cargos > $100
- Pagos atrasados

🟢 **Bajo Riesgo:**
- > 12 meses de antigüedad
- Contrato anual
- Uso frecuente

¿Quieres analizar un caso específico?"""
        
        elif any(analyze_word in message_lower for analyze_word in ["analizar", "evaluar", "revisar", "check"]):
            response = "Claro, puedo realizar análisis de churn. Por favor, proporcioname el ID del cliente o dime qué aspecto específico te gustaría evaluar (factores de riesgo, tendencias, recomendaciones)."
        
        elif any(help_word in message_lower for help_word in ["ayuda", "help", "cómo", "qué puedes"]):
            response = """Puedo ayudarte con:

- Análisis de riesgo de churn por cliente
- Identificación de factores de riesgo
- Recomendaciones para retención
- Tendencias y patrones de abandono

¿Qué te gustaría analizar primero?"""
        
        else:
            response = f"""Entiendo tu consulta sobre: '{message}'. 

Como agente especializado en churn, puedo ayudarte a:
- Analizar riesgos de clientes específicos
- Identificar factores de abandono
- Proporcionar estrategias de retención

¿Hay algún cliente o aspecto específico que te gustaría evaluar?"""
        
        # Generar conversation_id si no existe
        if not conversation_id:
            conversation_id = f"conv_{int(time.time())}"
        
        return {
            "response": response,
            "conversation_id": conversation_id,
            "timestamp": str(time.time())
        }
    
    
    def get_status(self) -> Dict[str, Any]:
        """Retorna el estado del agente"""
        return {
            "agent_framework_available": self.client is not None,
            "azure_configured": bool(AZURE_AI_PROJECT_ENDPOINT),
            "model_deployment": AZURE_AI_MODEL_DEPLOYMENT_NAME,
            "agent_name": "ChurnAgent" if self.client else None,
            "agent_id": AZURE_AI_AGENT_ID
        }
