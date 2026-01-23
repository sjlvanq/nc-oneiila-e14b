import { useState, useEffect, useCallback } from 'react';
import api from '@/services/api';
import Card from '@/components/common/Card';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';
import logoChurncheck from '@/assets/img/logo-churncheck-white.png';
import styles from '@/styles/pages/PageClientList.module.css';

export default function PageClientList() {
    useDocumentTitle('Listado de Clientes');

    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [error, setError] = useState(null);

    const fetchClients = useCallback(async (pageNumber) => {
        try {
            setLoading(true);
            setError(null);
            const response = await api.get(`/clients?page=${pageNumber}&size=10&sort=clientName,asc`);
            const data = response.data;

            setClients(data.content || []);
            setTotalPages(data.totalPages || 0);
            setPage(data.number || 0);
        } catch (err) {
            console.error('Error fetching clients:', err);
            setError('No se pudo cargar la lista de clientes. Por favor, intenta de nuevo más tarde.');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchClients(page);
    }, [page, fetchClients]);

    const handleNextPage = () => {
        if (page < totalPages - 1) {
            setPage(prev => prev + 1);
        }
    };

    const handlePrevPage = () => {
        if (page > 0) {
            setPage(prev => prev - 1);
        }
    };

    return (
        <div className={styles.clientsPage}>
            <header className={styles.hero}>
                <div className={styles.heroContent}>
                    <div className={styles.heroText}>
                        <h1>Gestión de Clientes</h1>
                        <p>Listado completo de clientes activos en el sistema con información detallada</p>
                    </div>
                    <img
                        src={logoChurncheck}
                        alt="ChurnCheck Logo"
                        className={styles.heroLogo}
                    />
                </div>
            </header>

            <main className="container">
                <Card title="Listado de Clientes Activos">
                    {loading ? (
                        <div className={styles.loading}>Cargando clientes...</div>
                    ) : error ? (
                        <div className={styles.error}>{error}</div>
                    ) : (
                        <div className={styles.tableWrapper}>
                            <table className={styles.table}>
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Género</th>
                                        <th>Teléfono</th>
                                        <th>Edad</th>
                                        <th>Cercanía</th>
                                        <th>Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {clients.length > 0 ? (
                                        clients.map(client => (
                                            <tr key={client.id}>
                                                <td>{client.clientName}</td>
                                                <td>{client.gender === 'MALE' ? 'Masculino' : 'Femenino'}</td>
                                                <td>{client.clientPhone}</td>
                                                <td>{client.age} años</td>
                                                <td>{client.nearLocation ? 'Sí' : 'No'}</td>
                                                <td>
                                                    <span className={styles.badgeActive}>Activo</span>
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="6" className={styles.noData}>No hay clientes activos registrados.</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}

                    {!loading && !error && totalPages > 1 && (
                        <div className={styles.pagination}>
                            <button
                                onClick={handlePrevPage}
                                disabled={page === 0}
                                className={styles.pageBtn}
                            >
                                Anterior
                            </button>
                            <span className={styles.pageInfo}>
                                Página {page + 1} de {totalPages}
                            </span>
                            <button
                                onClick={handleNextPage}
                                disabled={page === totalPages - 1}
                                className={styles.pageBtn}
                            >
                                Siguiente
                            </button>
                        </div>
                    )}
                </Card>
            </main>
        </div>
    );
}
