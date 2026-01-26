#!/usr/bin/env python3
"""
Script de prueba para las herramientas MCP refactorizadas
"""

from tools.client_data import get_client_by_id, analyze_churn_risk, list_clients

def test_tools():
    print("Probando herramientas MCP refactorizadas...")
    print("=" * 50)

    # Probar get_client_by_id
    print("\n1. Probando get_client_by_id con DNI-1012:")
    result = get_client_by_id("DNI-1012")
    print(result)

    print("\n2. Probando analyze_churn_risk con DNI-1012:")
    result = analyze_churn_risk("DNI-1012")
    print(result)

    print("\n3. Probando list_clients:")
    result = list_clients()
    print(result[:500] + "..." if len(result) > 500 else result)

    print("\nPruebas completadas!")

if __name__ == "__main__":
    test_tools()