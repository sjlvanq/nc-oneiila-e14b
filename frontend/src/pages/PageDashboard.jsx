import { useState } from 'react';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';

import AttendanceChart from "@/components/AttendanceChart.jsx";
import ClientDetails from '@/components/ClientDetails';
import DniSearch from '@/components/DniSearch';
import GlobalStats from '@/components/GlobalStats';
import Recommendations from '@/components/Recommendations';

import logoChurncheck from '@/assets/img/logo-churncheck.png';
import styles from '@/styles/pages/PageDashboard.module.css';

export default function PageDashboard() {
    const [selectedClient, setSelectedClient] = useState(null);
    useDocumentTitle('Dashboard');

    return (
        <div className={styles.dashboardPage}>
            <header className={styles.hero}>
                <div className={styles.heroContent}>
                    <img
                        src={logoChurncheck}
                        alt="ChurnCheck Logo"
                        className={styles.heroLogo}
                    />
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </header>

            <main className="container">
                <div className={styles.dashboardGrid}>
                    {/* Stats Section */}
                    <section className={styles.statsSection}>
                        <GlobalStats />
                    </section>

                    {/* Search Section */}
                    <section className={styles.searchSection}>
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
                    <p>© 2026 ChurnCheck - Avanzando hacia el futuro del fitness</p>
                </footer>
            </main>
        </div>
    );
}