import { Link, Outlet } from "react-router-dom";
import { useAuth } from '@/contexts/AuthContext';
import logo from '@/assets/img/logo-churncheck.png';

export default function MainLayout() {
    const { isAuthenticated, logout } = useAuth();

    return (
        <>
            <header>
                <img src={logo} alt="ChurnCheck Logo" className="logo" />
                <nav>
                    {!isAuthenticated ? (
                        <>
                            <a href="#">Visión</a>
                            <a href="#">Misión</a>
                            <Link to="/">Inicio</Link>
                            <Link to="/login" className="btn-login">Login</Link>
                        </>
                    ) : (
                        <>
                            <Link to="/dashboard">Dashboard</Link>
                            <Link to="/clients">Clientes</Link>
                            <Link to="/risk">Riesgo</Link>
                            <Link to="/retention">Retención</Link>
                            <Link to="/reports">Reportes</Link>
                            <button onClick={logout} className="btn-logout">Salir</button>
                        </>
                    )}
                </nav>
            </header>

            <main>
                <Outlet />
            </main>
        </>
    );
}
