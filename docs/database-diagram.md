
![ORACLE + Alura Latam](assets/oraclealura-logo.svg)
## Hackathon Oracle Next Education II - Latam

***Desafío intensivo de innovación para participantes de todo Latam.***

powered by ![NoCountry](assets/nocountry-logo.png)

----

# ChurnCheck - Arquitectura y Diseño de Datos (ERD)
## Predicción de Cancelación de Clientes

* Proyecto ChurnInsight
* **Equipo: H12-25-L-Equipo 14-Data Science**

-----

> Aunque el modelo de predicción trabaja con un dataset estructurado de forma estática, se ha decidido implementar un Modelo de Datos Dinámico. El objetivo es aportar realismo técnico a la simulación, transformando una base de datos plana en un sistema transaccional capaz de generar dinámicamente la información que el modelo de IA requiere.

-----

**Entidades principales:**
- **USERS**: Gestiona usuarios del sistema con autenticación
- **ROLES**: Define roles disponibles
- **PARTNERS**: Empresas o socios asociados
- **CLIENTS**: Clientes registrados en el sistema
- **CHARGE_TYPES**: Tipos de cargos adicionales
- **ADDITIONAL_CHARGES**: Cargos adicionales aplicados a clientes
- **ATTENDANCE**: Registro de asistencia individual
- **GROUP_ACTIVITIES**: Actividades grupales
- **GROUP_ACTIVITY_ATTENDANCE**: Asistencia a actividades grupales

**Relaciones clave:**
- Los usuarios tienen roles mediante una tabla de unión
- Los clientes están asociados a partners
- Los clientes generan cargos adicionales
- Los clientes registran asistencia individual y grupal

PK = Primary Key, FK = Foreign Key, UK = Unique Key

-----
```mermaid

erDiagram
    USERS ||--o{ USER_ROLES : "has"
    ROLES ||--o{ USER_ROLES : "assigned to"
    PARTNERS ||--o{ CLIENTS : "refers"
    CLIENTS ||--o{ ADDITIONAL_CHARGES : "receives"
    CHARGE_TYPES ||--o{ ADDITIONAL_CHARGES : "defines"
    CLIENTS ||--o{ ATTENDANCE : "records"
    CLIENTS ||--o{ GROUP_ACTIVITY_ATTENDANCE : "attends"
    GROUP_ACTIVITIES ||--o{ GROUP_ACTIVITY_ATTENDANCE : "includes"

    USERS {
        bigint id PK
        string email UK
        string password_hash
        string name
        boolean active
        timestamp created_at
    }

    ROLES {
        bigint id PK
        string name UK
    }

    USER_ROLES {
        bigint user_id PK, FK
        bigint role_id PK, FK
    }

    PARTNERS {
        bigint id PK
        string name UK
    }

    CLIENTS {
        bigint id PK
        bigint partner_id FK
        string dni UK
        string name
        string phone
        string gender
        date birth_date
        boolean near_location
        boolean promo_friends
        date registration_date
        date contract_start_date
        int contract_period
        boolean group_visit
        boolean active
        tinyint last_prediction_churn
        decimal last_prediction_probability
        timestamp last_prediction_timestamp
    }

    CHARGE_TYPES {
        bigint id PK
        string name UK
    }

    ADDITIONAL_CHARGES {
        bigint id PK
        bigint client_id FK
        bigint charge_type_id FK
        decimal amount
        date charge_date
    }

    ATTENDANCE {
        bigint id PK
        bigint client_id FK
        timestamp checked_in_at
    }

    GROUP_ACTIVITIES {
        bigint id PK
        string name UK
    }

    GROUP_ACTIVITY_ATTENDANCE {
        bigint id PK
        bigint client_id FK
        bigint group_activity_id FK
        timestamp attendance_date
    }

```
-----

### Mapeo de Variables para el Modelo de IA

La siguiente tabla detalla las variables procesadas que el modelo de Machine Learning utiliza para calcular la probabilidad de abandono. Estos datos se derivan dinámicamente de las entidades relacionales definidas anteriormente.

| Variable | Tipo | Descripción |
| :--- | :--- | :--- |
| **idClient** | Integer | Identificador único del cliente. |
| **gender** | Binary | Género del cliente (0 o 1). |
| **nearLocation** | Binary | Indica si el cliente vive o trabaja cerca del centro (1: Sí, 0: No). |
| **partner** | Binary | Indica si el cliente es empleado de una empresa asociada (1: Sí, 0: No). |
| **promoFriends** | Binary | Indica si el cliente se unió mediante la promoción "Trae a un amigo" (1: Sí, 0: No). |
| **phone** | Binary | Indica si el cliente proporcionó su número de teléfono (1: Sí, 0: No). |
| **contractPeriod** | Integer | Duración del contrato actual en meses ({1, 6, 12}). |
| **groupVisits** | Binary | Indica si el cliente participa en sesiones grupales (1: Sí, 0: No). |
| **age** | Integer | Edad del cliente. Min: 18 - Max: 41 |
| **avgAdditionalChargesTotal** | Float | Promedio de gastos adicionales en el centro (cafetería, masajes, etc.) **0.15 a 552.33** |
| **monthToEndContract** | Integer | Meses restantes hasta la finalización del contrato. 1 a 12 |
| **lifetime** | Integer | Tiempo (en meses) desde que el cliente se unió por primera vez. 0 a 31 |
| **avgClassFrequencyTotal** | Float | Frecuencia media de visitas por semana desde el inicio. **0.00 a 6.02** |
| **avgClassFrequencyCurrentMonth** | Float | Frecuencia media de visitas por semana en el último mes.**0.00 a 6.15** |

-----

[Volver al README del proyecto](../README.md)

