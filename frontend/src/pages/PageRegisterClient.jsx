import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/services/api';
import Card from '@/components/common/Card';
import { useDocumentTitle } from '@/hooks/useDocumentTitle';
import logoChurncheck from '@/assets/img/logo-churncheck-white.png';
import styles from '@/styles/pages/PageRegisterClient.module.css';

export default function PageRegisterClient() {
    useDocumentTitle('Registrar Nuevo Cliente');
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        clientName: '',
        dni: '',
        gender: 'MALE',
        birthDate: '',
        clientPhone: '',
        nearLocation: true,
        partnerId: null,
        promoFriends: false,
        contractPeriod: 1,
        groupVisits: false,
        active: true
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked :
                name === 'contractPeriod' ? parseInt(value) : value
        }));
    };

    const validateAge = (birthDate) => {
        const birth = new Date(birthDate);
        const today = new Date();
        let age = today.getFullYear() - birth.getFullYear();
        const m = today.getMonth() - birth.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) {
            age--;
        }
        return age >= 18 && age <= 41;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (!validateAge(formData.birthDate)) {
            setError('La edad del cliente debe estar entre 18 y 41 años según los requerimientos del modelo.');
            return;
        }

        try {
            setLoading(true);
            const response = await api.post('/clients', formData);
            if (response.status === 201) {
                setSuccess(true);
                setTimeout(() => navigate('/clients'), 2000);
            }
        } catch (err) {
            console.error('Registration error:', err);
            const message = err.response?.data?.message || 'Error al registrar el cliente. Verifica los datos.';
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    if (success) {
        return (
            <div className={styles.successWrapper}>
                <Card className={styles.successCard}>
                    <div className={styles.successIcon}>✓</div>
                    <h2>¡Registro Exitoso!</h2>
                    <p>El cliente ha sido registrado correctamente. Serás redirigido al listado...</p>
                </Card>
            </div>
        );
    }

    return (
        <div className={styles.registerPage}>
            <header className={styles.hero}>
                <div className={styles.heroContent}>
                    <div className={styles.heroText}>
                        <h1>Registro de Cliente</h1>
                        <p>Inscripción de nuevos miembros con análisis predictivo integrado para retención</p>
                    </div>
                    <img
                        src={logoChurncheck}
                        alt="ChurnCheck Logo"
                        className={styles.heroLogo}
                    />
                </div>
            </header>

            <main className="container">
                <div className={styles.formGrid}>
                    <Card title="Datos de Inscripción" className={styles.formCard}>
                        <form onSubmit={handleSubmit} className={styles.form}>
                            <section className={styles.formSection}>
                                <h3>Información Personal</h3>
                                <div className={styles.inputGroup}>
                                    <label>Nombre Completo</label>
                                    <input
                                        type="text"
                                        name="clientName"
                                        value={formData.clientName}
                                        onChange={handleChange}
                                        required
                                        placeholder="Ej. Juan Pérez"
                                    />
                                </div>
                                <div className={styles.inputRow}>
                                    <div className={styles.inputGroup}>
                                        <label>DNI / Documento</label>
                                        <input
                                            type="text"
                                            name="dni"
                                            value={formData.dni}
                                            onChange={handleChange}
                                            required
                                            placeholder="12345678A"
                                        />
                                    </div>
                                    <div className={styles.inputGroup}>
                                        <label>Género</label>
                                        <select name="gender" value={formData.gender} onChange={handleChange}>
                                            <option value="MALE">Masculino</option>
                                            <option value="FEMALE">Femenino</option>
                                        </select>
                                    </div>
                                </div>
                                <div className={styles.inputRow}>
                                    <div className={styles.inputGroup}>
                                        <label>Teléfono</label>
                                        <input
                                            type="tel"
                                            name="clientPhone"
                                            value={formData.clientPhone}
                                            onChange={handleChange}
                                            required
                                            placeholder="+34 600 000 000"
                                        />
                                    </div>
                                    <div className={styles.inputGroup}>
                                        <label>Fecha de Nacimiento</label>
                                        <input
                                            type="date"
                                            name="birthDate"
                                            value={formData.birthDate}
                                            onChange={handleChange}
                                            required
                                        />
                                        <small className={styles.hint}>Edad requerida: 18 - 41 años</small>
                                    </div>
                                </div>
                            </section>

                            <section className={styles.formSection}>
                                <h3>Contrato y Preferencias</h3>
                                <div className={styles.inputRow}>
                                    <div className={styles.inputGroup}>
                                        <label>Periodo de Contrato (Meses)</label>
                                        <input
                                            type="number"
                                            name="contractPeriod"
                                            min="1"
                                            max="12"
                                            value={formData.contractPeriod}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className={styles.checkboxGrid}>
                                        <label className={styles.checkboxLabel}>
                                            <input
                                                type="checkbox"
                                                name="nearLocation"
                                                checked={formData.nearLocation}
                                                onChange={handleChange}
                                            />
                                            Vive cerca del gimnasio
                                        </label>
                                        <label className={styles.checkboxLabel}>
                                            <input
                                                type="checkbox"
                                                name="promoFriends"
                                                checked={formData.promoFriends}
                                                onChange={handleChange}
                                            />
                                            Promoción Amigos
                                        </label>
                                        <label className={styles.checkboxLabel}>
                                            <input
                                                type="checkbox"
                                                name="groupVisits"
                                                checked={formData.groupVisits}
                                                onChange={handleChange}
                                            />
                                            Clases Grupales
                                        </label>
                                    </div>
                                </div>
                            </section>

                            {error && <div className={styles.errorBanner}>{error}</div>}

                            <div className={styles.actions}>
                                <button
                                    type="button"
                                    onClick={() => navigate('/clients')}
                                    className={styles.cancelBtn}
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    className={styles.submitBtn}
                                    disabled={loading}
                                >
                                    {loading ? 'Registrando...' : 'Registrar Cliente'}
                                </button>
                            </div>
                        </form>
                    </Card>
                </div>
            </main>
        </div>
    );
}
