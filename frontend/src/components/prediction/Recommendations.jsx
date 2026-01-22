import React from "react";
import styles from "../../styles/components/Recommendations.module.css";

const Recommendations = ({ probability }) => {
  if (probability === undefined || probability === null) return null;

  let riskTitle = "";
  let strategy = "";
  let action = "";
  let tactic = "";
  let riskLevel = "";

  if (probability <= 25) {
    riskLevel = "low";
    riskTitle = "Riesgo bajo (0% - 25%)";
    strategy = "Fidelización y venta cruzada";
    action =
      "No saturar al cliente con descuentos. Ofrecer recompensas por lealtad, como beneficios por referir amigos.";
    tactic =
      "Promocionar servicios premium como SPA, cafetería o merchandising.";
  } else if (probability <= 50) {
    riskLevel = "medium";
    riskTitle = "Riesgo medio (26% - 50%)";
    strategy = "Engagement preventivo";
    action =
      "Fomentar el hábito cuando la asistencia del cliente comienza a disminuir.";
    tactic =
      "Invitar al cliente a clases grupales para aumentar la interacción social.";
  } else if (probability <= 75) {
    riskLevel = "high";
    riskTitle = "Riesgo alto (51% - 75%)";
    strategy = "Intervención activa";
    action =
      "Enviar una oferta personalizada para la renovación anticipada del contrato.";
    tactic =
      "Ofrecer una sesión gratuita con un entrenador personal para reenganchar al cliente.";
  } else {
    riskLevel = "critical";
    riskTitle = "Riesgo crítico (76% - 100%)";
    strategy = "Retención de emergencia";
    action =
      "Contactar al cliente con una oferta urgente y agresiva válida por 48 horas.";
    tactic =
      "Ofrecer la opción de congelar la membresía o realizar una breve encuesta de salida.";
  }

  return (
    <section className={`${styles.recommendations} ${styles[riskLevel]}`}>
      <h2 className={styles.title}>Recomendaciones</h2>

      <div className={styles["detail-row"]}>
        <span>Nivel de riesgo</span>
        <span>{riskTitle}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Estado de abandono</span>
        <span>{probability > 50 ? "Churn" : "Active"}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Estrategia</span>
        <span>{strategy}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Acción recomendada</span>
        <span>{action}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Táctica</span>
        <span>{tactic}</span>
      </div>
    </section>
  );
};

export default Recommendations;

