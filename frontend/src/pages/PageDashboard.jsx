import { useState } from 'react';

import { useDocumentTitle } from '@/hooks/useDocumentTitle';

import AttendanceChart from "@/components/AttendanceChart.jsx";
import ClientDetails from '../components/ClientDetails';
import DniSearch from '@/components/DniSearch';
import GlobalStats from '@/components/GlobalStats';
import Recommendations from '../components/Recommendations';

import styles from '@/styles/pages/PageDashboard.module.css';

export default function PageDashboard() {
  const [selectedClient, setSelectedClient] = useState(null);
  useDocumentTitle('Dashboard');
    return (
        <div className="dashboardPage">
            <section className="hero" style={{ height: 'auto', paddingTop: '100px', paddingLeft: ' 80px' }}>
                <div className="heroContent">
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </section>

            <GlobalStats />

            <div className="container">
                {selectedClient && (
                    <div className={styles.predictionResultsContainer}>
                        <div>
                            <ClientDetails client={selectedClient} />
                        </div>
                        <div>
                            <Recommendations probability={selectedClient.probability} />
                        </div>
                    </div>
                
                )}                
                <DniSearch onClientFound={setSelectedClient} />
                
                {selectedClient && <AttendanceChart clientId={selectedClient.id} />}

                <div style={{ marginTop: '40px', color: '#6b7280', textAlign: 'center' }}>
                    <p>ChurnCheck</p>
                </div>
            </div>
        </div>
    )
}