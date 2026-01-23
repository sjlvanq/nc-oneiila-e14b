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
  const pieLabels = breakdown.map(b => `${b.type}: $${b.amount}`);
  const pieValues = breakdown.map(b => b.percentage);

  const hasPie = breakdown.length > 0;

  const barData = {
    labels: monthlyLabels,
    datasets: [
      {
        label: 'Asistencias mensuales',
        data: monthlyValues,
        backgroundColor: 'rgba(59, 130, 246, 0.8)',
        borderColor: 'rgba(59, 130, 246, 1)',
        borderWidth: 2,
        borderRadius: 8,
        hoverBackgroundColor: 'rgba(59, 130, 246, 1)',
      }
    ]
  };
  
  const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false
      },
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
          font: {
            size: 12,
            weight: '500'
          }
        },
        grid: {
          display: false
        },
        ticks: {
          color: '#6b7280'
        }
      },
      y: {
        title: {
          display: true,
          text: 'Número de asistencias',
          color: '#6b7280',
          font: {
            size: 12,
            weight: '500'
          }
        },
        beginAtZero: true,
        ticks: {
          stepSize: 1,
          color: '#6b7280'
        },
        grid: {
          color: 'rgba(229, 231, 235, 0.5)',
          drawBorder: false
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

  const pieOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#374151',
          font: {
            size: 12
          },
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