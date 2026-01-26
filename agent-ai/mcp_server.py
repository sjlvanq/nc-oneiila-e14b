"""
MCP Server for churn analysis and client data
Exposes MCP tools for dynamic access to CSV data
"""

import asyncio
import os
from dotenv import load_dotenv
from agent_framework.openai import OpenAIResponsesClient
from mcp.server.stdio import stdio_server
from tools.client_data import get_client_by_id, analyze_churn_risk, list_clients

load_dotenv()

# El agente usará Azure OpenAI configurado en tu entorno
agent = OpenAIResponsesClient(
    api_key=os.getenv("AZURE_AI_API_KEY"),
    endpoint=os.getenv("AZURE_OPENAI_ENDPOINT"),
    model_id=os.getenv("AZURE_OPENAI_CHAT_DEPLOYMENT_NAME", "gpt-4.1")
).as_agent(
    name="ChurnAnalysisMCP",
    description="MCP Server for churn analysis and client data. Exposes MCP tools for dynamic access to CSV data.",
    tools=[
        get_client_by_id,
        analyze_churn_risk,
        list_clients
    ]
)

# Exponer agente como servidor MCP
server = agent.as_mcp_server()

async def run():
    """
    Starts the MCP Server for Churn Analysis via stdio
    """
    print("Starting MCP Server for Churn Analysis...")
    print("Available tools:")
    print("   • get_client_by_id: Search client by DNI")
    print("   • analyze_churn_risk: Analyze churn risk")
    print("   • list_clients: List all clients")
    print("MCP Server ready to receive MCP connections...")
    
    async with stdio_server() as (read_stream, write_stream):
        await server.run(read_stream, write_stream, server.create_initialization_options())

if __name__ == "__main__":
    asyncio.run(run())