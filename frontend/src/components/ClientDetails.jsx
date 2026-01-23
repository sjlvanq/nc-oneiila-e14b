import styles from "@/styles/components/ClientDetails.module.css";

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
    <section className={`${styles.clientDetails} ${styles[riskLevel]}`}>
      <h2>Detalles del cliente</h2>

      <div className={styles.detailRow}>
       <span>Nombre del cliente</span>
        <span>{client.clientName}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Teléfono</span>
        <span>{client.clientPhone}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Edad</span>
        <span>{client.age}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Probabilidad de abandono</span>
        <span>{probability}%</span>
      </div>

      <div className={styles.detailRow}>
        <span>Estado de abandono</span>
        <span>{churnState === 1 ? "Churn" : "Active"}</span>
      </div>
    </section>
  );
};

export default ClientDetails;
