Documentación de desarrollo - Frontend - ChurnCheck
# Gestión de Autenticación (JWT)

El sistema de autenticación se basa en **JSON Web Tokens (JWT)** y se gestiona globalmente mediante la Context API de React.

## AuthContext
El `AuthContext` (`src/contexts/AuthContext.jsx`) es la fuente de verdad para el estado de la sesión.

### Hook: `useAuth`
Permite acceder al estado de autenticación en cualquier componente:
```javascript
const { token, isAuthenticated, login, logout } = useAuth();

```

## Flujo de Autenticación

1. **Login:** Al validar credenciales, el token se almacena en `localStorage` y se actualiza el estado global.
2. **Persistencia:** Al recargar la página, el contexto verifica la existencia del token en `localStorage` para restaurar la sesión.
3. **Cierre de Sesión:** El método `logout` elimina el token y limpia el estado, provocando que las rutas protegidas se cierren.

## Rutas Protegidas

Para proteger una vista, se debe registrar dentro del grupo de `ProtectedRoute` en `src/App.jsx`. Esto impide el acceso vía URL a usuarios no autenticados.
