import azure.functions as func
import logging
import json
from typing import Optional

# Importar el agente modular
from churn_agent import ChurnAgent

app = func.FunctionApp(http_auth_level=func.AuthLevel.ANONYMOUS)

# Instancia global del agente (se inicializa automáticamente)
chat_agent = ChurnAgent()

@app.route(route="chat", methods=["POST"])
def chat(req: func.HttpRequest) -> func.HttpResponse:
    """
    Endpoint principal de chat - compatible con el backend Java simplificado
    """
    try:
        logging.info(f"Received request: {req.url}")
        
        # Parsear request body
        req_body = req.get_json()
        message = req_body.get('message')
        conversation_id = req_body.get('conversation_id')
        
        logging.info(f"Message: {message}, Conversation ID: {conversation_id}")
        
        # Validar mensaje
        if not message:
            return func.HttpResponse(
                json.dumps({"error": "Message is required"}),
                status_code=400,
                mimetype="application/json"
            )
        
        # Llamar al agente (síncrono)
        response = chat_agent.chat(message, conversation_id)
        
        logging.info(f"Agent response: {response}")
        
        return func.HttpResponse(
            json.dumps(response),
            status_code=200,
            mimetype="application/json"
        )
        
    except Exception as e:
        logging.error(f"Error in chat function: {str(e)}")
        return func.HttpResponse(
            json.dumps({
                "error": "Internal server error",
                "details": str(e)
            }),
            status_code=500,
            mimetype="application/json"
        )

@app.route(route="health", methods=["GET"])
def health_check(req: func.HttpRequest) -> func.HttpResponse:
    """Health check endpoint con estado del agente"""
    try:
        agent_status = chat_agent.get_status()
        
        return func.HttpResponse(
            json.dumps({
                "status": "healthy",
                "service": "Churn Agent Function",
                "version": "1.0.0",
                "agent": agent_status
            }),
            status_code=200,
            mimetype="application/json"
        )
    except Exception as e:
        logging.error(f"Error en health check: {e}")
        return func.HttpResponse(
            json.dumps({
                "status": "unhealthy",
                "service": "Churn Agent Function",
                "error": str(e)
            }),
            status_code=503,
            mimetype="application/json"
        )
