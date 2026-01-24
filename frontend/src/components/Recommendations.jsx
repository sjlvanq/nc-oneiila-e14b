import React from "react";
import styles from "@/styles/components/Recommendations.module.css";
import { getRiskLevel, getRiskTitle, isChurn } from "@/utils/riskUtils";
import Card from "@/components/common/Card";

const Recommendations = ({ probability }) => {
  if (probability === undefined || probability === null) return null;

  const riskLevel = getRiskLevel(probability);
  const riskTitle = getRiskTitle(probability);
  const isChurnClient = isChurn(probability);

  let strategy = "";
  let action = "";
  let tactic = "";

  switch (riskLevel) {
    case 'low':
      strategy = "Fidelización y venta cruzada";
      action = "No saturar al cliente con descuentos. Ofrecer recompensas por lealtad, como beneficios por referir amigos.";
      tactic = "Promocionar servicios premium como SPA, cafetería o merchandising.";
      break;
    case 'medium':
      strategy = "Engagement preventivo";
      action = "Fomentar el hábito cuando la asistencia del cliente comienza a disminuir.";
      tactic = "Invitar al cliente a clases grupales para aumentar la interacción social.";
      break;
    case 'high':
      strategy = "Intervención activa";
      action = "Enviar una oferta personalizada para la renovación anticipada del contrato.";
      tactic = "Ofrecer una sesión gratuita con un entrenador personal para reenganchar al cliente.";
      break;
    case 'critical':
      strategy = "Retención de emergencia";
      action = "Contactar al cliente con una oferta urgente y agresiva válida por 48 horas.";
      tactic = "Ofrecer la opción de congelar la membresía o realizar una breve encuesta de salida.";
      break;
    default:
      break;
  }

  return (
    <Card title="Recomendaciones" riskLevel={riskLevel}>
      <div className={styles.probabilityBadge}>
        <div className={styles.probabilityCircle}>
          <span className={styles.probabilityValue}>{Math.round(probability * 100)}%</span>
          <span className={styles.probabilityLabel}>Riesgo</span>
        </div>
        <div className={styles.riskInfo}>
          <span className={styles.riskTitle}>{riskTitle}</span>
          <span className={styles.riskStatus}>{isChurnClient ? "Churn" : "Active"}</span>
        </div>
      </div>

      <div className={styles.detailRow}>
        <span>Estrategia</span>
        <span>{strategy}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Acción recomendada</span>
        <span>{action}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Táctica</span>
        <span>{tactic}</span>
      </div>
    </Card>
  );
};

export default Recommendations;

