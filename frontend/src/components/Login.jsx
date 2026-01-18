// export default function Login(){
//     return (
//         <div>
//             <h1>Login</h1>
//         </div>
//     )
// }

import { useState } from 'react';
import styles from './Login.module.css';

export default function Login() {
	// Estados del formulario
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');

	// Estados de control visual
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState('');
	const [response, setResponse] = useState(null);
	const [token, setToken] = useState('');

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
            setToken('');

			const response = await fetch('http://localhost:8080/login', {
				method: 'POST',
				headers: {
					'Content-type': 'application/json',
				},
				body: JSON.stringify(postBody)
			});

			const data = await response.json();
			setResponse(data);

			if (response.ok) {
				setToken(data.token);
			} else {
                setToken(''); 
				setError(data.message || 'Error no especificado');
			}

		} catch (error) {
			console.error("Sin conexión con el Backend");
			setError("Sin conexión con el Backend");
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
            <form onSubmit={getToken}>
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
                    <button onClick={getToken} disabled={loading}>
                        Entrar
                    </button>
                </div>
            </form>
		</div>
	);
}