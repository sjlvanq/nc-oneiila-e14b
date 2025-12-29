# Sprint 2
Inicio: 2025-12-29 | Fin: 2026-12-02 

## Resumen

Este sprint se centra en la integración del núcleo analítico del sistema mediante la conexión de nuestra API con el microservicio externo de predicción de *churn*. El objetivo es habilitar un flujo de datos que permita recuperar la información de un cliente desde la base de datos (mediante su DNI), enviarla a través de un `RestClient` hacia el modelo de IA y procesar la respuesta JSON para obtener la probabilidad de abandono. Las tareas se distribuirán entre el desarrollo del cliente HTTP, la lógica de transformación de datos y la exposición de nuevos endpoints validados.

### Flujo de Ejecución

1. **Request:** El cliente (frontend/Postman) envía un `GET` a `/clients/prediction/{dni}`.
2. **Controller:** `ClientController` recibe el DNI y llama al `ChurnService`.
3. **Service:** * Solicita al `ClientRepository` los datos del cliente.
* Mapea los datos del cliente al DTO que espera el microservicio.
* Llama al `PredictionClient` (Infra).

4. **Client (Infra):** Realiza el `POST` al microservicio y recibe el JSON de predicción.
5. **Service:** Recibe la respuesta, puede guardarla en un log o base de datos si es necesario, y devuelve un DTO unificado al controlador.
6. **Response:** El controlador responde al usuario con un `200 OK` y el resultado.

## Distribución de responsabilidades

| Desarrollador | Responsabilidad Principal | Tareas Específicas |
| :--- | :--- | :--- |
| **devA (Infra & Integration)** | Cliente Externo | Configurar RestClient, crear el cliente que consume el microservicio y manejar errores de conexión. |
| **devB (Business Logic)** | Capa de Servicio | Crear el ChurnService, lógica de orquestación (buscar en DB -> llamar cliente -> procesar respuesta) y DTOs internos. |
| **devC (API & Persistence)** | Endpoints y Datos | Adaptar el ClientRepository para búsquedas por DNI, crear el endpoint en ClientController y validaciones. |

## Nuevos archivos de código
```
├───java
│   └───com
│       └───churncheck
│           └───api
│               ├───controller
│               │   └─── ClientController.java          [* devC] -> Nuevo endpoint GET /prediction/{dni}
│               ├───domain
│               │   ├───client
│               │   │   ├─── Client.java                [  ]
│               │   │   ├─── ClientRepository.java      [* devC] -> Query: findByDni(String dni)
│               │   │   └─── dto                        [+ devB] -> Carpeta para organizar DTOs
│               │   │        ├─── PredictionResponseDTO.java [+ devB] -> (prevision, probabilidad)
│               │   │        └─── ClientFullResponseDTO.java [+ devB] -> Datos cliente + predicción
│               │   └───user
│               ├───service                             [+ devB] -> NUEVA CAPA
│               │   └─── ChurnService.java              [+ devB] -> Orquestador de la lógica
│               └───infra
│                   ├───clients                         [+ devA] -> NUEVA CAPA (Consumo externo)
│                   │   ├─── PredictionClient.java      [+ devA] -> Interfaz/Clase RestClient
│                   │   └─── dto                        [+ devA] -> DTOs específicos del microservicio
│                   │        └─── PredictionRequestDTO.java [+ devA] -> Lo que el microservicio recibe
│                   ├───security
│                   └───springdoc
└───resources
    └─── application.yaml                               [* devA] -> URL del microservicio
```
