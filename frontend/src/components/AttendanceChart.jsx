import { useEffect, useState, useMemo } from 'react';
import api from '@/services/api';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  LineElement,
  PointElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  BarElement
} from "chart.js";
import { Bar, Pie } from "react-chartjs-2";
import Card from '@/components/common/Card';
import styles from '@/styles/components/AttendanceChart.module.css';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
  BarElement
);

export default function AttendanceChart({ clientId }) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!clientId) return;

    const fetchStats = async () => {
      try {
        setLoading(true);
        setError('');
        setData(null);

        const response = await api.get(`/clients/clients/statistics/${clientId}`);
        setData(response.data);

      } catch (err) {
        setError('Error al obtener estadísticas');
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [clientId]);

  const barData = useMemo(() => {
    if (!data) return null;
    const monthly = data.monthlyAttendanceLastSixMonths || {};
    return {
      labels: Object.keys(monthly),
      datasets: [
        {
          label: 'Asistencias mensuales',
          data: Object.values(monthly),
          backgroundColor: 'rgba(59, 130, 246, 0.8)',
          borderColor: 'rgba(59, 130, 246, 1)',
          borderWidth: 2,
          borderRadius: 8,
          hoverBackgroundColor: 'rgba(59, 130, 246, 1)',
        }
      ]
    };
  }, [data]);

  const barOptions = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: 'rgba(10, 20, 48, 0.9)',
        titleColor: '#ffffff',
        bodyColor: '#ffffff',
        borderColor: 'rgba(59, 130, 246, 0.5)',
        borderWidth: 1,
        padding: 12,
        cornerRadius: 8,
        displayColors: false
      }
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Mes',
          color: '#6b7280',
          font: { size: 12, weight: '500' }
        },
        grid: { display: false },
        ticks: { color: '#6b7280' }
      },
      y: {
        title: {
          display: true,
          text: 'Número de asistencias',
          color: '#6b7280',
          font: { size: 12, weight: '500' }
        },
        beginAtZero: true,
        ticks: { stepSize: 1, color: '#6b7280' },
        grid: { color: 'rgba(229, 231, 235, 0.5)', drawBorder: false }
      }
    }
  }), []);

  const pieData = useMemo(() => {
    if (!data?.additionalChargesByCat?.breakdown) return null;
    const breakdown = data.additionalChargesByCat.breakdown;
    return {
      labels: breakdown.map(b => `${b.type}: $${b.amount}`),
      datasets: [
        {
          data: breakdown.map(b => b.percentage),
          backgroundColor: [
            'rgba(59, 130, 246, 0.8)',
            'rgba(34, 197, 94, 0.8)',
            'rgba(245, 158, 11, 0.8)',
            'rgba(239, 68, 68, 0.8)',
            'rgba(139, 92, 246, 0.8)'
          ],
          borderColor: [
            'rgba(59, 130, 246, 1)',
            'rgba(34, 197, 94, 1)',
            'rgba(245, 158, 11, 1)',
            'rgba(239, 68, 68, 1)',
            'rgba(139, 92, 246, 1)'
          ],
          borderWidth: 2,
          hoverOffset: 8
        }
      ]
    };
  }, [data]);

  const pieOptions = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#374151',
          font: { size: 12 },
          padding: 15,
          usePointStyle: true,
          pointStyle: 'circle'
        }
      },
      tooltip: {
        backgroundColor: 'rgba(10, 20, 48, 0.9)',
        titleColor: '#ffffff',
        bodyColor: '#ffffff',
        borderColor: 'rgba(59, 130, 246, 0.5)',
        borderWidth: 1,
        padding: 12,
        cornerRadius: 8
      }
    }
  }), []);

  if (loading) return <div className={styles.loading}>Cargando estadísticas...</div>;
  if (error) return <div className={styles.error}>{error}</div>;
  if (!data) return null;

  return (
    <>
      <Card title="Asistencia mensual" className={styles.chartCard}>
        <div className={styles.chartWrapper}>
          {barData ? <Bar data={barData} options={barOptions} /> : <p className={styles.empty}>Sin datos de asistencia</p>}
        </div>
      </Card>

      <Card title="Gastos por categoría" className={styles.chartCard}>
        <div className={styles.chartWrapper}>
          {pieData ? <Pie data={pieData} options={pieOptions} /> : <p className={styles.empty}>Sin cargos adicionales</p>}
        </div>
      </Card>
    </>
  );
}