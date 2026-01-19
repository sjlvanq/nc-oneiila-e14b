Documentación de desarrollo - Frontend - ChurnCheck
# Consumo de API

Para todas las peticiones al backend de ChurnCheck, se utiliza una instancia centralizada de Axios.

## Instancia de API
La instancia se encuentra en `src/services/api.js`. Esta ya incluye la `baseURL` y los interceptores necesarios.

### Uso básico
```javascript
import api from '@/services/api';

const fetchData = async () => {
  try {
    const response = await api.get('/endpoint');
    return response.data;
  } catch (error) {
    // Los errores 401 son manejados automáticamente
    throw error;
  }
};

```

## Interceptores

Contamos con lógica automatizada en `src/interceptors/`:

1. **Request:** Inyecta automáticamente el token JWT desde el `localStorage` en los headers de cada petición.
2. **Response:** Escucha errores del servidor. Si detecta un **401 (Unauthorized)**, limpia la sesión y redirige al usuario al Login.

> **Regla:** Nunca añadas el header `Authorization` manualmente en tus componentes.

-----
[Volver a Documentación interna de Frontend](../README.md)