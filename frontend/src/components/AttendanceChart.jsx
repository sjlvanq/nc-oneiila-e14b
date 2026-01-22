import { useEffect, useState } from 'react';
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
        setError('Error al alcanzar estadisticas');
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [clientId]);

  if (loading) return null;
  if (error) return <p>{error}</p>;
  if (!data) return null;

  const monthly = data.monthlyAttendanceLastSixMonths || {};
  const monthlyLabels = Object.keys(monthly); 
  const monthlyValues = Object.values(monthly);
  
  const hasMonthly = monthlyLabels.length > 0;

  const breakdown = data.additionalChargesByCat?.breakdown || [];
  const pieLabels = breakdown.map(b => b.type);
  const pieValues = breakdown.map(b => b.percentage);

  const hasPie = breakdown.length > 0;

  const barData = {
    labels: monthlyLabels,
    datasets: [
      {
        label: 'Asistencias mensuales',
        data: monthlyValues,
        backgroundColor: 'rgba(59, 130, 246, 0.7)',
        borderColor: 'rgba(59, 130, 246, 1)',
        borderWidth: 1,
        borderRadius: 6, 
      }
    ]
  };
  
  const barOptions = {
    responsive: true,
    plugins: {
      legend: {
        display: false
      }
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Mes'
        },
        grid: {
          display: false
        }
      },
      y: {
        title: {
          display: true,
          text: 'Número de asistencias'
        },
        beginAtZero: true,
        ticks: {
          stepSize: 1
        }
      }
    }
  };

  const pieData = {
    labels: pieLabels,
    datasets: [
      {
        data: pieValues,
        backgroundColor: [
          '#22c55e',
          '#f59e0b',
          '#ef4444',
          '#8b5cf6',
          '#06b6d4'
        ]
      }
    ]
  };

  const pieOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.boxesContainer}>
        {hasMonthly ? (
          <div className={styles.chartBox}>
            <div className={styles.title}>Asistencia mensual</div>
            <Bar data={barData} options={barOptions} />
          </div>
        ):(
          <p>No ha tenido registro mensual</p>
        )}
        {hasPie ? (
            <div className={styles.chartBox}>
              <div className={styles.title}>Gastos por categoría</div>
              <Pie data={pieData} options={pieOptions} />
            </div>
          ) : (
            <p>No tiene cargos adicionales.</p>
          )}
      </div>
    </div>
  );
}