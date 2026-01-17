# Frontend - Sprint 1

**Inicio:** 2026-01-16 | **Fin:** 2026-01-18

## Resumen

Este sprint de corto plazo (48 horas) tiene como objetivo unificar los criterios de búsqueda de clientes mediante el **DNI** tanto en el backend como en el frontend, corrigiendo las divergencias técnicas detectadas en ciclos previos. Paralelamente, se iniciará la migración visual hacia la maqueta, estableciendo la arquitectura de estado global para la autenticación en **React** y el flujo de acceso seguro a la plataforma.

---

## Distribución de Responsabilidades (4 Desarrolladores)

| Desarrollador | Perfil | Responsabilidad Principal |
| --- | --- | --- |
| **Dev 1** | Fullstack / Backend | **Persistencia y API:** Refactorización del modelo de datos para incluir DNI y actualización de endpoints de predicción. |
| **Dev 2** | Frontend Lead | **Arquitectura de Estado:** Implementación del `AuthContext` y lógica de persistencia de sesión (JWT). |
| **Dev 3** | Frontend / UI | **Maquetación Base:** Slicing de HTML/CSS de la maqueta hacia componentes React y estructura global de `App.jsx`. |
| **Dev 4** | Frontend / Logic | **Componente de Acceso:** Desarrollo del componente Login integrando la guía experimental con el diseño final de la maqueta. |

---

## Objetivos Técnicos por Área

### 1. Refactorización de Datos y API (Dev 1)

* **Base de Datos:** Modificar el script de creación y `data.sql` para incluir la columna `dni` (String) en la tabla `clients`.
* **Entidad JPA:** Agregar el campo `dni` en `Client.java` con las validaciones correspondientes.
* **Repository:** Implementar `findByDni(String dni)` y eliminar/depreciar la búsqueda por ID para predicciones.
* **Controller & Service:** Actualizar el endpoint a `GET /clients/prediction/{dni}` asegurando que el flujo de orquestación con el microservicio de IA funcione con este nuevo identificador.

### 2. Estructura y Estilos (Dev 3)

* **CSS Slicing:** Extraer los estilos globales de la maqueta Iris e integrarlos en `src/styles/index.css` o un archivo equivalente de Vite.
* **Layout:** Configurar el marco básico en `App.jsx` que contendrá las rutas protegidas y públicas, respetando el contenedor principal definido en el diseño estático.

### 3. Autenticación y Seguridad (Dev 2 & Dev 4)

* **Contexto:** Crear `AuthContext.jsx` para gestionar el estado del usuario, el token JWT y los métodos `login/logout`.
* **Servicio de API:** Implementar un cliente Axios/Fetch centralizado que incluya el token en el header `Authorization` de forma automática.
* **Login:** Desarrollar el formulario en el componente `Login.jsx`, mapeando los inputs del diseño Iris con el estado de React y comunicándolo con el endpoint `/login` de la API.

---

## Nuevos archivos y modificaciones

```text
├── src
│   ├── context
│   │   └── AuthContext.jsx                [+ Dev 2] -> Gestión de JWT y estado global
│   ├── components
│   │   ├── auth
│   │   │   └── Login.jsx                  [+ Dev 4] -> Estructura Iris + Lógica experimental
│   │   └── layout
│   │       └── MainLayout.jsx             [+ Dev 3] -> Marco estático de Iris
│   ├── styles
│   │   └── iris.css                       [+ Dev 3] -> Estilos extraídos de la maqueta
│   └── App.jsx                            [* Dev 3] -> Configuración de rutas y Providers
│
├── backend (Refactor)
│   ├── domain/client/Client.java          [* Dev 1] -> Campo DNI
│   ├── service/ChurnService.java          [* Dev 1] -> Lógica por DNI
│   └── controller/ClientController.java   [* Dev 1] -> Endpoint GET /prediction/{dni}

```

---

## Flujo de Trabajo y Dependencias

1. **Bloqueo Inicial:** Dev 2 y Dev 4 dependen de la definición final de los estilos de Dev 3 para que el componente de Login no requiera re-trabajo visual.
2. **Sincronización API:** Dev 4 debe coordinar con Dev 1 los nombres exactos de los campos del JSON que retorna el `/login` (implementado en el Sprint 1) para asegurar la compatibilidad con el `AuthContext`.
3. **Unificación DNI:** Dev 1 debe entregar la actualización del repositorio y el servicio antes de que el equipo de frontend comience a prototipar la vista de resultados de predicción (siguiente sprint).

---

## Entregables Técnicos

* **API Actualizada:** Endpoint `/clients/prediction/{dni}` funcional y documentado en Swagger.
* **Persistencia Frontend:** Token JWT almacenado correctamente en el estado de la aplicación y persistido en `localStorage`.
* **Interfaz Base:** Aplicación Vite+React mostrando la estructura visual de Iris con un formulario de Login operativo.
* **Scripts de Datos:** Archivo `data.sql` actualizado con DNI reales para pruebas de integración.

---

## Análisis de Riesgos y Recomendaciones

* **Consistencia de Estilos:** Se recomienda que Dev 3 use variables CSS para los colores y fuentes de Iris, facilitando ajustes futuros en toda la aplicación.
* **Manejo de Errores:** Dev 4 debe implementar feedback visual (ej. alertas o mensajes de error) en el Login para casos de "Credenciales Inválidas" o "Servidor No Disponible", utilizando los códigos de estado definidos por la API.