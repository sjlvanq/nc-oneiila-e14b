# Churn Insight - Backend

## 📌 Descripción
Este proyecto forma parte de un MVP para la predicción de churn (cancelación de clientes) en servicios por suscripción.

El backend expone una API REST que permite consultar la probabilidad de churn de un cliente, utilizando un modelo predictivo desarrollado por el equipo de Data Science.

---

## 🗄️ Base de Datos (H2)

Para el entorno de desarrollo se utiliza **H2**, una base de datos liviana y en memoria.

Los scripts de inicialización se encuentran en:


---

## 📊 Modelo de Datos

### clients
Representa los clientes finales que pueden presentar churn.

- plan: tipo de suscripción
- fecha_alta: fecha de inicio del servicio

---

### users
Usuarios internos del sistema (admin, analistas, soporte).

---

### roles
Define los roles del sistema (ej: ADMIN, ANALYST).

---

### permissions
Acciones permitidas dentro del sistema (ej: READ_CLIENTS).

---

### role_permissions
Relaciona roles con permisos (muchos a muchos).

---

### user_client_access
Controla qué usuarios pueden acceder a qué clientes y con qué nivel de acceso.

Ejemplo:
- Un analista solo puede leer datos de ciertos clientes
- Un administrador tiene acceso completo

---

## 🚀 Ejecución
Al iniciar la aplicación Spring Boot, H2 carga automáticamente `schema.sql` y `data.sql`.

No se requiere configuración adicional para el entorno de desarrollo.
