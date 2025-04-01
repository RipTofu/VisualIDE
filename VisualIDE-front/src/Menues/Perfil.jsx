import React from 'react';
import { useAuth} from "../Auth/AuthContext";
import './Perfil.css';

const Perfil = () => {
    const { user } = useAuth();

    if (!user) {
        console.log(user);
        return <div>Ocurrió un error inesperado al cargar tu perfil. Por favor, date una vuelta y vuelve pronto, que ya lo arrelgo.</div>;
    }
    console.log(user);

    const { nombre, email, centro, archivosguardados, almacenamientodisponible } = user;

    const MBtotales = 60;
    const MBusados = MBtotales - almacenamientodisponible;
    const porcentajeUsado = (MBusados / MBtotales) * 100;

    return (
        <div className="perfil-container">
            <h2>Mi Perfil</h2>
            <div className="perfil-info">
                <p><strong>Nombre:</strong> {nombre}</p>
                <p><strong>Correo:</strong> {email}</p>
                <p><strong>Centro de estudios:</strong> {centro?.nombre || 'N/A'}</p>
                <p><strong>Archivos guardados:</strong> {archivosguardados} archivos ({MBusados} MB en total)</p>
                <p><strong>Espacio disponible:</strong> {almacenamientodisponible} MB</p>
                <div className="barra-almacenamiento">
                    <div
                        className="barra-almacenamiento-usado"
                        style={{ width: `${porcentajeUsado}%` }}
                    ></div>
                </div>
            </div>
        </div>
    );
};

export default Perfil;
