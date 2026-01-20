import { useState } from 'react';
import styles from '@/styles/components/DniSearch.module.css';
import api from '@/services/api';

export default function DniSearch() {
    const [dni, setDni] = useState('');

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [response, setResponse] = useState(null);

    /*
    const handleInputChange = (e) => {
        const value = e.target.value;
        // Solo permitir números
        if (/^\d*$/.test(value)) {
            setDni(value);
        }
    }; */

    const handleSearch = async (e) => {
        e.preventDefault();
        if (dni) {
            try {
                setError('');
                setResponse(null);
                setLoading(true);
                
                const response = await api.get(`/clients/prediction/${dni}`);
                const data = response.data;
                console.log(data);
                setResponse(data);

            } catch (error) {
                if (error.response) {
                    setError(error.response.data.message || 'Error al consultar churn');
                    setResponse(error.response.data);
                } else {
                    setError("Sin conexión con el Backend");
                }
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <>
        <form className={styles.searchBox} onSubmit={handleSearch}>
            <input
                type="text"
                placeholder="Buscar cliente por DNI"
                value={dni}
                onChange={(e)=>setDni(e.currentTarget.value)}
                //inputMode="numeric"
                //pattern="[0-9]*"
            />
            <button type="submit">
                Buscar cliente
            </button>
        </form>

        {loading && <p className={styles.info}>Consultando churn...</p>}
        {error && <p className={styles.error}>{error}</p>}
        
        {!error && response && (
            <div className={styles.card}>
            <div className={styles.perfilGrid}>
                <h2 id="nombre">{response.clientName}</h2>
                <h3>Tel.: {response.clientPhone}</h3>
                <div className={styles.datos}>
                    <div className={styles.dato}><strong>Churn:</strong> {response.churn}</div>
                    <div className={styles.dato}><strong>Probabilidad:</strong> {response.probability}</div>
                    <div className={styles.dato}><strong>Timestamp:</strong> {response.timestamp}</div>
                </div>
            </div>
            </div>
        )}

        </>
    );
}
