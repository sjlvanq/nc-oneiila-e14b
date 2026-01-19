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

                    {/* Navigation React */}
                    <Link to="/">Inicio</Link>

                    {isAuthenticated && (
                        <Link to="/dashboard">Dashboard</Link>
                    )}

                    {/* Action Button */}
                    {!isAuthenticated ? (
                        <Link to="/login" className="btn-login">Login</Link>
                    ) : (
                        <button onClick={logout} className="btn-logout">Salir</button>
                    )}
                </nav>
            </header>

            <main>
                <Outlet />
            </main>
        </>
    );
}
