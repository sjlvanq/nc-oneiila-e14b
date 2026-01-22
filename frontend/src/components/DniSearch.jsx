import { useState } from 'react';
import styles from '@/styles/components/DniSearch.module.css';
import api from '@/services/api';

import ClientDetails from './prediction/ClientDetails';
import Recommendations from './prediction/Recommendations';

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
		const client = response
  ? {
      name: response.clientName ?? 'N/A',
      phone: response.clientPhone ?? 'N/A',
      age: response.age ?? 'N/A',
      lifetime: response.lifetime ?? 'N/A',
      avgClassFrequency: response.avgClassFrequency ?? 'N/A',
      groupVisits: response.groupVisits ?? 'N/A',
      avgAdditionalChargesTotal: response.avgAdditionalChargesTotal ?? 0,
      monthToEndContract: response.monthToEndContract ?? 'N/A',
    }
  : null;

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
        <ClientDetails client={client} />
        <Recommendations probability={response.probability} />
    </div>
)}
     </>
    );
}
