import os
import sys
import json
from datetime import datetime, timezone
from typing import Any, Dict, List, Optional

import joblib
import numpy as np
import pandas as pd
import uvicorn
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

# --- LIBRERÍAS ML NECESARIAS ---
from sklearn.base import BaseEstimator, TransformerMixin
import xgboost as xgb

# -----------------------------------------------------------------------------
# 1. DEFINICIÓN DE CLASES CUSTOM (IDÉNTICA AL NOTEBOOK)
# -----------------------------------------------------------------------------
class FeatureEnricher(BaseEstimator, TransformerMixin):
    def fit(self, X, y=None):
        return self
    
    def transform(self, X):
        X_out = X.copy()
        if 'avgClassFrequencyTotal' in X_out.columns and 'avgClassFrequencyCurrentMonth' in X_out.columns:
             X_out['attendance_trend'] = X_out['avgClassFrequencyCurrentMonth'] / (X_out['avgClassFrequencyTotal'] + 1e-5)
        if 'avgClassFrequencyTotal' in X_out.columns and 'lifetime' in X_out.columns:
            X_out['visits_per_lifetime'] = X_out['avgClassFrequencyTotal'] * X_out['lifetime']
        return X_out

# PARCHE PARA QUE JOBLIB ENCUENTRE LA CLASE
if "FeatureEnricher" not in sys.modules["__main__"].__dict__:
    setattr(sys.modules["__main__"], "FeatureEnricher", FeatureEnricher)

# -----------------------------------------------------------------------------
# 2. FUNCIONES UTILITARIAS
# -----------------------------------------------------------------------------
def utc_now_iso() -> str:
    # Devuelve formato ISO 8601 (Ej: 2026-01-04T12:00:00Z)
    return datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")

def format_proba(p: float) -> float:
    # Redondeo a 4 decimales para mantener el JSON limpio
    return float(f"{float(p):.4f}")

def load_metadata(path: str) -> Dict[str, Any]:
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)

def build_required_columns(meta: Dict[str, Any]) -> List[str]:
    if "input_contract" in meta:
        return meta["input_contract"]
    cols = meta.get("required_columns")
    if not cols:
        cols = (meta.get("numeric_features", []) or []) + (meta.get("categorical_features", []) or [])
    seen = set()
    out = []
    for c in cols or []:
        if c not in seen:
            out.append(c)
            seen.add(c)
    return out

def coerce_types(df: pd.DataFrame, meta: Dict[str, Any]) -> pd.DataFrame:
    nums = meta.get("numeric_features", []) or []
    for c in nums:
        if c in df.columns:
            df[c] = pd.to_numeric(df[c], errors="coerce")
    return df

def model_predict_proba(model: Any, X: pd.DataFrame) -> np.ndarray:
    if not hasattr(model, "predict_proba"):
        raise RuntimeError("El modelo no tiene predict_proba().")
    proba = model.predict_proba(X)
    if isinstance(proba, list):
        proba = np.array(proba)
    if proba.ndim == 2 and proba.shape[1] >= 2:
        return proba[:, 1]
    if proba.ndim == 1:
        return proba
    return proba.reshape(-1)

# -----------------------------------------------------------------------------
# 3. CONFIGURACIÓN FASTAPI
# -----------------------------------------------------------------------------
app = FastAPI(title="ChurnInsight Gym API", version="1.0.0")

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.abspath(os.path.join(BASE_DIR, ".."))

MODEL_PATH = os.getenv("MODEL_PATH", os.path.join(PROJECT_ROOT, "model", "churn_pipeline.joblib"))
METADATA_PATH = os.getenv("METADATA_PATH", os.path.join(PROJECT_ROOT, "model", "metadata.json"))
DEFAULT_THRESHOLD = float(os.getenv("THRESHOLD", "0.5"))

model = None
metadata: Dict[str, Any] = {}
required_columns: List[str] = []
stats_state = {"total_evaluated": 0, "total_pred_churn": 0, "last_request_at": None}

