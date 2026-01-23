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
        <div className="dashboardPage">
            <section 
                className="hero" 
                style={{
                    height: 'auto', 
                    paddingTop: '100px', 
                    paddingLeft: '80px',
                    backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${gymHero})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    backgroundRepeat: 'no-repeat',
                    color: 'white'
                }}
            >
                <div className="heroContent">
                    <img 
                        src={logoChurncheck} 
                        alt="ChurnCheck Logo" 
                        style={{
                            width: '80px',
                            height: 'auto',
                            marginBottom: '20px'
                        }}
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
                        <div>
                            <ClientDetails client={selectedClient} />
                            <AttendanceChart clientId={selectedClient.id} />
                        </div>
                        <div>
                            <Recommendations probability={selectedClient.probability} />
                        </div>
                    </div>
                
                )}
                
                <div style={{ marginTop: '40px', color: '#6b7280', textAlign: 'center' }}>
                    <p>ChurnCheck</p>
                </div>
            </div>
        </div>
    )
}