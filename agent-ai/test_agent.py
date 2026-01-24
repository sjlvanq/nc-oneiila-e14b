"""Test script para el ChurnAgent"""

from churn_agent import ChurnAgent

def test_agent():
    try:
        agent = ChurnAgent()
        print("Agente inicializado")
        print("Estado:", agent.get_status())
        
        # Probar chat (síncrono)
        response = agent.chat("Hola, necesito analizar churn")
        print("Chat response:")
        print(response)
        
    except Exception as e:
        print("Error:", e)
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    test_agent()
