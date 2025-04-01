import React from 'react';
import { ReactComponent as DiagramIcon } from '../icons/diagram.svg';
import axios from 'axios';
import './Parsear.css';

const Parsear = ({contenido, setJsonData}) => {
    const handleClick = async () => {
        console.log("Recibido: ", contenido);
        try {
            console.log("Usando: ", process.env.REACT_APP_API_URL);
            const response = await axios.post(`${process.env.REACT_APP_API_URL}/api/parse`, contenido, {
                headers: {
                    'Content-Type': 'text/plain'
                }
            });
            const data = response.data;
            console.log('Parseado:', data);
            setJsonData(data);
        } catch (error) {
            console.error('Error, no se pudo parsear: ', error);
        }
    };

    return (
        <div className="parsearIcono">
            <DiagramIcon onClick={handleClick} fill="var(--icono)" width="40px" height="40px" style={{cursor: 'pointer'}} />
        </div>
    );
};

export default Parsear;
