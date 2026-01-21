# Frontend - Sprint 3

**Inicio:** 2026-01-20 | **Fin:** 2026-01-21 (Ciclo de 24h)

## Resumen

Este sprint tiene como objetivo profundizar en la analítica individual del cliente y la profesionalización de la infraestructura. Se transformará la búsqueda por DNI en una ficha de cliente detallada con visualizaciones de datos históricos (asistencia y gastos) y un motor de recomendaciones basado en el riesgo de abandono. Además, se habilitará el despliegue multi-entorno y se mejorará la experiencia móvil.

## Distribución de Responsabilidades (5 Desarrolladores)

| Desarrollador | Perfil | Responsabilidad Principal |
| --- | --- | --- |
| **Dev 1** | DevOps / BE | **Infraestructura y API:** Configuración de entornos (Vite/Backend) y nuevos endpoints de estadísticas individuales. |
| **Dev 2** | Frontend | **Visualización de Datos A:** Componente de asistencia (6 meses) y finalización de estadísticas globales (Sprint 2). |
| **Dev 3** | Fullstack | **Lógica de Cliente:** Estilización de ficha de cliente y motor de recomendaciones condicionales. |
| **Dev 4** | UI / UX | **Experiencia de Usuario:** Sidebar móvil (Responsive) y ampliación de la Landing Page. |
| **Dev 5** | Frontend | **Visualización de Datos B:** Componente de gastos extra categorizados (6 meses). |

---

## Objetivos Técnicos por Área

### 1. Infraestructura y Backend (Dev 1)

* **Multi-entorno:** Configurar archivos `.env.development` y `.env.production` en el frontend para apuntar a diferentes URLs de API.
* **Enriquecimiento de Churn:** Modificar el DTO de respuesta en `/clients/prediction/{dni}` para incluir el campo `age`.
* **Endpoint Estadístico:** Crear `GET /clients/statistics/{id}` que devuelva un JSON estructurado con el histórico de 6 meses de asistencia y gastos por categoría.

### 2. Visualización de Datos (Dev 2 & Dev 5)

* **Asistencia (Dev 2):** Crear un componente de gráfico (ej. Líneas o Barras) que muestre la frecuencia mensual de asistencia del cliente. * **Gastos Extra (Dev 5):** Crear un componente de visualización (ej. Gráfico de tarta o barras apiladas) que desglose los gastos en categorías (Cafetería, Tienda) de los últimos 6 meses.
* **Global Stats (Dev 2):** Finalizar la integración del componente de estadísticas generales pendiente del Sprint 2 utilizando los datos de `/api/stats`.

### 3. Inteligencia de Cliente (Dev 3)

* **Ficha de Detalles:** Maquetar la presentación visual de los datos clave: Nombre, Teléfono, Edad y los indicadores de Churn/Probabilidad con colores semánticos (Rojo: Riesgo alto, Verde: Bajo).
* **Motor de Recomendaciones:** Implementar lógica de renderizado condicional. Si la probabilidad de abandono es > 60%, mostrar alertas de "Acción Preventiva" (ej. Ofrecer descuento o entrevista personal).

### 4. UI Extendida (Dev 4)

* **Sidebar Mobile:** Implementar el comportamiento *drawer* (desplegable) para la navegación lateral en dispositivos móviles.
* **Landing Page:** Añadir secciones para dar más profundidad a la página de inicio pública.
* **Login Form:** Añadir control para regresar al Home (Landing Page).

---

## Nuevos archivos y modificaciones

```text
├── .env.development               [+ Dev 1] -> URL Backend local
├── .env.production                [+ Dev 1] -> URL Backend producción
├── src
│   ├── components
│   │   ├── dashboard
│   │   │   ├── AttendanceChart.jsx           [+ Dev 2] -> Gráfico de asistencia
│   │   │   └── AdditionalChargesChart.jsx    [+ Dev 5] -> Gráfico de gastos categorizados
│   │   ├── prediction
│   │   │   ├── ClientDetails.jsx      [+ Dev 3] -> UI de datos (DNI, Tel, etc.)
│   │   │   └── Recommendations.jsx    [+ Dev 3] -> Lógica de acción preventiva
│   │   └── layout
│   │       └── Sidebar.jsx            [* Dev 4] -> Versión móvil (Hamburguesa)
│   └── pages
│       └── Home.jsx                   [* Dev 4] -> Contenido extendido
│
└── backend (Refactor)
    ├── dto/ClientStatsDTO.java        [+ Dev 1] -> Estructura de asistencia/gastos
    └── controller/ClientController.java [* Dev 1] -> Nuevo endpoint /statistics/{id}

```

---

## Entregables Técnicos

* **Dashboard Detallado:** Vista completa del cliente tras buscar por DNI, integrando datos personales y gráficos históricos.
* **Sistema Transaccional de Recomendaciones:** El sistema sugiere acciones comerciales basadas en la IA de Churn.
* **Entornos Segregados:** Aplicación capaz de cambiar de servidor de API según el modo de compilación.
* **Responsive Design:** Navegación lateral funcional en smartphones.

---

## Análisis de Riesgos

* **Presión de Tiempo (24h):** La carga de trabajo es alta. Se priorizará la funcionalidad de los gráficos sobre el pulido estético extremo.
* **Consistencia de Datos:** Dev 2 y Dev 5 deben acordar con Dev 1 el formato exacto del JSON de estadísticas para evitar errores de mapeo en los componentes de gráficos.
