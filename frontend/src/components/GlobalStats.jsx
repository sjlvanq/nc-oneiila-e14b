import { useEffect, useState } from 'react';
import api from '@/services/api';
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
        console.log(response);
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

  if (loading) return <p>Cargando estadísticas...</p>;
  if (error) return <p>Error al cargar los datos del servidor.</p>;
  if (!stats) return null;

  return (
    <section className={styles.kpiGrid}>
      <div className={`${styles.kpiCard} ${styles.green}`}>
        <span>Clientes en programa</span>
        <strong>{stats.total}</strong>
      </div>

      <div className={`${styles.kpiCard} ${styles.success}`}>
        <span>Clientes activos</span>
        <strong>{stats.active}</strong>
      </div>

      <div className={`${styles.kpiCard} ${styles.warning}`}>
        <span>Promedio de edad</span>
        <strong>{stats.averageAge}</strong>
      </div>
    </section>
  );
}
