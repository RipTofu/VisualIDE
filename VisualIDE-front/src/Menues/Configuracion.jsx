import React from 'react';
import './Configuracion.css'
import ToggleTema from './ToggleTema'

const Configuracion = ({darkMode, setDarkMode, fontSize, setFontSize}) => {

    const handleFontSizeChange = (e) => {
        setFontSize(e.target.value);
    };

    return (
        <div className="Configuracion">

            <div className="config-item">
                <ToggleTema darkMode={darkMode} setDarkMode={setDarkMode} />
            </div>

            <div className="config-item">
                <label htmlFor="font-size-slider"
                       style={{color: "var(--primario)"}}>
                    >Tamaño de fuente: {fontSize}px</label>
                <input
                    id="font-size-slider"
                    type="range"
                    min="10"
                    max="24"
                    value={fontSize}
                    onChange={handleFontSizeChange}
                />
            </div>
        </div>
    );
};

export default Configuracion;