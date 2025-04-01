import React, { useContext, useState } from 'react';
import './Sidebar.css';
import { ReactComponent as CargararchIcon } from "../icons/file-code.svg";
import { ReactComponent as LogoutIcon } from "../icons/left-from-bracket.svg";
import { ReactComponent as UsuarioIcon } from "../icons/circle-user.svg";
import { ReactComponent as ConfiguracionIcon } from "../icons/settings.svg";
import { ContextoGrilla } from "../Context/ContextoGrilla";
import { useAuth } from "../Auth/AuthContext";

const Sidebar = () => {
    const { cambiarVisibilidadItem } = useContext(ContextoGrilla);
    const { logout } = useAuth();
    const [itemsActivos, setItemsActivos] = useState([]);

    const handleItemClick = (nombreItem) => {
        setItemsActivos((prevItemsActivos) => {
            if (prevItemsActivos.includes(nombreItem)) {
                return prevItemsActivos.filter(item => item !== nombreItem);
            } else {
                return [...prevItemsActivos, nombreItem];
            }
        });
        cambiarVisibilidadItem(nombreItem);
    };

    const handleLogout = () => {
        logout();
    };

    return (
        <div className="sidebar">
            <CargararchIcon
                className={`sidebar-item ${itemsActivos.includes('Archivos') ? 'active' : ''}`}
                onClick={() => handleItemClick('Archivos')}
            />
            <UsuarioIcon
                className={`sidebar-item ${itemsActivos.includes('Usuario') ? 'active' : ''}`}
                onClick={() => handleItemClick('Usuario')}
            />
            <ConfiguracionIcon
                className={`sidebar-item ${itemsActivos.includes('Configuracion') ? 'active' : ''}`}
                onClick={() => handleItemClick('Configuracion')}
            />
            <div className="divider"></div>
            <LogoutIcon className="sidebar-item" onClick={handleLogout} />
        </div>
    );
};

export default Sidebar;
