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
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    // States for visual control
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [response, setResponse] = useState(null);

    // Function to communicate with the backend
    const handleSubmit = async (e) => {
        e.preventDefault();

        const postBody = {
            email: email,
            password: password
        };

        try {
            setError('');
            setResponse(null);
            setLoading(true);

            const res = await api.post('/login', postBody);
            const data = res.data;

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
                {loading && <p className={styles.info}>Solicitando acceso...</p>}
                {error && <p className={styles.error}>{error}</p>}
            </div>

            <form onSubmit={handleSubmit}>
                <div className={styles.cardInput}>
                    <label htmlFor="email">
                        Correo Electrónico:
                    </label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        placeholder='ejemplo@correo.com'
                        onChange={(e) => setEmail(e.currentTarget.value)}
                        required
                    />
                    <p className={styles.fieldError}>
                        <span>{getFieldError('email')}</span>
                    </p>
                </div>

                <div className={styles.cardInput}>
                    <label htmlFor="password">
                        Contraseña:
                    </label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        placeholder='Contraseña'
                        onChange={(e) => setPassword(e.currentTarget.value)}
                        required
                    />
                    <p className={styles.fieldError}>
                        <span>{getFieldError('password')}</span>
                    </p>
                </div>

                <div className={styles.cardButtons}>
                    <button
                        type="button"
                        className={styles.cancelButton}
                        onClick={() => navigate('/')}
                        disabled={loading}
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        className={styles.loginButton}
                        disabled={loading}
                    >
                        {loading ? 'Entrando...' : 'Entrar'}
                    </button>
                </div>
            </form>
        </div>
    );
}