import { useEffect, useState } from 'react';
import api from '@/services/api';
import Card from '@/components/common/Card';
import styles from '@/styles/components/HighRiskList.module.css';

export default function HighRiskList() {
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const [showToast, setShowToast] = useState(false);

    useEffect(() => {
        const fetchHighRisk = async () => {
            try {
                setLoading(true);
                setShowToast(true);
                const response = await api.get('/clients/high-risk');
                setClients(response.data);
            } catch (err) {
                console.error('Error fetching high risk clients:', err);
                setError(true);
            } finally {
                setLoading(false);
                setTimeout(() => setShowToast(false), 2000);
            }
        };
        fetchHighRisk();
    }, []);

    if (error) return null;

    if (loading) {
        return (
            <>
                {showToast && (
                    <div className={styles.toast}>
                        <span className={styles.toastIcon}>⏳</span>
                        Analizando riesgos...
                    </div>
                )}
                <Card title="Alertas de Abandono Crítico" className={styles.container}>
                    <div className={styles.skeleton}>
                        <div className={styles.skeletonLine}></div>
                        <div className={styles.skeletonLine}></div>
                        <div className={styles.skeletonLine}></div>
                    </div>
                </Card>
            </>
        );
    }

    if (clients.length === 0) return (
        <Card title="Alertas de Abandono (Churn)" className={styles.container}>
            <p className={styles.empty}>No hay clientes en riesgo crítico detectados actualmente.</p>
        </Card>
    );

    return (
        <Card title="Alertas de Abandono Crítico" className={styles.container}>
            <p className={styles.subtitle}>Clientes con probabilidad de abandono mayor al 70%.</p>
            <div className={styles.list}>
                {clients.map(client => (
                    <div key={client.id} className={styles.clientItem}>
                        <div className={styles.clientInfo}>
                            <span className={styles.name}>{client.clientName}</span>
                            <span className={styles.phone}>{client.clientPhone}</span>
                        </div>
                        <div className={styles.riskBadge}>
                            Alto Riesgo
                        </div>
                    </div>
                ))}
            </div>
            <div className={styles.footer}>
                <small>* Basado en comportamiento de asistencia y pagos.</small>
            </div>
        </Card>
    );
}
