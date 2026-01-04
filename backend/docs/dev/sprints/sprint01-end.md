# Notas de implementación - Sprint 1
2025-12-26

Plan del Sprint 1: [enlace](sprint01.md)

## 1. Resumen de estado

El sistema de seguridad **stateless** con **JWT** se encuentra implementado y funcional según los requerimientos del Sprint 1. Se ha integrado el flujo completo desde la recepción de credenciales hasta la validación de tokens en peticiones subsecuentes.

## 2. Análisis de cumplimiento por área

### Capa de Persistencia (Dev A)

* **Entidades:** Las clases `User` y `Role` cumplen con el mapeo de tablas `users` y `roles`. Se implementó correctamente la relación `@ManyToMany` con carga `EAGER`.
* **Repositorio:** `UserRepository` incluye la consulta optimizada `findByEmailWithRoles` utilizando `JOIN FETCH` y filtrando por usuarios activos.

### Núcleo de Identidad (Dev B)

* **AuthUser:** Implementa `UserDetails` y mapea correctamente los roles a `GrantedAuthority`.
* **AuthUserService:** Implementa `loadUserByUsername` y añade un método adicional `validateAccess` para verificar si el usuario está activo antes de emitir el token.
* **TokenService:** Utiliza la librería `java-jwt` (HMAC256) con una expiración configurada de 2 horas. El secreto utiliza una propiedad inyectada con un valor por defecto para entornos de desarrollo.

### Infraestructura de Seguridad (Dev C)

* **SecurityConfig:** Configurado como `STATELESS`. Se definieron permisos públicos para `/login`, `/h2-console/**` y la documentación de Swagger.
* **SecurityFilter:** Implementado para extraer el token del header `Authorization`, validar el `subject` y establecer el contexto de autenticación.
* **AuthController:** Gestiona el endpoint `/login` devolviendo un `TokenDTO` tras una autenticación exitosa.

## 3. Observaciones y Divergencias Técnicas

* **Integración de SpringDoc:** Se detecta una implementación adicional no detallada en el plan original (`SpringDocConfig`), la cual configura el esquema de seguridad para JWT en la interfaz de Swagger.
* **Paquetes:** El plan mencionaba `com.example.demo`, el código ya está organizado bajo `com.churncheck.api`.

---
