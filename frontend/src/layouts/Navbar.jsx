import { Link } from "react-router-dom";
import { useAuth } from '@/contexts/AuthContext';
import styles from '@/styles/components/Navbar.module.css'
import homeicon from './icons/house-regular-full.svg'
import dashicon from './icons/chart-line-solid-full.svg'
import loginicon from './icons/arrow-right-to-bracket-solid-full.svg'
import logouticon from './icons/arrow-right-from-bracket-solid-full.svg'

export default function Navbar() {
    const { isAuthenticated, logout } = useAuth();

    return (
        <nav className={styles.nav}> 
            <div className={styles.elementStyle}>
                <Link classname={styles.link} to="/" title="Home" >
                    <img src={homeicon} className={styles.icon} alt="Home" />
                </Link>
            </div>
            <div className={styles.elementStyle}>
                {isAuthenticated && (
                    <Link className={styles.link} to="/dashboard" title="Dashboard">
                        <img src={dashicon} className={styles.icon} alt="Home" />
                    </Link>)
                }
            </div>
            <div className={styles.elementStyle}>
                {!isAuthenticated ? (
                    <Link className={styles.link} title="Login" to="/login"><img src={loginicon} className={styles.icon} alt="Login" /></Link>
                ) : (
                    <button className={styles.button} title="Logout" onClick={logout}><img src={logouticon} className={styles.icon} alt="Logout" /></button>
                )}
            </div>
        </nav>
    );
}