# Diccionario de Datos - Integración Backend

Este documento describe las variables del dataset entregado para las pruebas de integración. Todas las variables han sido preprocesadas para su consumo directo.

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
| **age** | Integer | Edad del cliente. | Min: 18 | Max: 41 |
| **avgAdditionalChargesTotal** | Float | Promedio de gastos adicionales en el centro (cafetería, masajes, etc.). | **0.15 a 552.33** |
| **monthToEndContract** | Integer | Meses restantes hasta la finalización del contrato. |1 a 12 |
| **lifetime** | Integer | Tiempo (en meses) desde que el cliente se unió por primera vez. |0 a 31 |
| **avgClassFrequencyTotal** | Float | Frecuencia media de visitas por semana desde el inicio. | **0.00 a 6.02** |
| **avgClassFrequencyCurrentMonth** | Float | Frecuencia media de visitas por semana en el último mes. | **0.00 a 6.15** |

---
**Nota:** El archivo `base_datos_back.csv` ha sido extraído mediante un muestreo estratificado para garantizar la representatividad de los datos en las pruebas.

---
**Instrucción para Backend:** Cualquier valor fuera de estos rangos en las variables `Float` debe ser validado o notificado, ya que se consideran valores atípicos (outliers) para este modelo específico.