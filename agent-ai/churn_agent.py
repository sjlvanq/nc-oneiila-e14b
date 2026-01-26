import asyncio
from random import randint
from typing import Annotated, Dict, Any

from agent_framework.azure import AzureOpenAIAssistantsClient
from azure.identity import AzureCliCredential
from pydantic import Field
from tools.client_data import get_client_by_id, analyze_churn_risk, list_clients
import os
from dotenv import load_dotenv

# Cargar variables de entorno
load_dotenv()

"""
ChurnCheck Assistants Churn Analysis Agent

This sample demonstrates a churn analysis agent with automatic
assistant lifecycle management, showing both streaming and non-streaming responses.
"""


class ChurnAgent:
    """Churn Analysis Agent class for Azure Functions integration."""
    
    def __init__(self):
        self.agent = None
        self._initialize_agent()
    
    def _initialize_agent(self):
        """Initialize the agent with Azure OpenAI Assistants."""
        try:
            self.agent = AzureOpenAIAssistantsClient(credential=AzureCliCredential()).as_agent(
                instructions="""You're a AI agent specialized in churn analysis of clients.
                Your objective is to analyze the risk of a client leaving the service.
                Use the available tools to obtain data from the CSV and analyze risk patterns.
                Provide specific recommendations based on real data to reduce churn.
                Respond in English or Spanish professionally and concisely.
                Always look for the client's information first before analyzing the risk.""",
                tools=[get_client_by_id, analyze_churn_risk, list_clients],
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
                "tools_available": ["get_client_by_id", "analyze_churn_risk", "list_clients"]
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
