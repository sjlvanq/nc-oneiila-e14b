import { Link } from 'react-router-dom';
import styles from '@/styles/pages/NotFound.module.css';

export default function NotFound() {
    return (
        <div className={styles.wrapper}>
            <div className={styles.content}>
                <h1 className={styles.title}>404</h1>
                <h2 className={styles.subtitle}>Oops! Página no encontrada</h2>
                <p className={styles.description}>
                    Parece que te has perdido. La página que buscas no existe o ha sido movida.
                </p>
                <Link to="/" className={styles.btnHome}>
                    Volver al inicio
                </Link>
            </div>
        </div>
    );
}
