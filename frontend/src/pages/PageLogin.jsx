import Login from '../components/Login'
import styles from '@/styles/pages/PageLogin.module.css';
import logo from './img/logo-churncheck.png'

export default function PageLogin(){
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