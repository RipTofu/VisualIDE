import React, { useState } from 'react';
import { useAuth } from './AuthContext';
import './Login.css';

export default function Login() {
    const { login, register } = useAuth();
    const [isRegister, setIsRegister] = useState(false);
    const [formData, setFormData] = useState({ email: '', password: '', name: '' });
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setError('');
            if (isRegister) {
                await register({
                    email: formData.email,
                    contrasena: formData.password,
                    nombre: formData.name,
                });
                alert('¡Registro exitoso! Por favor, inicia sesión.');
                setIsRegister(false);
            } else {
                await login({
                    email: formData.email,
                    contrasena: formData.password,
                });
            }
        } catch (err) {
            setError(err.message || 'Ocurrió un error');
        }
    };

    const toggleForm = () => {
        setIsRegister((prev) => !prev);
        setFormData({ email: '', password: '', name: '' });
        setError('');
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit} className="login-form">
                <h2>{isRegister ? 'Registro' : 'Login'}</h2>
                {error && <p className="error-message">{error}</p>}
                <input
                    type="email"
                    placeholder="Correo electrónico"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    required
                />
                <input
                    type="password"
                    placeholder="Contraseña"
                    value={formData.password}
                    onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                    required
                />
                {isRegister && (
                    <input
                        type="text"
                        placeholder="Nombre"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        required
                    />
                )}
                <button type="submit">{isRegister ? 'Registrar' : 'Login'}</button>
                <button type="button" onClick={toggleForm}>
                    {isRegister ? 'Ya tienes cuenta? Inicia sesión' : 'Regístrate'}
                </button>
            </form>
        </div>
    );
}
