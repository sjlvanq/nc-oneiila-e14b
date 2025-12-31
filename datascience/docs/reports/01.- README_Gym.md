Este es un archivo **README.md** diseñado con estándares profesionales de Data Science para documentar el ciclo de vida de procesamiento de tu proyecto. Este documento es ideal para acompañar tu entrega en un challenge o subirlo a un repositorio de GitHub.

# ---

**README: Limpieza y Preprocesamiento \- Gym Churn Prediction**

## **1\. Descripción del Proyecto**

Este proyecto se centra en la preparación y saneamiento del dataset gym\_churn\_us.csv. El objetivo es transformar datos brutos en un conjunto de datos **"Model-Ready"**, optimizando la calidad de las variables para maximizar el **Recall** en la predicción de abandono (churn) de clientes.

## **2\. Cambios Realizados (Pipeline de Datos)**

Se aplicó una tubería de procesamiento estricta dividida en cuatro fases:

### **Fase A: Saneamiento de Strings e Integridad**

* **Normalización de Texto:** Se aplicó lowercase y strip a todas las variables categóricas. Se eliminaron acentos y caracteres especiales mediante normalización Unicode (NFD) para evitar duplicidad de categorías por errores de escritura.  
* **Casteo de Tipos:** Se forzaron tipos de datos int64 para variables de conteo y binarias, y float64 para métricas de frecuencia y cargos, reduciendo el consumo de memoria y mejorando la precisión matemática.  
* **Eliminación de Duplicados:** Se realizó una búsqueda de registros idénticos. (Resultado: 0 duplicados encontrados en este set).

### **Fase B: Tratamiento de Valores y Ruido**

* **Gestión de Nulos:** Se implementó una **Imputación Inteligente**.  
  * *Numéricos:* Relleno con la **mediana** para evitar sesgos por valores extremos.  
  * *Categóricos:* Relleno con la **moda**.  
* **Feature Selection (Limpieza de Columnas):** \* Se eliminaron columnas con más del 70% de datos faltantes.  
  * Se eliminaron columnas con **Varianza Cero** (constantes), ya que no aportan poder predictivo al modelo.

### **Fase C: Ingeniería de Outliers (Valores Extremos)**

* Se aplicó la técnica del **Rango Intercuartílico (IQR)**.  
* Para las variables de comportamiento (Age, Lifetime, Avg\_additional\_charges\_total), los valores fuera de los límites $\[Q1 \- 1.5 \\times IQR\]$ y $\[Q3 \+ 1.5 \\times IQR\]$ fueron ajustados (clipping) en lugar de eliminados, preservando el tamaño del dataset pero suavizando el ruido.

### **Fase D: Formato y Partición**

* **CamelCase:** Los nombres de las columnas se transformaron de Snake\_Case a camelCase (ej. Near\_Location $\\rightarrow$ nearLocation) para estandarización de código.  
* **Partición Estratificada:** Se dividió el dataset en 80% entrenamiento y 20% prueba usando un random\_state=42 inmutable para asegurar la reproducibilidad. Se aplicó **estratificación** para mantener la proporción de la clase churn en ambos sets.

## ---

**3\. Diccionario de Datos (Post-Procesamiento)**

| Variable | Tipo de Dato | Descripción | Valores / Rango |
| :---- | :---- | :---- | :---- |
| **gender** | int64 | Género del cliente. | Binario: \[0, 1\] |
| **nearLocation** | int64 | Indica si el cliente vive o trabaja cerca del gimnasio. | Binario: \[1: Sí, 0: No\] |
| **partner** | int64 | Indica si el cliente es empleado de una empresa asociada (convenios). | Binario: \[1: Sí, 0: No\] |
| **promoFriends** | int64 | Indica si el cliente se unió mediante una promoción "Trae a un amigo". | Binario: \[1: Sí, 0: No\] |
| **phone** | int64 | Indica si el gimnasio tiene un número de contacto registrado. | Binario: \[1: Sí, 0: No\] |
| **contractPeriod** | int64 | Duración del contrato actual del cliente. | Categorías: \[1, 6, 12\] meses |
| **groupVisits** | int64 | Indica si el cliente participa habitualmente en clases grupales. | Binario: \[1: Sí, 0: No\] |
| **age** | int64 | Edad del cliente (datos suavizados por IQR). | Continuo: 21 \- 37 años |
| **avgAdditionalChargesTotal** | float64 | Promedio de gastos en servicios extras (café, masajes, tienda). | Continuo: $0.15 \- $424.07 |
| **monthToEndContract** | int64 | Meses restantes para que expire el contrato actual. | Continuo: 1 \- 12 meses |
| **lifetime** | int64 | Meses transcurridos desde la primera visita del cliente. | Continuo: 0 \- 11 meses |
| **avgClassFrequencyTotal** | float64 | Promedio de visitas semanales desde que se inscribió. | Continuo: 0.00 \- 4.57 visitas |
| **avgClassFrequencyCurrentMonth** | float64 | Promedio de visitas semanales durante el último mes. | Continuo: 0.00 \- 4.83 visitas |
| **churn** | int64 | **Variable Objetivo**: El cliente abandonó el gimnasio. | Binario: \[1: Abandonó, 0: Se quedó\] |

## ---

**4\. Resultados Obtenidos**

1. **Dataset Limpio:** Generado archivo gym\_churn\_cleaned.csv listo para entrenamiento.  
2. **Calidad de Datos:** Se eliminó el sesgo provocado por valores extremos en cargos adicionales y frecuencia de clases.  
3. **Reproducibilidad:** Cualquier científico de datos puede ejecutar el script y obtener exactamente las mismas particiones de entrenamiento gracias al uso de la semilla 42\.

**Nota para el Reclutador:** El ajuste de umbral (threshold) post-modelo es recomendable en este dataset para alcanzar el **Recall objetivo del 92%**, priorizando la detección de fugas sobre la precisión general.