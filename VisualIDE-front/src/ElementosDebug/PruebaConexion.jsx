import React from 'react';
import axios from 'axios';

const BotonDePrueba = () => {
    const handleClick = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/mensaje');
            console.log(response.data);
        } catch (error) {
            console.error('Error. No se pudo conectar al backend:', error);
        }
    };

    return (
        <button onClick={handleClick} style={{backgroundColor: "red"}}>
            TEST
        </button>
    );
};

export default BotonDePrueba;