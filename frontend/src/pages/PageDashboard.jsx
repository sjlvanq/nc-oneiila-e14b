import DniSearch from '@/components/DniSearch';

export default function PageDashboard() {
    return (
        <div className="dashboard-page">
            <section className="hero" style={{ height: 'auto', paddingTop: '100px', paddingLeft: ' 80px' }}>
                <div className="heroContent">
                    <h1>Dashboard General</h1>
                    <p>Visualiza el estado de tu gimnasio y busca clientes específicos.</p>
                </div>
            </section>

            <div className="container">
                <DniSearch />

                {/* Las tarjetas de KPI se agregarán aquí en el futuro */}
                <div style={{ marginTop: '40px', color: '#6b7280', textAlign: 'center' }}>
                    <p>ChurnCheck</p>
                </div>
            </div>
        </div>
    )
}