# Notas de implementación - Frontend Sprint 1
2026-01-19

Plan del Sprint 1: [enlace](sprint01.md)

## 1. Resumen de Implementación

Se ha completado la infraestructura base de autenticación y enrutamiento del frontend de ChurnCheck. Se implementó el flujo completo de login mediante JWT, la arquitectura de contexto global para la gestión de sesiones, y la migración visual inicial hacia la maqueta Iris. La unificación del criterio de búsqueda por DNI entre backend y frontend quedó establecida a nivel de API.

---

## 2. Análisis de cumplimiento por área

### Refactorización de Datos y API (Dev 1)

* **Entidad JPA:** Se agregó correctamente el campo `dni` en `Client.java`.
* **Repository:** Se implementó el método `findByDni(String dni)` en `ClientRepository.java` para búsqueda por DNI.
* **Controller & Service:** Se creó el nuevo endpoint `GET /clients/prediction/{dni}` en `ClientController.java` y el método `predictChurnByDni(String dni)` en `ClientService.java`.
* **Base de Datos:** Se actualizaron los scripts `schema.sql` y `data.sql` para incluir la columna `dni` con valores de ejemplo (DNI-1001, DNI-1002, DNI-1003, DNI-1004).

### Componentes de Interfaz y Navegación (Dev 2)

* **LoginForm:** Se desarrolló un componente funcional con validación de estado local para los campos de email y contraseña, integrado directamente con el hook `useAuth`.
* **Navegación:** Se implementó un componente `Navbar.jsx` mínimo con enlaces de navegación y acceso / cierre de sesión.

### Estructura y Estilos (Dev 3)

* **CSS Slicing:** Se creó `src/styles/index.css` con estilos globales minimalistas y reset básico. Los estilos específicos de componentes se implementaron mediante módulos CSS.
* **Layout:** Se configuró `App.jsx` con React Router v7 utilizando `createBrowserRouter` y un componente `Layout` que incluye `<Navbar>` y `<Outlet>` para las rutas hijas.
* **Página de Inicio:** Se implementó `Inicio.jsx` que utiliza la imagen de fondo `gym-bg.jpg` y estilos en `Inicio.module.css`.

### Autenticación y Seguridad (Dev 2 & Dev 4)

* **Contexto:** Se implementó `AuthContext.jsx` con los métodos `login`, `logout` y el estado `isAuthenticated`. El token se persiste en `localStorage` y se restaura automáticamente al recargar la página.
* **Servicio de API:** Se creó `src/services/api.js` como instancia centralizada de Axios con `baseURL: 'http://localhost:8080'`. Se implementó `auth.interceptor.js` que:
  - Inyecta automáticamente el token JWT en el header `Authorization` de cada petición.
  - Captura errores 401 y redirige al login automáticamente, limpiando el `localStorage`.
* **Rutas Protegidas:** Se creó el componente `ProtectedRoute.jsx` que actúa como guardián de navegación, redirigiendo a usuarios no autenticados a la página de `/login`.
* **Login:** Se desarrolló `Login.jsx` como componente reutilizable con:
  - Manejo de estados de formulario (`username`, `password`).
  - Estados de control visual (`loading`, `error`, `response`).
  - Validación de errores por campo utilizando el formato de respuesta del backend.
  - Redirección automática al dashboard tras login exitoso.
  - Se creó `PageLogin.jsx` como wrapper que incluye el logo de ChurnCheck y el componente `Login`.

---

## 3. Observaciones y Divergencias Técnicas

* **Rutas placeholders:** La página `DashboardPage` se ha creado como placeholder funcional para permitir la navegación, pero su contenido real se desarrollará en los sprints posteriores conforme se habiliten los servicios de backend correspondientes.
* **Módulos CSS:** Se adoptó CSS Modules (`.module.css`) para evitar conflictos de nombres de clases. Los archivos implementados son:
  - `Login.module.css` (estilos del formulario)
  - `PageLogin.module.css` (estilos de la página de login)
  - `Inicio.module.css` (estilos del hero)
* **Rutas Protegidas:** Se implementó el componente `ProtectedRoute.jsx` que utiliza `<Outlet>` para renderizar rutas hijas solo si el usuario está autenticado, redirigiendo a `/login` en caso contrario.
* **Configuración de Axios:** Se definió la `baseURL` como `http://localhost:8080/api`, lo cual es correcto para desarrollo, pero deberá parametrizarse mediante variables de entorno (`.env`) antes del despliegue.
* **Alias de Importación:** Se configuró Vite para soportar el alias `@` apuntando a `./src`, permitiendo importaciones como `import api from '@/services/api'`.
* **Dependencias Instaladas:**
  - `axios: ^1.13.2`
  - `react-router-dom: ^7.12.0`
* **Navbar Temporal:** Se implementó un componente `Navbar.jsx` con estilos inline (pendiente de migración a módulos CSS) que muestra/oculta opciones según el estado de autenticación.

---

## 4. Flujo de Autenticación Implementado

1. El usuario ingresa credenciales en `Login.jsx`.
2. Se realiza un `POST` a `/login` mediante la instancia de Axios (`api.js`).
3. El interceptor de request añade automáticamente el token si existe.
4. Si la respuesta es exitosa (`200`), se ejecuta `login(token)` del `AuthContext`, almacenando el token en `localStorage` y actualizando el estado global.
5. Se redirige automáticamente a `/dashboard` mediante `navigate()`.
6. En peticiones subsecuentes, el interceptor de request inyecta el token en el header `Authorization: Bearer {token}`.
7. Si el backend responde con `401`, el interceptor de response limpia la sesión y redirige a `/login`.

---

## 5. Entregables Técnicos Completados

- **API Actualizada:** Endpoint `/clients/prediction/{dni}` funcional y documentado en Swagger.  
- **Persistencia Frontend:** Token JWT almacenado en estado global y `localStorage` con restauración automática.  
- **Interfaz Base:** Aplicación React con Vite mostrando estructura visual de Iris, formulario de Login operativo y página de inicio con hero visual.  
- **Scripts de Datos:** Archivo `data.sql` actualizado con valores de DNI (`DNI-1001` a `DNI-1004`).  
- **Arquitectura de Estado:** `AuthContext` funcional con métodos `login/logout` y hook `useAuth()`.  
- **Rutas Protegidas:** Sistema de rutas implementado con `ProtectedRoute` que bloquea acceso no autenticado a `/dashboard`.

