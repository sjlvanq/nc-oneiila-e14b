# Frontend - Sprint 2
Inicio: 2026-01-19 | Fin: 2026-01-20

# Resumen
Este sprint se enfoca en la transición hacia la funcionalidad operativa de la plataforma. Se implementará la navegación definitiva con lógica de acceso dinámico, se expondrán y visualizarán las métricas generales del negocio y se habilitará la consulta individual de predicción de abandono (Churn) mediante DNI, aprovechando la refactorización del backend realizada en el ciclo anterior.

## Distribución de Responsabilidades (4 Desarrolladores)

| Desarrollador | Perfil | Responsabilidad Principal |
|---|---|---|
| Dev 1 | Backend | API de Estadísticas: Creación de endpoints para métricas globales (Totales, Activos, Promedio de edad). |
| Dev 2 | Frontend / Logic | Dashboard de Métricas: Consumo de la API de estadísticas y renderizado de componentes StatCard. |
| Dev 3 | Fullstack | Módulo de Predicción: Interfaz de búsqueda por DNI y conexión con el servicio de predicción de Churn. |
| Dev 4 | Frontend / UI | Navegación e Identidad: Estilos finales del Navbar y lógica de visibilidad Login/Logout en la Home. |

## Objetivos Técnicos por Área
1. Estadísticas Globales (Dev 1 & Dev 2)
 * Backend (Dev 1): Implementar en ClientRepository consultas de agregación para obtener el conteo de clientes y el promedio de edad. Exponer estos datos en GET /api/stats.
 * Frontend (Dev 2): Desarrollar el contenedor de métricas en el Dashboard, asegurando que los datos se recuperen de forma asíncrona al cargar la página.
2. Predicción por DNI (Dev 3)
 * Componente de Búsqueda: Crear un formulario de consulta.
 * Integración: Conectar el componente con el endpoint GET /clients/prediction/{dni} desarrollado en el Sprint 1.
 * Visualización: Mostrar el resultado de la predicción (probabilidad de abandono).
3. Navegación y Acceso (Dev 4)
 * Navbar: Migrar los estilos temporales a Navbar.module.css para asegurar aislamiento de estilos.
 * Renderizado Condicional: 
    * En la Home: El botón de acción debe alternar entre "Iniciar Sesión" (público) e "Ir al Dashboard" (si ya hay sesión activa).

## Nuevos archivos y modificaciones

```
├── src
│   ├── components
│   │   ├── layout
│   │   │   ├── Navbar.jsx                 [* Dev 4] -> Lógica condicional Login/Logout
│   │   │   └── Navbar.module.css          [+ Dev 4] -> Estilos finales Iris
│   │   ├── dashboard
│   │   │   └── StatCard.jsx               [+ Dev 2] -> Visualización de KPIs
│   │   └── prediction
│   │       └── ChurnSearch.jsx            [+ Dev 3] -> Buscador por DNI y resultado
│   └── pages
│       └── DashboardPage.jsx              [* Dev 2, Dev 3] -> Integración de Stats y Churn
│
└── backend
    └── ...         [+ Dev 1

```

Entregables Técnicos
 * Métricas en Vivo: Dashboard mostrando datos reales de la base de datos (Clientes totales, activos y edad promedio).
 * Buscador de Churn: Herramienta funcional para predecir el abandono de un cliente específico mediante su DNI.
 * Sistema de Navegación: Navbar con respuesta dinámica al estado de autenticación del usuario.
Análisis de Riesgos
 * Acoplamiento en Dashboard: Dev 2 y Dev 3 trabajarán sobre la misma página (DashboardPage.jsx). Se requiere coordinación o uso de ramas separadas para evitar conflictos de fusión.
 * Cálculo de Promedios: Se recomienda que el Backend maneje el redondeo de la edad promedio a un decimal para simplificar la visualización en el Frontend.