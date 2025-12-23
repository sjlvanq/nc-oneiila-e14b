# Sprint 1
Inicio: 2025-12-23 | Fin: 2025-12-26

## Resumen
El objetivo principal de este sprint de tres días es implementar el sistema de seguridad **stateless** (sin estado) utilizando **JWT** para proteger los recursos de la API. El equipo colaborará para integrar la autenticación mediante un flujo que involucra la validación de credenciales a través de un **DTO de login**, la búsqueda y validación de usuarios en la base de datos mediante un repositorio especializado, y la emisión de tokens firmados que serán verificados en cada petición por un filtro de seguridad personalizado.

## Nuevos archivos de código
```
└──- src/main/java/com/example/demo
      |
      ├── DemoApplication.java
      ├── domain
      │   └── user
      │       ├── User.java
      │       └── UserRepository.java
      └── infra
            ├── security
            │   ├── AuthUser.java
            │   ├── AuthUserService.java
            │   ├── LoginRequestDTO.java
            │   ├── SecurityConfig.java
            │   ├── SecurityFilter.java
            │   ├── TokenDTO.java
            │   └── TokenService.java
            └── springdoc
```

## Workflow

1. **`LoginRequestDTO`** recibe las credenciales.
2. **`AuthUserService`** busca al usuario mediante el **`UserRepository`**.
3. **`AuthUser`** proporciona la información de seguridad.
4. **`TokenService`** genera el token si las credenciales son válidas.
5. En peticiones subsecuentes, **`SecurityFilter`** valida el token usando el **`TokenService`** y aplica las reglas de **`SecurityConfig`**.

## Propuesta de división de tareas

División por capas **Dominio/Datos**, **Lógica de Seguridad** y **Configuración de Filtros**.

| Desarrollador | Área de Foco | Responsabilidades Principales | Dependencias Críticas (Bloqueantes) |
| --- | --- | --- | --- |
| **Dev A (Persistencia)** | Capa de Persistencia | Entidad `User`, `Role`, `UserRepository` y ajuste de `data.sql`. | Ninguna. Necesita los nombres de tablas de `schema.sql`. |
| **Dev B (Identidad)** | Núcleo de Identidad | Implementación de `AuthUser`, `AuthUserService` y `TokenService`. | Requiere que **Dev A** defina la entidad `User` y el método `findByEmailWithRoles`. |
| **Dev C (Infraestructura)** | Infraestructura Web | `SecurityConfig`, `SecurityFilter` y el controlador `AuthController`. | Requiere que **Dev B** entregue los métodos de validación de `TokenService` y el `AuthUserService`. |

---

1. **De Dev A a Dev B**: `AuthUserService` no puede compilar sin la interfaz `UserRepository`. Además, el constructor de `AuthUser` necesita recibir la entidad `User` para extraer el email, el hash de la contraseña y el estado activo.
2. **De Dev B a Dev C**: El `SecurityFilter` de **Dev C** llama directamente a `tokenService.getSubject()` para identificar al usuario en cada petición. Sin la lógica de **Dev B**, el filtro no tiene forma de validar si un token es real o falso.
3. **El factor `data.sql**`: **Dev C** no podrá realizar pruebas de login exitosas hasta que **Dev A** proporcione un hash de BCrypt válido en el archivo de datos. Si **Dev A** inserta texto plano, el `AuthenticationManager` de **Dev C** siempre rechazará el acceso.

## Guía de Trabajo por Desarrollador

### Dev A: Capa de Persistencia y Datos

* **Entidades JPA:** Crear `User.java` y `Role.java` mapeando exactamente las tablas `users` y `roles`. En `User`, mapear el campo `password_hash` y el booleano `active`.
* **Relaciones:** Configurar la relación `@ManyToMany` entre `User` y `Role` a través de la tabla de unión `user_roles` con las columnas `user_id` y `role_id`.
* **UserRepository:** Completar la interfaz extendiendo `JpaRepository`. Implementar la consulta `findByEmailWithRoles` usando `JOIN FETCH` y asegurando que solo recupere usuarios con `active = true`.
* **Corrección de Data Seed:** Editar `data.sql` para incluir la columna `password_hash` en los `INSERT` de la tabla `users` utilizando hashes reales de BCrypt.

### Dev B: Identidad y Servicio de Tokens

* **AuthUser:** Implementar la interfaz `UserDetails` en `AuthUser.java`. Mapear la lista de roles del usuario a `SimpleGrantedAuthority` (ej. "ROLE_ADMIN").
* **AuthUserService:** Completar `loadUserByUsername` para invocar al repositorio de Dev A y retornar el `AuthUser`. Implementar la validación post-autenticación basada en el campo `active` de la base de datos.
* **TokenService:** Implementar la lógica de `createToken` y `getSubject` usando la librería `java-jwt`. Configurar la expiración y el secreto mediante la propiedad `api.security.token.secret`.

### Dev C: Infraestructura de Seguridad y API

* **SecurityConfig:** Configurar el `SecurityFilterChain` para que `/login` sea público y el resto de rutas requieran autenticación. Registrar los beans de `BCryptPasswordEncoder` y `AuthenticationManager`.
* **SecurityFilter:** Implementar el filtro que captura el header `Authorization`, valida el token con el `TokenService` y registra al usuario en el `SecurityContextHolder` para cada petición.
* **AuthController:** Crear el controlador para el endpoint `/login`. Debe recibir un `LoginRequestDTO`, usar el `AuthenticationManager` para validar las credenciales contra la DB y devolver un `TokenDTO` con el JWT generado.

---
Todo muy bien
