import DniSearch from '@/components/DniSearch';

export default function PageDashboard() {
    return (
        <div className="dashboard-page">
            <section className="hero" style={{ height: 'auto', padding: '60px 80px' }}>
                <div className="hero-content">
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </section>

            <div className="container">
                <DniSearch />

                {/* Las tarjetas de KPI se agregarán aquí en el futuro */}
                <div style={{ marginTop: '40px', color: '#6b7280', textAlign: 'center' }}>
                    <p>Los indicadores de clientes, riesgo y retención se mostrarán aquí.</p>
                </div>
            </div>
        </div>
    )
}