import { useState } from 'react';
import styles from '@/styles/components/DniSearch.module.css';
import api from '@/services/api';

export default function DniSearch({ onClientFound }) {
    const [dni, setDni] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSearch = async (e) => {
        e.preventDefault();
        if (dni) {
            try {
                setError('');
                setLoading(true);

                const response = await api.get(`/clients/prediction/${dni}`);
                const data = response.data;

                onClientFound?.(data);

            } catch (error) {
                if (error.response) {
                    setError(error.response.data.message || 'Error al consultar churn');
                } else {
                    setError("Sin conexión con el Backend");
                }
                onClientFound?.(null);
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
                    onChange={(e) => setDni(e.currentTarget.value)}
                />
                <button type="submit" disabled={loading}>
                    {loading ? 'Buscando...' : 'Buscar cliente'}
                </button>
            </form>

            {error && <p className={styles.error}>{error}</p>}
        </>
    );
}
