import { Link } from 'react-router-dom';
import styles from '@/styles/pages/Home.module.css';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';

export default function Inicio() {
    useDocumentTitle('Inicio');
    return (
        <div>
            {/* Hero Section */}
            <section className={styles.hero}>
                <div className={styles.heroContent}>
                    <h1>Analítica predictiva para gimnasios</h1>
                    <p>Clientes, riesgo y retención en un solo lugar</p>
                    <div className={styles.heroButtons}>
                        <Link to="/login" className={`${styles.btn} ${styles.btnPrimary}`}>Iniciar sesión</Link>
                        <Link to="/register" className={`${styles.btn} ${styles.btnSecondary}`}>Registrarse</Link>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section className={styles.features}>
                <div className={styles.container}>
                    <h2 className={styles.sectionTitle}>Características principales</h2>
                    <div className={styles.featureGrid}>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>📊</div>
                            <h3>Análisis en tiempo real</h3>
                            <p>Monitorea el comportamiento de tus clientes 24/7 con dashboards actualizados al instante</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>⚠️</div>
                            <h3>Detección de riesgo</h3>
                            <p>Identifica clientes con probabilidad de cancelación antes de que tomen la decisión</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>🎯</div>
                            <h3>Campañas personalizadas</h3>
                            <p>Crea estrategias de retención basadas en datos específicos de cada cliente</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>📈</div>
                            <h3>Métricas clave</h3>
                            <p>Accede a KPIs esenciales como LTV, churn rate y tasa de retención</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Benefits Section */}
            <section className={styles.benefits}>
                <div className={styles.container}>
                    <div className={styles.benefitsContent}>
                        <div className={styles.benefitsText}>
                            <h2 className={styles.sectionTitle}>Aumenta la retención de tus clientes</h2>
                            <p>Nuestra plataforma utiliza algoritmos de machine learning para analizar patrones de comportamiento y predecir qué clientes están en riesgo de abandonar tu gimnasio.</p>
                            <ul className={styles.benefitsList}>
                                <li>Reduce el churn rate hasta un 40%</li>
                                <li>Aumenta el LTV de tus clientes</li>
                                <li>Optimiza tus campañas de marketing</li>
                                <li>Toma decisiones basadas en datos</li>
                            </ul>
                            <Link to="/demo" className={`${styles.btn} ${styles.btnPrimary}`}>Solicitar demo</Link>
                        </div>
                        <div className={styles.benefitsImage}>
                            <div className={styles.mockDashboard}>
                                <div className={styles.mockHeader}>
                                    <div className={styles.mockMetrics}>
                                        <div className={styles.mockMetric}>
                                            <span className={styles.mockLabel}>Retención</span>
                                            <span className={styles.mockValue}>87%</span>
                                        </div>
                                        <div className={styles.mockMetric}>
                                            <span className={styles.mockLabel}>Riesgo</span>
                                            <span className={styles.mockValue}>12%</span>
                                        </div>
                                    </div>
                                </div>
                                <div className={styles.mockChart}></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* CTA Section */}
            <section className={styles.cta}>
                <div className={styles.container}>
                    <div className={styles.ctaContent}>
                        <h2>¿Listo para transformar tu gimnasio?</h2>
                        <p>Únete a cientos de gimnasios que ya están reduciendo su churn rate con nuestra plataforma</p>
                        <div className={styles.ctaButtons}>
                            <Link to="/demo" className={`${styles.btn} ${styles.btnPrimary} ${styles.btnLarge}`}>Solicitar demo gratuita</Link>
                            <Link to="/login" className={`${styles.btn} ${styles.btnSecondary} ${styles.btnLarge}`}>Iniciar sesión</Link>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    )
}