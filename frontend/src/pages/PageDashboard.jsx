import { useState } from 'react';

import { useDocumentTitle } from '@/hooks/useDocumentTitle';

import AttendanceChart from "@/components/AttendanceChart.jsx";
import ClientDetails from '../components/ClientDetails';
import DniSearch from '@/components/DniSearch';
import GlobalStats from '@/components/GlobalStats';
import Recommendations from '../components/Recommendations';

import logoChurncheck from '@/assets/img/logo-churncheck.png';
import gymHero from '@/assets/img/gym-hero.jpg';

import styles from '@/styles/pages/PageDashboard.module.css';

export default function PageDashboard() {
  const [selectedClient, setSelectedClient] = useState(null);
  useDocumentTitle('Dashboard');
    return (
        <div className={styles.dashboardPage}>
            <section 
                className={styles.hero}
                style={{
                    backgroundImage: `url(${gymHero})`
                }}
            >
                <div className={styles.heroContent}>
                    <img 
                        src={logoChurncheck} 
                        alt="ChurnCheck Logo" 
                        className={styles.heroLogo}
                    />
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </section>

            <GlobalStats />

            <div className="container">

                <DniSearch onClientFound={setSelectedClient} />

                {selectedClient && (
                    <div className={styles.predictionResultsContainer}>
                        {/* Sección de gráficas - ocupa más espacio */}
                        <div className={styles.chartsSection}>
                            <AttendanceChart clientId={selectedClient.id} />
                            <ClientDetails client={selectedClient} />
                            <Recommendations probability={selectedClient.probability} />
                        </div>
                        
                        {/* Sección de detalles - abajo
                        <div className={styles.detailsSection}>
                        </div> */}
                    </div>
                
                )}
                
                <div className={styles.footer}>
                    <p>ChurnCheck</p>
                </div>
            </div>
        </div>
    )
}