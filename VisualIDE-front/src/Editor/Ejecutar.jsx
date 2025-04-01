import React from "react";
import { ReactComponent as EjecutarIcono } from '../icons/run.svg';
import './Ejecutar.css';
import axios from 'axios';

function Ejecutar({codigo, setStdout, setStderr, darkMode, limpiarVisualizador}) {
    const config = {
        headers: {
            "Content-Type": "text/plain",
        },
    };

    const handleEjecutar = async () => {
        limpiarVisualizador();
        if (codigo.includes("import")) {
            setStdout('');
            setStderr("Error al ejecutar: Importe de librerías no soportado.");
        } else if(codigo.includes("input")) {
            setStdout('');
            setStderr("Error al ejecutar: Ingresos de usuario no soportado");
        } else if(codigo.includes("while(true)")) {
            setStdout('');
            setStderr("Error al ejecutar: Se declaró un bucle infinito. Por favor, revisa tu código e inténtalo de nuevo.");
        } else {
            try {
                console.log("codigo a ejecutar:", codigo)
                const response = await axios.post(`${process.env.REACT_APP_API_URL}/api/ejecutar`, codigo, config);
                const data = response.data;
                console.log("resultado recibido:", data.stdout, data.stderr);
                setStdout(data.stdout);
                setStderr(data.stderr);
            } catch (error) {
                setStdout('');
                setStderr('Error al ejecutar: ' + error.message);
            }
        }
    }


    return (
        <div className="ejecutarIcono" onClick={handleEjecutar} style={{backgroundColor: 'transparent', border: 'none', cursor: 'pointer'}}>
            <EjecutarIcono fill="var(--icono)" width="40px" height="40px" />
        </div>
    );
}

export default Ejecutar;