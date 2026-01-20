import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import api from '@/services/api';
import { useAuth } from '@/contexts/AuthContext';

import styles from '@/styles/components/Login.module.css';

export default function Login() {
    const { login, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (isAuthenticated) {
            navigate('/dashboard');
        }
    }, [isAuthenticated, navigate]);

    // States for the form
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    // States for visual control
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [response, setResponse] = useState(null);

    // Function to communicate with the backend
    const getToken = async (e) => {
        e.preventDefault(); // Prevent page reload

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

            // Axios throws an error (goes to catch) if the status is not 2xx
            if (data.token) {
                login(data.token);
                navigate('/dashboard');
            }

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

    // Function to show errors by field
    const getFieldError = (fieldName) => {
        if (!response || !response.fields) return null;
        const fieldError = response.fields.find(f => f.field === fieldName);
        return fieldError ? fieldError.message : null;
    };

    return (
        <div className={styles.card}>
            <div className={styles.cardMessage}>
                {/* Show token if exists */}
                {/* {token && <p className={styles.success}>{token}</p>} */}

                {/* Messages */}
                {loading && <p className={styles.info}>Solicitando acceso...</p>}
                {error && <p className={styles.error}>{error}</p>}
            </div>
            {/* Form */}
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
                    <button type="submit" disabled={loading}>
                        Entrar
                    </button>
                </div>
            </form>
        </div>
    );
}