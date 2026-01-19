import { Link } from "react-router-dom";
import { useAuth } from '@/contexts/AuthContext';

export default function Navbar() {
    const { isAuthenticated, logout } = useAuth();

    const navStyle = {
        display: 'flex',
        justifyContent: 'center',
        gap: '15px',
        alignItems: 'center',
        padding: '10px'
    };

    return (
        <nav style={navStyle}> {/* Reemplazar estilos inline por .module.css */}
            <Link to="/">Home</Link>
            {isAuthenticated && (<Link to="/dashboard">Dashboard</Link>)}
            {!isAuthenticated ? (<Link to="/login">Login</Link>) : (<button onClick={logout}>Logout</button>)}
        </nav>
    );
}