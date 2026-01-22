import { Link } from "react-router-dom";
import { useAuth } from '@/contexts/AuthContext';
import styles from '@/styles/components/Sidebar.module.css'
import homeicon from './icons/house-regular-full.svg'
import dashicon from './icons/chart-line-solid-full.svg'
import loginicon from './icons/arrow-right-to-bracket-solid-full.svg'
import logouticon from './icons/arrow-right-from-bracket-solid-full.svg'

export default function Sidebar() {
    const { isAuthenticated, logout } = useAuth();

    return (
        <nav className={styles.sidebar}>
            <div className={styles.elementStyle}>
                <Link className={styles.link} to="/" title="Home" >
                    <img src={homeicon} className={styles.icon} alt="Home" />
                    <span className={styles.label}>Home</span>
                </Link>
            </div>
            <div className={styles.elementStyle}>
                {isAuthenticated && (
                    <Link className={styles.link} to="/dashboard" title="Dashboard">
                        <img src={dashicon} className={styles.icon} alt="Dashboard" />
                        <span className={styles.label}>Dashboard</span>
                    </Link>)
                }
            </div>
            <div className={`${styles.elementStyle} ${styles.bottomElement}`}>
                {!isAuthenticated ? (
                    <Link className={styles.link} title="Login" to="/login">
                        <img src={loginicon} className={styles.icon} alt="Login" />
                        <span className={styles.label}>Login</span>
                    </Link>
                ) : (
                    <button className={styles.button + ' ' + styles.link} title="Logout" onClick={logout}>
                        <img src={logouticon} className={styles.icon} alt="Logout" />
                        <span className={styles.label}>Logout</span>
                    </button>
                )}
            </div>
        </nav>
    );
}