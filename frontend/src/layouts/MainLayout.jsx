import { Link, Outlet } from "react-router-dom";
import { useAuth } from '@/contexts/AuthContext';

export default function MainLayout() {
    const { isAuthenticated, logout } = useAuth();

    return (
        <>
            <header>
                <nav>
                    {!isAuthenticated ? (
                        <>
                            <Link to="/">Inicio</Link>
                            <Link to="/login">Login</Link>
                        </>
                    ) : (
                        <>
                            <Link to="/dashboard">Dashboard</Link>
                            <button onClick={logout}>Salir</button>
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
