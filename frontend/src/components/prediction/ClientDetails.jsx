import React from "react";
import styles from "../../styles/components/ClientDetails.module.css";

const ClientDetails = ({ client }) => {
  if (!client) return null;

  const probability = client.probability;
  const churnState = probability > 50 ? 1 : 0;

  const riskLevel =
    probability <= 25
      ? "low"
      : probability <= 50
      ? "medium"
      : probability <= 75
      ? "high"
      : "critical";

  return (
    <section className={`${styles["client-details"]} ${styles[riskLevel]}`}>
      <h2 className={styles.title}>Detalles del cliente</h2>

      <div className={styles["detail-row"]}>
        <span>Nombre del cliente</span>
        <span>{client.clientName}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Teléfono</span>
        <span>{client.clientPhone}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Edad</span>
        <span>{client.age}</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Probabilidad de abandono</span>
        <span>{probability}%</span>
      </div>

      <div className={styles["detail-row"]}>
        <span>Estado de abandono</span>
        <span>{churnState === 1 ? "Churn" : "Active"}</span>
      </div>
    </section>
  );
};

export default ClientDetails;
