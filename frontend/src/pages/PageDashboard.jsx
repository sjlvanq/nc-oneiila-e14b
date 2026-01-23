import { useState } from 'react';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';

import AttendanceChart from "@/components/AttendanceChart.jsx";
import ClientDetails from '@/components/ClientDetails';
import DniSearch from '@/components/DniSearch';
import GlobalStats from '@/components/GlobalStats';
import HighRiskList from '@/components/HighRiskList';
import Recommendations from '@/components/Recommendations';

import logoChurncheck from '@/assets/img/logo-churncheck-white.png';
import styles from '@/styles/pages/PageDashboard.module.css';

export default function PageDashboard() {
    const [selectedClient, setSelectedClient] = useState(null);
    useDocumentTitle('Dashboard');

    return (
        <div className={styles.dashboardPage}>
            <header className={styles.hero}>
                <div className={styles.heroContent}>
                    <div className={styles.heroText}>
                        <h1>Centro de Inteligencia</h1>
                        <p>Monitoreo predictivo avanzado para retener clientes y optimizar tu negocio</p>
                    </div>
                    <img
                        src={logoChurncheck}
                        alt="ChurnCheck Logo"
                        className={styles.heroLogo}
                    />
                </div>
            </header>

            <main className="container">
                <div className={styles.dashboardGrid}>
                    {/* Global Stats Section */}
                    <section className={styles.statsSection}>
                        <GlobalStats />
                        <HighRiskList />
                    </section>

                    {/* Search Section */}
                    <section className={styles.searchSection}>
                        <div className={styles.sectionHeader}>
                            <h2>Busca un cliente para análisis detallado</h2>
                        </div>
                        <DniSearch onClientFound={setSelectedClient} />
                    </section>

                    {/* Result Section (Conditional) */}
                    {selectedClient && (
                        <div className={styles.resultsGrid}>
                            <div className={styles.chartsArea}>
                                <AttendanceChart clientId={selectedClient.id} />
                            </div>

                            <div className={styles.detailsArea}>
                                <ClientDetails client={selectedClient} />
                                <Recommendations probability={selectedClient.probability} />
                            </div>
                        </div>
                    )}
                </div>

                <footer className={styles.footer}>
                    <p>© 2026 ChurnCheck - Inteligencia Artificial para el Éxito Fitness</p>
                </footer>
            </main>
        </div>
    );
}