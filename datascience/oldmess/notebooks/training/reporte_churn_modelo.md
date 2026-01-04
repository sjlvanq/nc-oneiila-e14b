
# Reporte Técnico – Modelo de Predicción de Churn

## Contexto del Dataset
- Registros: 4000  
- Variables predictoras: 14  
- Variable objetivo: churn  

Distribución:
- No Churn (0): 73.47%
- Churn (1): 26.53%

## Split de Datos
- 80/20 estratificado (random_state=42)
- Proporción de churn conservada en Train y Test
- Test set completamente virgen

## Modelo Baseline – DummyClassifier

**Matriz de Confusión**
[[588, 0],
 [212, 0]]

**Métricas**
| Métrica | Valor |
|-------|-------|
| Accuracy | 0.735 |
| Precision | 0.000 |
| Recall | 0.000 |
| F1 | 0.000 |

Conclusión: no detecta churn.

## Balanceo de Clases
- SMOTE aplicado solo al Train
- Distribución balanceada: 2351 / 2351

## Modelo Entrenado – Logistic Regression

**Matriz de Confusión**
[[558, 30],
 [27, 185]]

**Métricas (Test Set)**
| Métrica | Valor |
|-------|-------|
| Accuracy | 0.92875 |
| Precision | 0.86047 |
| Recall | 0.87264 |
| F1 | 0.86651 |

## Validación Cruzada (5-Fold)
- Recall medio: ~0.91
- Desviación estándar baja (~0.02)

## Comparación Dummy vs Modelo

| Modelo | Accuracy | Precision | Recall | F1 |
|------|---------|----------|-------|----|
| Dummy | 0.735 | 0.000 | 0.000 | 0.000 |
| Logistic Regression | 0.92875 | 0.86047 | 0.87264 | 0.86651 |

## Smoke Test

Predicción individual:
- Clase: No Churn
- Probabilidad de churn: 0.0031

## Checklist Cumplido
✔ Split estratificado  
✔ Dummy + matriz  
✔ Balanceo solo train  
✔ Modelo entrenado  
✔ Recall > 80%  
✔ Test virgen  
✔ Validación cruzada  
✔ Reporte comparativo  
✔ Serialización (.joblib)  
✔ Smoke tests  

## Estado Final
Modelo listo para optimización, tuning, feature selection y despliegue en API.
