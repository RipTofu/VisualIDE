import React, { useState} from "react";
import './ElementosVisualizador.css';
import InfoVariable from "../Popups/InfoVariable";

const Variable = ({ id, valor, linea, tipoDato}) => {
    const [isHover, setIsHover] = useState(false);
    const [posicion, setPosicion] = useState({ x: 0, y: 0 });

    const handleMouseEnter = (e) => {
        setPosicion({ x: e.clientX, y: e.clientY });
        setIsHover(true);
    };

    const handleMouseLeave = () => {
        setIsHover(false);
    }

    return (
        <div
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            style={{
                border: '2px solid var(--borde)',
                color: 'var(--primario)',
                backgroundColor: 'var(--fondo-primario)',
                borderRadius: '3px',
                margin: '10px',
                width: '100px',
                textAlign: 'center',
            }}>
            <div style={{ backgroundColor: 'var(--secundario)' }}>
                {id}
            </div>
            {valor !== undefined ? (
                <div>{valor}</div>
            ) : (
                <div></div>
            )}
            {isHover && <InfoVariable posicion={posicion} linea={linea} tipo={tipoDato} />}
        </div>
    );
};

export default Variable;