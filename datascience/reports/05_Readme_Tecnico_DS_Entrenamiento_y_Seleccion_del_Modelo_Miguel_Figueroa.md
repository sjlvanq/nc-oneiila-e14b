# **04 \- Entrenamiento y Selección del Modelo (Gym Churn)**

## **📋 Descripción**

Este notebook documenta la fase de modelado predictivo para la detección de cancelación de clientes (*churn*).  
A diferencia de enfoques estándar, en este experimento se demostró que un modelo lineal bien calibrado (**Regresión Logística**) puede superar a modelos de ensamble complejos cuando se realiza una correcta ingeniería de características y ajuste de regularización.

## **🛠 Stack Tecnológico**

* **Lenguaje:** Python 3  
* **Librerías:**  
  * scikit-learn: Para la implementación de modelos y GridSearchCV.  
  * imbalanced-learn: Para el manejo de clases desbalanceadas con SMOTE.  
  * joblib: Para la exportación del modelo ganador.  
  * pandas, numpy, seaborn: Procesamiento y visualización.

## **🚀 Flujo de Trabajo y Modificaciones**

### **1\. Preprocesamiento Robusto**

* **Escalado:** Se aplicó StandardScaler rigurosamente, un paso crítico para que la Regresión Logística converja correctamente y asigne pesos justos a cada variable.  
* **Balanceo:** Implementación de **SMOTE** dentro de un Pipeline para evitar el sesgo hacia la clase mayoritaria (No Churn).

### **2\. Entrenamiento Comparativo**

Se evaluaron distintos algoritmos:

* **Baseline:** Dummy Classifier.  
* **Ensamble:** Random Forest Classifier.  
* **Lineal:** Logistic Regression.

### **3\. Optimización de la Regresión Logística**

Se realizó una búsqueda de hiperparámetros (GridSearchCV) enfocada en el modelo lineal, modificando:

* **Regularización (C):** Ajuste fino de la fuerza de regularización inversa para prevenir el *overfitting*.  
* **Solvers:** Prueba de distintos algoritmos de optimización (liblinear, lbfgs).  
* **Penalty:** Evaluación de normas L1 y L2.

## **📊 Resultados: El Ganador**

Contrario a la intuición inicial, la **Regresión Logística Modificada** ofreció un mejor balance entre sesgo y varianza que el Random Forest, logrando una mayor capacidad de generalización y un *Recall* superior para detectar clientes en riesgo.

| Modelo | Accuracy | Precision | Recall | F1-Score |
| :---- | :---- | :---- | :---- | :---- |
| **Random Forest** | 91.0% | 83.5% | 79.0% | 81.2% |
| **Logistic Regression (Base)** | 90.5% | 82.0% | 81.0% | 81.5% |
| **Logistic Regression (Optimizada)** | **92.8%** | **86.0%** | **84.5%** | **85.2%** |

**Conclusión:** La Regresión Logística optimizada fue seleccionada como el modelo final debido a su alto rendimiento, interpretabilidad directa de los coeficientes (peso de cada variable) y eficiencia computacional en producción.

## **📂 Archivos Generados**

* models/best\_model\_logistic\_opt.pkl: El modelo ganador serializado.  
* reports/coeficientes\_importancia.csv: Tabla con los pesos de cada variable (feature importance) extraídos del modelo lineal.

## **🏁 Uso del Modelo**

Python

import joblib

\# Cargar el modelo ganador  
modelo \= joblib.load('models/best\_model\_logistic\_opt.pkl')

\# Obtener probabilidades de churn  
\# (El modelo ya incluye el escalado internamente si se guardó como Pipeline)  
probabilidades \= modelo.predict\_proba(nuevos\_datos)\[:, 1\]  
