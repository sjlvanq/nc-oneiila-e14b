"""
Entry point para desarrollo local de agentes
"""

import asyncio
import logging
from src.agents.churn_agent import LocalChurnAgent

# Configurar logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

async def main():
    """Función principal para testing local"""
    print("Iniciando Agente Local de Churn Analysis")
    print("=" * 50)
    
    try:
        # Crear instancia del agente local
        agent = LocalChurnAgent()
        
        # Menu interactivo
        while True:
            print("\nOpciones:")
            print("1. Analizar cliente específico")
            print("2. Consultar factores de churn")
            print("3. Generar estrategia de retención")
            print("4. Chat libre con el agente")
            print("5. Salir")
            
            opcion = input("\nSelecciona una opción (1-5): ").strip()
            
            if opcion == "1":
                customer_id = input("ID del cliente: ").strip()
                message = f"Analiza el riesgo del cliente {customer_id}"
                response = await agent.chat(message)
                print(f"\nRespuesta:\n{response}")
                
            elif opcion == "2":
                response = await agent.chat("¿Cuáles son los principales factores de churn?")
                print(f"\nRespuesta:\n{response}")
                
            elif opcion == "3":
                risk_level = input("Nivel de riesgo (ALTO/MEDIO/BAJO): ").strip().upper()
                message = f"Genera una estrategia de retención para un cliente con riesgo {risk_level}"
                response = await agent.chat(message)
                print(f"\nRespuesta:\n{response}")
                
            elif opcion == "4":
                message = input("Tu mensaje: ").strip()
                print("\nPensando...")
                response = await agent.chat(message)
                print(f"\nRespuesta:\n{response}")
                
            elif opcion == "5":
                print("¡Hasta luego!")
                break
                
            else:
                print("Opción no válida. Intenta de nuevo.")
                
    except KeyboardInterrupt:
        print("\nPrograma interrumpido. ¡Hasta luego!")
    except Exception as e:
        logger.error(f"Error en main: {e}")
        print(f"Error: {e}")

if __name__ == "__main__":
    asyncio.run(main())