# -----------------------------------------------------------------------------
# 4. SCHEMAS PYDANTIC
# -----------------------------------------------------------------------------
class PredictIn(BaseModel):
    data: Dict[str, Any] = Field(..., description="Campos del cliente.")
    threshold: Optional[float] = Field(default=None, ge=0.0, le=1.0)

# ✅ CONTRATO DE SALIDA ESTRICTO
class PredictOut(BaseModel):
    churn: int
    probability: float
    timestamp: str 

class BatchPredictIn(BaseModel):
    data: List[Dict[str, Any]]
    threshold: Optional[float] = None

class BatchPredictOut(BaseModel):
    count: int
    timestamp: str
    results: List[PredictOut]
    summary: Dict[str, Any]

# -----------------------------------------------------------------------------
# 5. STARTUP Y LÓGICA
# -----------------------------------------------------------------------------
@app.on_event("startup")
def startup_event():
    global model, metadata, required_columns, DEFAULT_THRESHOLD
    print(f"Iniciando API... Modelo: {MODEL_PATH}")
    
    if not os.path.exists(MODEL_PATH) or not os.path.exists(METADATA_PATH):
        raise RuntimeError("❌ Archivos de modelo no encontrados.")

    try:
        model = joblib.load(MODEL_PATH)
        print("Modelo cargado.")
    except Exception as e:
        print(f"Error cargando .joblib: {e}")
        raise e

    metadata = load_metadata(METADATA_PATH)
    if "optimal_threshold" in metadata:
        DEFAULT_THRESHOLD = float(metadata["optimal_threshold"])
        print(f"Umbral óptimo aplicado: {DEFAULT_THRESHOLD}")
    
    required_columns = build_required_columns(metadata)

def predict_one(row: Dict[str, Any], threshold: float) -> PredictOut:
    # Validación simple
    missing = [c for c in required_columns if c not in row]
    if missing:
        raise HTTPException(status_code=400, detail={"error": "Faltan campos", "missing": missing})

    X = pd.DataFrame([row])
    X = X[required_columns].copy()
    X = coerce_types(X, metadata)

    try:
        proba = float(model_predict_proba(model, X)[0])
    except Exception as e:
        raise HTTPException(status_code=500, detail={"error": "Inferencia fallida", "msg": str(e)})

    churn_val = 1 if proba >= threshold else 0

    # Stats
    stats_state["total_evaluated"] += 1
    stats_state["total_pred_churn"] += churn_val
    stats_state["last_request_at"] = utc_now_iso()

    # RETORNO STRICTO SEGÚN CONTRATO
    return PredictOut(
        churn=churn_val,
        probability=format_proba(proba),
        timestamp=utc_now_iso() 
    )

# -----------------------------------------------------------------------------
# 6. ENDPOINTS
# -----------------------------------------------------------------------------
@app.get("/health")
def health():
    return {"status": "ok", "model_loaded": model is not None}

# ESTE ES EL ENDPOINT QUE TE FALTABA O NO SE ACTUALIZÓ
@app.get("/metadata")
def get_metadata_route():
    return metadata

@app.get("/stats")
def stats():
    return stats_state

@app.post("/predict", response_model=PredictOut)
def predict_endpoint(payload: PredictIn):
    thr = float(payload.threshold) if payload.threshold is not None else DEFAULT_THRESHOLD
    return predict_one(payload.data, threshold=thr)

@app.post("/batch_predict", response_model=BatchPredictOut)
def batch_predict_endpoint(payload: BatchPredictIn):
    thr = float(payload.threshold) if payload.threshold is not None else DEFAULT_THRESHOLD
    results = []
    probas = []
    for r in payload.data:
        out = predict_one(r, threshold=thr)
        results.append(out)
        probas.append(out.probability)
    
    probas_arr = np.array(probas)
    return BatchPredictOut(
        count=len(results),
        timestamp=utc_now_iso(),
        results=results,
        summary={
            "mean_probability": format_proba(float(np.mean(probas_arr))),
            "churn_count": int(np.sum(probas_arr >= thr))
        }
    )

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
