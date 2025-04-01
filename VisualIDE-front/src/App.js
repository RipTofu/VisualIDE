import React from 'react';
import GrillaEditor from './Grid/GrillaEditor';
import './Grid/GrillaEditor.css';
import Sidebar from './Sidebar/Sidebar';
import './App.css';
import { GrillaProvider } from './Context/ContextoGrilla';
import { AuthProvider, useAuth } from './Auth/AuthContext';
import Login from './Auth/Login';

function ProtectedApp() {
    const { isAuthenticated } = useAuth();

    // No autenticado
    if (!isAuthenticated) {
        return <Login />;
    }

    // Autenticado
    return (
        <GrillaProvider>
            <div className="app-contenedor">
                <Sidebar />
                <div className="contenido">
                    <GrillaEditor />
                </div>
            </div>
        </GrillaProvider>
    );
}

function App() {
    return (
        <AuthProvider>
            <ProtectedApp />
        </AuthProvider>
    );
}

export default App;
