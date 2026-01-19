import { useState } from 'react';

import api from '@/services/api';

import styles from './Login.module.css';

export default function Login() {
	// Estados del formulario
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');

	// Estados de control visual
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState('');
	const [response, setResponse] = useState(null);

	// Función que se comunica con el backend
	const getToken = async (e) => {
		e.preventDefault(); // Evita que la página se recargue

		const postBody = {
			email: username,
			password: password
		};

		try {
			setError('');
			setResponse(null);
			setLoading(true);

			const response = await api.post('/login', postBody);
			const data = response.data;

			// Axios lanza un error (cae al catch) si el status no es 2xx

		} catch (error) {
			if (error.response) {
            	setError(error.response.data.message || 'Error en las credenciales');
                setResponse(error.response.data);
            } else {
                setError("Sin conexión con el Backend");
            }
        } finally {
			setLoading(false);
		}
	};

	// Función auxiliar para mostrar errores por campo
	const getFieldError = (fieldName) => {
		if (!response || !response.fields) return null;
		const fieldError = response.fields.find(f => f.field === fieldName);
		return fieldError ? fieldError.message : null;
	};

	return (
		<div className={styles.card}>
            <div className={styles.cardMessage}>
                {/* Muestra el token si existe */}
                {/* {token && <p className={styles.success}>{token}</p>} */}

                {/* Mensajes de estado */}
                {loading && <p className={styles.info}>Solicitando acceso...</p>}
                {error && <p className={styles.error}>{error}</p>}            
            </div>
			{/* Formulario */}
            <form>
                <div className={styles.cardInput}>
                    <label htmlFor="username">
                        Nombre de usuario:
                    </label>
                    <input
                        type="text"
                        // value={username}
                        placeholder='Correo Electrónico'
                        onChange={(e) => setUsername(e.currentTarget.value)}
                    />
                    <br />
                    <p className={styles.fieldError}>
                        <span>{getFieldError('email')}</span>
                    </p>
                </div>
                <div className={styles.cardInput}>
                    Contraseña:
                    <input
                        type="password"
                        placeholder='Contraseña'
                        onChange={(e) => setPassword(e.currentTarget.value)}
                    />
                    <br />
                    <p className={styles.fieldError}>
                        <span>{getFieldError('password')}</span>
                    </p>
                </div>
                <div className={styles.cardButton}>
                    <button type="button" onClick={getToken} disabled={loading}>
                        Entrar
                    </button>
                </div>
            </form>
		</div>
	);
}