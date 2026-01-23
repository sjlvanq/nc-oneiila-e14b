import { useEffect, useState } from 'react';
import api from '@/services/api';
import Card from '@/components/common/Card';
import styles from '@/styles/components/GlobalStats.module.css';

export default function GlobalStats() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setError(false);
        const response = await api.get(`/api/stats`);
        setStats(response.data);
      } catch (err) {
        console.error(err);
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) return <div className={styles.loading}>Cargando estadísticas...</div>;
  if (error) return <div className={styles.error}>Error al cargar los datos del servidor.</div>;
  if (!stats) return null;

  return (
    <section className={styles.gStatsGrid}>
      <Card title="Clientes en programa" className={styles.clientsTotal}>
        <strong className={styles.statValue}>{stats.total}</strong>
      </Card>

      <Card title="Clientes activos" className={styles.clientsActive}>
        <strong className={styles.statValue}>{stats.active}</strong>
      </Card>

      <Card title="Promedio de edad" className={styles.averageAge}>
        <strong className={styles.statValue}>{Math.round(stats.averageAge)}</strong>
      </Card>
    </section>
  );
}
