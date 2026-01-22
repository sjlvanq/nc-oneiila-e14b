import DniSearch from '@/components/DniSearch';
import GlobalStats from '../components/GlobalStats';
import AttendanceChart from "../components/AttendanceChart.jsx";
import { useState } from 'react';

export default function PageDashboard() {
    const [selectedClient, setSelectedClient] = useState(null);
    return (
        <div className="dashboard-page">
            <section className="hero" style={{ height: 'auto', paddingTop: '100px', paddingLeft: ' 80px' }}>
                <div className="heroContent">
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </section>

            <GlobalStats />

            <div className="container">
                <DniSearch onClientFound={setSelectedClient} />

                {/* Las tarjetas de KPI se agregarán aquí en el futuro */}
                <div style={{ marginTop: '40px', color: '#6b7280', textAlign: 'center' }}>
                    <p>ChurnCheck</p>
                </div>
            </div>
            <div>
                {selectedClient && (
                    <>
                    <h2 style={{alignContent:'center', textAlign:'center', fontSize: '50px'}}>Dashboard</h2>
                    <AttendanceChart clientId={selectedClient.id} />
                    </>
                    
                )}
            </div>
        </div>
    )
}