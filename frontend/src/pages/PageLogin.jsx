import Login from '@/components/Login'
import styles from '@/styles/pages/PageLogin.module.css';
import logo from '@/assets/img/logo-churncheck.png'
import { useDocumentTitle } from '@/hooks/useDocumentTitle';

export default function PageLogin() {
    useDocumentTitle('Iniciar Sesión');
    return (
        <div className={styles.loginWrapper}>
            <div className={styles.loginWrapper}>
                <div className={styles.loginCard}>
                    <img src={logo} className={styles.loginCardLogo} alt="ChurnCheck Logo" />
                    <h2>Qué bueno verte de nuevo</h2>
                    <Login />
                </div>
                
            </div>
        </div>
    )
}