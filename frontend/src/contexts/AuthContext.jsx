import React, { createContext, useContext, useState, useEffect} from 'react';

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    //const [user, setUser] = useState(localStorage.getItem('user'));
    const [token, setToken] = useState(localStorage.getItem('token'));

    const login = (token) => {
        localStorage.setItem('token', token);
        //const decoded = parseJwt(token);
        //setUser(userData);
        setToken(token);
    }; 
    
    const logout = () => {
        localStorage.removeItem('token');
        //setUser(null);
        setToken(null);
    }

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if (storedToken) {
            setToken(storedToken);
        }
    }, []);

    const isAuthenticated = !!token;
    //Ejemplo de uso: {isAuthenticated ? <Logout /> : <Login />}

    return (
        <AuthContext.Provider value={{ token, isAuthenticated, login, logout }}>
        {children}
        </AuthContext.Provider>
    );
}