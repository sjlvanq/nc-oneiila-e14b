
# Copyright (c) Microsoft. All rights reserved.

import asyncio
from random import randint
from typing import Annotated, Dict, Any

from agent_framework.azure import AzureOpenAIAssistantsClient
from azure.identity import AzureCliCredential
from pydantic import Field

"""
Azure OpenAI Assistants Churn Analysis Agent

This sample demonstrates a churn analysis agent with automatic
assistant lifecycle management, showing both streaming and non-streaming responses.
"""


def get_client_data(
    client_id: Annotated[str, Field(description="The client ID to get data for.")],
) -> str:
    """Get client information for churn analysis."""
    # Simulación de datos del cliente
    clients = {
        "client_001": {
            "name": "María González",
            "tenure": 6,
            "monthly_charges": 89.50,
            "contract_type": "month-to-month",
            "payment_method": "electronic_check",
            "support_tickets": 3
        },
        "client_002": {
            "name": "Juan Pérez",
            "tenure": 24,
            "monthly_charges": 120.00,
            "contract_type": "one_year",
            "payment_method": "credit_card",
            "support_tickets": 1
        }
    }
    
    if client_id in clients:
        data = clients[client_id]
        return f"""Datos del cliente {client_id}:
- Nombre: {data['name']}
- Antigüedad: {data['tenure']} meses
- Cargos mensuales: ${data['monthly_charges']}
- Tipo de contrato: {data['contract_type']}
- Método de pago: {data['payment_method']}
- Tickets de soporte: {data['support_tickets']}"""
    else:
        return f"Cliente {client_id} no encontrado en la base de datos."


def analyze_churn_risk(
    client_data: Annotated[str, Field(description="Client data to analyze for churn risk.")],
) -> str:
    """Analyze churn risk based on client data."""
    # Simulación de análisis de riesgo
    risk_factors = []
    risk_score = 0
    
    # Análisis de factores de riesgo
    if "month-to-month" in client_data.lower():
        risk_factors.append("Contrato mes a mes (alto riesgo)")
        risk_score += 30
    
    if "tenure: " in client_data:
        tenure_line = [line for line in client_data.split('\n') if 'tenure:' in line.lower()]
        if tenure_line:
            tenure = int(tenure_line[0].split(':')[1].strip().split()[0])
            if tenure < 12:
                risk_factors.append(f"Baja antigüedad ({tenure} meses)")
                risk_score += 25
    
    if "support_tickets: " in client_data:
        tickets_line = [line for line in client_data.split('\n') if 'support_tickets:' in line.lower()]
        if tickets_line:
            tickets = int(tickets_line[0].split(':')[1].strip())
            if tickets > 2:
                risk_factors.append(f"Múltiples tickets de soporte ({tickets})")
                risk_score += 20
    
    if "monthly_charges: $" in client_data:
        charges_line = [line for line in client_data.split('\n') if 'monthly_charges:' in line.lower()]
        if charges_line:
            charges = float(charges_line[0].split('$')[1].strip())
            if charges > 100:
                risk_factors.append(f"Cargos mensuales elevados (${charges})")
                risk_score += 15
    
    # Determinar nivel de riesgo
    if risk_score >= 50:
        risk_level = "ALTO"
        recommendation = "Contactar inmediatamente con oferta de retención"
    elif risk_score >= 25:
        risk_level = "MEDIO"
        recommendation = "Monitorear y ofrecer beneficios adicionales"
    else:
        risk_level = "BAJO"
        recommendation = "Mantener servicio actual"
    
    return f"""Análisis de Riesgo de Churn:
- Nivel de Riesgo: {risk_level}
- Puntuación: {risk_score}/100
- Factores de Riesgo: {', '.join(risk_factors) if risk_factors else 'No se detectaron factores significativos'}
- Recomendación: {recommendation}"""


class ChurnAgent:
    """Churn Analysis Agent class for Azure Functions integration."""
    
    def __init__(self):
        self.agent = None
        self._initialize_agent()
    
    def _initialize_agent(self):
        """Initialize the agent with Azure OpenAI Assistants."""
        try:
            self.agent = AzureOpenAIAssistantsClient(credential=AzureCliCredential()).as_agent(
                instructions="""Eres un agente especializado en análisis de churn de clientes.
                Tu objetivo es analizar el riesgo de que un cliente abandone el servicio.
                Usa las herramientas disponibles para obtener datos del cliente y analizar patrones.
                Responde en español de forma profesional y concisa.
                Proporciona recomendaciones específicas para reducir el churn.""",
                tools=[get_client_data, analyze_churn_risk],
            )
        except Exception as e:
            print(f"Error initializing agent: {e}")
            raise
    
    async def chat(self, message: str, conversation_id: str = None) -> Dict[str, Any]:
        """
        Chat with the agent.
        
        Args:
            message: The user message
            conversation_id: Optional conversation ID for context
            
        Returns:
            Dictionary with response and conversation_id
        """
        try:
            if not self.agent:
                self._initialize_agent()
            
            # Run the agent
            result = await self.agent.run(message)
            
            return {
                "response": result.text,
                "conversation_id": conversation_id or "default",
                "timestamp": str(asyncio.get_event_loop().time())
            }
            
        except Exception as e:
            return {
                "response": f"Error: {str(e)}",
                "conversation_id": conversation_id or "default",
                "timestamp": str(asyncio.get_event_loop().time()),
                "error": True
            }
    
    def get_status(self) -> Dict[str, Any]:
        """Get agent status."""
        try:
            return {
                "status": "ready",
                "agent_initialized": self.agent is not None,
                "tools_available": ["get_client_data", "analyze_churn_risk"]
            }
        except Exception as e:
            return {
                "status": "error",
                "error": str(e),
                "agent_initialized": False
            }


async def interactive_chat() -> None:
    """Interactive chat session with the churn analysis agent."""
    
    agent = ChurnAgent()
    
    while True:
        try:
            # Get user input
            user_input = input("> ").strip()
            
            # Check for exit commands
            if user_input.lower() in ['salir', 'exit', 'quit', 'adios']:
                break
            
            if not user_input:
                continue
            
            # Send to agent and get response
            result = await agent.chat(user_input)
            print(result["response"])
            
        except KeyboardInterrupt:
            break
        except Exception as e:
            print(f"Error: {e}")
            break


async def main() -> None:
    await interactive_chat()


if __name__ == "__main__":
    asyncio.run(main())
