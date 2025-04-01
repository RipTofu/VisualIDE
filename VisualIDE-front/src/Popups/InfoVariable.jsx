import React from 'react';

const InfoVariable = ({ linea, tipo, posicion }) => {
    const aspectoInfoVariable = {
        position: 'absolute',
        top: posicion.y + 15,
        left: posicion.x + 15,
        padding: '10px',
        color: 'var(--primario)',
        backgroundColor: 'var(--fondo-primario)',
        border: '1px var(--borde)',
        boxShadow: '0px 4px 8px var(--borde)',
        zIndex: 9999,
        visibility: 'visible',
    };

    return (
        <div style={aspectoInfoVariable}>
            <p>Linea: {linea}'<br/>
                tipo: {tipo}
            </p>
        </div>
    )
}

export default InfoVariable