# Notas de implementación - Sprint 3 (Frontend)

2026-01-22

Plan del Sprint 3: [enlace](sprint03.md) 

## 1. Resumen de Implementación

Se ha completado el Sprint 3 enfocándose en la **profesionalización de la infraestructura** y el **detalle analítico del cliente**. Se implementó un sistema de entornos segregados mediante archivos `.env`, se integraron visualizaciones de datos históricos con Chart.js y se habilitó una experiencia móvil completa con un sidebar responsive. Además, la Landing Page ha sido expandida para reflejar la misión y beneficios de la plataforma.

---

## 2. Análisis de cumplimiento por área

### Infraestructura y Backend (Dev 1)

*  **Entornos Segregados:** Se crearon los archivos `.env.development`, `.env.production.example` y `.env.local.example` para gestionar dinámicamente la URL de la API (`VITE_API_URL`) según el modo de ejecución.
*  **Seguridad:** Se actualizó el archivo `.gitignore` para excluir archivos de entorno locales y de producción, evitando la fuga de configuraciones sensibles.
*  **Backend (Refactor):** Preparación del endpoint `/statistics/{id}` para alimentar los nuevos gráficos.

### Visualización de Datos (Dev 2 y Dev 5)

* **Gráficos de Asistencia:** Implementación del componente `AttendanceChart.jsx` utilizando `react-chartjs-2` para mostrar la evolución de asistencia de los últimos 6 meses.
* **Gráficos de Gastos:** Creación de `AdditionalChargesChart.jsx` para visualizar el desglose de consumos extra (cafetería, suplementos, etc.).
* **Dependencias:** Se añadieron con éxito `chart.js` y `react-chartjs-2` al `package.json`.

### Lógica de Cliente y Recomendaciones (Dev 3)

* **Ficha Técnica:** Desarrollo de `ClientDetails.jsx` para mostrar datos personales detallados (DNI, teléfono, estado) tras la búsqueda.
* **Motor de Recomendaciones:** Implementación de `Recommendations.jsx`, que genera sugerencias comerciales automáticas basadas en el nivel de riesgo de abandono detectado.

### Experiencia de Usuario y Landing Page (Dev 4)

* **Sidebar Responsive:** Refactorización de `Sidebar.jsx` para incluir navegación optimizada en smartphones.
* **Contenido Expandido:** Ampliación de la página de inicio (`Home.jsx`) con secciones de "Características", "Misión/Visión" y "Beneficios".
* **Refactorización de Estructura:** Corrección de la carpeta `desing` a `design` y actualización de rutas de activos.
* **Identidad Visual:** Actualización del favicon y el título de la aplicación en `index.html`.

---

## 3. Entregables Técnicos Completados

*  **Multi-entorno:** Aplicación configurada para alternar entre servidor local y producción automáticamente.
*  **Dashboard Detallado:** Vista unificada que combina probabilidad de *churn*, datos históricos y gastos en una sola interfaz.
*  **Acciones Preventivas:** Interfaz que no solo predice el riesgo, sino que propone acciones inmediatas para el equipo comercial.
*  **Responsive Design:** Navegación lateral funcional y estética en dispositivos móviles.

