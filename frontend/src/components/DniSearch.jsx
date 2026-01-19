import { useState } from 'react';
import styles from '@/styles/components/DniSearch.module.css';

export default function DniSearch() {
    const [dni, setDni] = useState('');

    /*
    const handleInputChange = (e) => {
        const value = e.target.value;
        // Solo permitir números
        if (/^\d*$/.test(value)) {
            setDni(value);
        }
    }; */

    const handleSearch = (e) => {
        e.preventDefault();
        if (dni) {
            console.log("Buscando cliente con DNI:", dni);
            // Aquí iría la navegación al perfil del cliente en el futuro
            // navigate(`/profile/${dni}`);
        }
    };

    return (
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
    );
}
