import { Link } from 'react-router-dom';
import styles from '@/styles/pages/Home.module.css';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';
import dashboardMockup from '@/assets/img/gym-tools.jpg';

export default function Home() {
    useDocumentTitle('Inicio');

    return (
        <div className={styles.homeContainer}>
            {/* HERO SECTION - Tidy Centered Style */}
            <section className={styles.hero}>
                <div className={styles.heroContent}>
                    <img src="/logo-churncheck-white.svg" alt="ChurnCheck Logo" className={styles.heroLogo} />
                    <h1>Analítica predictiva para tu gimnasio</h1>
                    <p>
                        Identifica clientes en riesgo, mejora la retención y toma decisiones basadas en datos con nuestra plataforma especializada.
                    </p>
                    <div className={styles.heroButtons}>
                        <Link to="/login" className={`${styles.btn} ${styles.btnPrimary}`}>Iniciar sesión</Link>
                        <Link to="/register" className={`${styles.btn} ${styles.btnSecondary}`}>Ver demostración</Link>
                    </div>
                </div>
            </section>

            {/* FEATURES SECTION - Minimalist Grid */}
            <section className={styles.features}>
                <div className={styles.container}>
                    <h2 className={styles.sectionTitle}>Diseñado para resultados</h2>
                    <p className={styles.sectionSubtitle}>
                        Nuestra plataforma se integra con tu flujo de trabajo para ofrecerte las métricas que realmente importan.
                    </p>

                    <div className={styles.featureGrid}>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>📈</div>
                            <h3>Análisis Predictivo</h3>
                            <p>Algoritmos avanzados que predicen el comportamiento de tus socios antes de que suceda.</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>🔔</div>
                            <h3>Alertas de Riesgo</h3>
                            <p>Recibe notificaciones automáticas cuando un cliente muestra señales de abandono.</p>
                        </div>
                        <div className={styles.featureCard}>
                            <h3>Dashboard Intuitivo</h3>
                            <div className={styles.featureIcon}>📊</div>
                            <p>Visualiza la salud de tu negocio con gráficos claros y KPIs accionables.</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>👥</div>
                            <h3>Segmentación</h3>
                            <p>Clasifica a tus clientes según su nivel de compromiso y actividad.</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>🛠️</div>
                            <h3>Flujo Robusto</h3>
                            <p>Herramientas diseñadas para escalar junto con el crecimiento de tu centro deportivo.</p>
                        </div>
                        <div className={styles.featureCard}>
                            <div className={styles.featureIcon}>⚡</div>
                            <h3>Integración Rápida</h3>
                            <p>Configuración sencilla para que empieces a ver resultados desde el primer día.</p>
                        </div>
                    </div>
                </div>
            </section>

            {/* MISSION SECTION */}
            <section className={styles.missionVision}>
                <div className={styles.container}>
                    <div className={styles.mvContent}>
                        <div className={styles.mvText}>
                            <span className={styles.mvLabel}>Nuestra Misión</span>
                            <h2>Democratizamos la tecnología predictiva</h2>
                            <p>
                                Buscamos nivelar el campo de juego, permitiendo que dueños de gimnasios locales en América Latina tomen decisiones basadas en datos para proteger su rentabilidad mediante Machine Learning accesible.
                            </p>
                        </div>
                        <div className={styles.mvImage}>
                            <div className={styles.mvImageContainer}>
                                <img src="/src/assets/img/woman.jpg" alt="Misión ChurnCheck" className={styles.mvImg} />
                                <img src="/src/assets/img/woman1.png" alt="Decoración ChurnCheck" className={styles.mvDecoration} />
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* VISION SECTION */}
            <section className={`${styles.missionVision} ${styles.missionVisionBg}`}>
                <div className={styles.container}>
                    <div className={`${styles.mvContent} ${styles.mvReverse}`}>
                        <div className={styles.mvText}>
                            <span className={styles.mvLabel}>Nuestra Visión</span>
                            <h2>El estándar tecnológico para el 2030</h2>
                            <p>
                                Aspiramos a reducir la tasa de deserción global del sector Wellness de un 50% a un saludable 20% mediante intervención proactiva, convirtiéndonos en el referente de habla hispana.
                            </p>
                        </div>
                        <div className={styles.mvImage}>
                            <div className={styles.mvImageContainer} style={{ animationDelay: '1s' }}>
                                <img src="/src/assets/img/man.jpg" alt="Visión ChurnCheck" className={styles.mvImg} />
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* BENEFITS SECTION - Split View */}
            <section className={styles.benefits}>
                <div className={styles.container}>
                    <div className={styles.benefitsContent}>
                        <div className={styles.benefitsText}>
                            <h2>Construido exclusivamente para tu éxito</h2>
                            <p>
                                Nos enfocamos en los problemas reales de la industria del fitness, ofreciendo soluciones técnicas que impactan directamente en tu rentabilidad.
                            </p>
                            <ul className={styles.benefitsList}>
                                <li>Reducción comprobada del churn rate</li>
                                <li>Optimización de campañas de retención</li>
                                <li>Seguimiento detallado de asistencia</li>
                                <li>Soporte técnico especializado</li>
                            </ul>
                        </div>
                        <div className={styles.benefitsImage}>
                            <div className={styles.mockDashboard}>
                                <img 
                                    src={dashboardMockup} 
                                    alt="Vista del Dashboard de ChurnCheck" 
                                    style={{ 
                                        width: '100%', 
                                        height: '300px', 
                                        objectFit: 'cover', 
                                        borderRadius: '8px',
                                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
                                    }} 
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* CTA SECTION - Final Push */}
            <section className={styles.cta}>
                <div className={styles.container}>
                    <div className={styles.ctaContent}>
                        <h2>¿Listo para transformar tu gestión?</h2>
                        <p>Únete a la nueva era de analítica deportiva hoy mismo.</p>
                        <div className={styles.heroButtons}>
                            <Link to="/login" className={`${styles.btn} ${styles.btnPrimary} ${styles.btnLarge}`}>Iniciar sesión</Link>
                        </div>
                    </div>
                </div>
            </section>

            <footer style={{ padding: '40px 0', textAlign: 'center', color: '#64748b', fontSize: '14px' }}>
                <p>© 2026 ChurnCheck. Todos los derechos reservados.</p>
            </footer>
        </div>
    );
}