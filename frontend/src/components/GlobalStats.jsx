import { useEffect, useState } from 'react';
import api from '@/services/api';
import Card from '@/components/common/Card';
import styles from '@/styles/components/GlobalStats.module.css';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from "chart.js";
import { Doughnut } from "react-chartjs-2";

ChartJS.register(ArcElement, Tooltip, Legend);

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

  const chartData = stats ? {
    labels: ['Activos', 'Inactivos'],
    datasets: [
      {
        data: [stats.active, stats.inactive || 0],
        backgroundColor: [
          'rgba(34, 197, 94, 0.8)',
          'rgba(239, 68, 68, 0.8)',
        ],
        borderColor: [
          'rgba(34, 197, 94, 1)',
          'rgba(239, 68, 68, 1)',
        ],
        borderWidth: 2,
      },
    ],
  } : null;

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          usePointStyle: true,
          padding: 20,
          font: { size: 12 }
        }
      }
    },
    cutout: '70%'
  };

  if (loading) return <div className={styles.loading}>Cargando estadísticas...</div>;
  if (error) return <div className={styles.error}>Error al cargar los datos del servidor.</div>;
  if (!stats) return null;

  return (
    <div className={styles.statsWrapper}>
      <section className={styles.gStatsGrid}>
        <Card title="Estado de Clientes" className={styles.chartArea}>
          <div className={styles.chartContainer}>
            <Doughnut data={chartData} options={chartOptions} />
            <div className={styles.chartCenter}>
              <span className={styles.centerValue}>{stats.total}</span>
              <span className={styles.centerLabel}>Total</span>
            </div>
          </div>
        </Card>

        <div className={styles.kpiValues}>
          <Card title="Clientes Activos" className={styles.kpiCard}>
            <strong className={`${styles.statValue} ${styles.activeColor}`}>{stats.active}</strong>
          </Card>

          <Card title="Clientes en Riesgo" className={styles.kpiCard}>
            <strong className={`${styles.statValue} ${styles.riskColor}`}>{stats.highRiskCount}</strong>
          </Card>

          <Card title="Promedio de Edad" className={styles.kpiCard}>
            <strong className={styles.statValue}>{Math.round(stats.averageAge)}</strong>
            <span className={styles.unit}>años</span>
          </Card>
        </div>
      </section>
    </div>
  );
}
