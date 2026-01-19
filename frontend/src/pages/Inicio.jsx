import { Link } from 'react-router-dom';
import styles from './Inicio.module.css';

export default function Inicio(){
    return (
        <div>
            <section className={styles.hero}>
                <div className={styles.heroContent}>
                    <h1>Analítica predictiva para gimnasios</h1>
                    <p>Clientes, riesgo y retención en un solo lugar</p>
                    <Link to="/login" className={styles.btn}>Iniciar sesión</Link>
                </div>
            </section>
        </div>
    )
}