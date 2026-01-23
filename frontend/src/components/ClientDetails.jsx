import styles from "@/styles/components/ClientDetails.module.css";
import { getRiskLevel, isChurn, formatProbability } from "@/utils/riskUtils";
import Card from "@/components/common/Card";

const ClientDetails = ({ client }) => {
  if (!client) return null;

  const riskLevel = getRiskLevel(client.probability);
  const isChurnClient = isChurn(client.probability);

  return (
    <Card title="Detalles del cliente" riskLevel={riskLevel}>
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
        <span>{formatProbability(client.probability)}</span>
      </div>

      <div className={styles.detailRow}>
        <span>Estado de abandono</span>
        <span>{isChurnClient ? "Churn" : "Active"}</span>
      </div>
    </Card>
  );
};

export default ClientDetails;
