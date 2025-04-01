import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
    const [user, setUser] = useState(() => {
        const userFromStorage = localStorage.getItem('user');
        try {
            return userFromStorage ? JSON.parse(userFromStorage) : null;
        } catch (error) {
            console.error('Error parsing user data from localStorage:', error);
            return null;
        }
    });

    const fetchUserProfile = async (email) => {
        try {
            console.log("fetchUserProfile iniciado.");
            const token = localStorage.getItem('authToken');
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/perfil?email=${email}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            if (response.ok) {
                console.log("respuesta okay");
                const profileData = await response.json();
                console.log("Obtenido: ", profileData);
                setUser(profileData);
                localStorage.setItem('user', JSON.stringify(profileData));
            } else {
                throw new Error('No se pudo extraer info de usuario');
            }
        } catch (error) {
            console.error('Error al extraer info de usuario:', error);
            throw error;
        }
    };

    const login = async (credentials) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });

            if (response.ok) {
                const data = await response.json();
                console.log("Datos extraidos: ", data);
                localStorage.setItem('authToken', data.token);
                setIsAuthenticated(true);
                console.log("Logeado exitosamente. Extrayendo informacion de perfil:");
                await fetchUserProfile(data.email);
            } else {
                throw new Error('Credenciales inválidas');
            }
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    };

    const register = async (userDetails) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userDetails),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || 'Registration failed');
            }
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    };

    const logout = () => {
        setIsAuthenticated(false);
        setUser(null);
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
    };

    const getToken = () => localStorage.getItem('authToken');

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                user,
                login,
                register,
                logout,
                getToken,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
