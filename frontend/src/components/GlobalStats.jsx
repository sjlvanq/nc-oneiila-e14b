import { useEffect, useState } from 'react';
import styles from '@/styles/components/GlobalStats.module.css';

export default function StatCards() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    fetch('URL_DEL_BACKEND')
      .then(res => res.json())
      .then(data => setStats(data));
  }, []);

  // Mientras carga el backend
  if (!stats) {
    return <p>Cargando estadísticas...</p>;
  }

  return (
    <section className={styles.kpiGrid}>
      <div className={`${styles.kpiCard} ${styles.green}`}>
        <span>Clientes en programa</span>
        <strong>{stats.totalClients}</strong>
      </div>

      <div className={`${styles.kpiCard} ${styles.success}`}>
        <span>Clientes activos</span>
        <strong>{stats.activeClients}</strong>
      </div>

      <div className={`${styles.kpiCard} ${styles.warning}`}>
        <span>Promedio de edad</span>
        <strong>{stats.averageAge}</strong>
      </div>
    </section>
  );
}
