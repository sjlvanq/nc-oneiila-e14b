# Database Seed — 1000 Clients Dataset

Este archivo contiene un **seed SQL completo** diseñado para poblar la base de datos con información realista y coherente para **pruebas de integración backend y analítica/ML**.

El dataset respeta el schema relacional definido y los rangos del **Diccionario de Datos de Integración Backend**.

---

## 📦 Archivo incluido

### `seed_1000.sql`

Script SQL que inserta datos en las siguientes tablas:

- `users`
- `roles`
- `user_roles`
- `partners`
- `charge_types`
- `group_activities`
- `clients` (**1000 registros**)
- `additional_charges`
- `attendance`
- `group_activity_attendance`

El script se ejecuta dentro de una **transacción** y desactiva temporalmente las **foreign keys** para facilitar la carga masiva.

---

## 🧩 Descripción de los datos

### Clients
- 1000 clientes con perfiles variados (alta retención, riesgo medio, churn potencial).
- Edades entre **18 y 41 años**.
- Contratos de **1, 6 o 12 meses**.
- `partner_id` y `phone` pueden ser `NULL` según el perfil.
- Fechas de registro, inicio de contrato y vigencia **coherentes entre sí**.

### Attendance
- Registros de asistencia simulados **solo para los últimos 30 días**.
- La cantidad de visitas es consistente con la variable:
  - `avgClassFrequencyCurrentMonth`

### Additional Charges
- Cargos adicionales asociados a clientes reales.
- Montos dentro del rango **0.15 – 552.33**.
- Tipos de cargos vinculados a `charge_types`.

### Group Activities
- Asistencia a actividades grupales solo para clientes con `group_visit = 1`.
- Datos generados para los **últimos 60 días**.

---

## 🔐 Integridad referencial

- Todas las claves foráneas (`FK`) son válidas.
- No existen referencias huérfanas.
- IDs explícitos para facilitar debugging y pruebas determinísticas.

---

## 🚀 Uso recomendado

```sql
mysql -u user -p database_name < seed_1000.sql
