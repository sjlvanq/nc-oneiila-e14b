
![ORACLE + Alura Latam](../../docs/assets/oraclealura-logo.svg)
## Hackathon Oracle Next Education II - Latam

***Desafío intensivo de innovación para participantes de todo Latam.***

powered by ![NoCountry](../../docs/assets/nocountry-logo.png)

----

# **Renombrar columnas a camelCase**

## **📋 Descripción**

Este notebook corresponde a una etapa de estandarización en el pipeline de ingeniería de datos. Su objetivo principal es normalizar los nombres de las columnas del dataset de retención de clientes (gym churn), convirtiéndolos al formato **camelCase**.  
Mantener una convención de nombres consistente facilita la manipulación de atributos en etapas posteriores de análisis exploratorio (EDA) y modelado predictivo.

## **🛠 Dependencias**

Para ejecutar este notebook, se requieren las siguientes librerías de Python:

* pandas: Para la manipulación del DataFrame y lectura/escritura de CSV.  
* re: Para el uso de expresiones regulares en la transformación de textos.

## **🚀 Flujo de Trabajo**

1. **Carga de Datos:** Lee el dataset procesado en la etapa anterior (gym\_churn\_cleaned\_2.csv).  
2. **Transformación:** Define e implementa la función a\_camel\_case que realiza la limpieza de los nombres de las columnas:  
   * Detecta si el nombre ya está en formato correcto.  
   * Reemplaza guiones bajos (\_) y guiones (-) por espacios.  
   * Elimina caracteres especiales no alfanuméricos.  
   * Aplica la capitalización adecuada (primera palabra en minúscula, siguientes capitalizadas).  
3. **Exportación:** Guarda el resultado final en un nuevo archivo CSV listo para el siguiente proceso.

## **📂 Entradas y Salidas**

| Tipo | Archivo | Descripción |
| :---- | :---- | :---- |
| **Input** | data/processed/gym\_churn\_cleaned\_2.csv | Dataset resultante de la limpieza previa. |
| **Output** | data/processed/gym\_churn\_camelcase.csv | Dataset con nombres de columnas estandarizados. |

## **🧩 Función Clave**

La lógica principal reside en la función de transformación de texto:

Python

def a\_camel\_case(columna):  
    \# Lógica para convertir strings a formato camelCase  
    \# usando expresiones regulares para limpieza  
    ...

---

**Nota:** Este script asegura que independientemente del origen de los datos (SQL, CSVs con diferentes formatos), las variables siempre tengan nombres predecibles en el código (ej. contractPeriod en lugar de contract\_period o Contract Period).

### 