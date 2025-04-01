import React, { useState, useEffect, useCallback } from "react";
import { useAuth } from "../Auth/AuthContext";
import './AdministradorArchivos.css';


const AdministradorArchivos = ({ setEditorContent }) => {
    const { user } = useAuth();
    const [files, setFiles] = useState([]);
    const [archivoSeleccionado, setArchivoSeleccionado] = useState(null);

    const fetchArchivos = useCallback(() => {
        if (!user) {
            console.error("Usuario no definido en AuthContext!");
            return;
        }
        console.log("Usuario enviado: ", user);
        fetch(`${process.env.REACT_APP_API_URL}/api/archivos?usuarioID=${user.usuarioID}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Error al obtener archivos.");
                }
                return response.json();
            })
            .then((data) => {
                setFiles(data);
            })
            .catch((error) => {
                console.error(error);
                alert("No hay archivos que mostrar!");
            });
    }, [user]);

    useEffect(() => {
        fetchArchivos();
    }, [fetchArchivos]);

    const validateFile = (file) => {
        if (file && !file.name.endsWith(".py")) {
            alert("Este formato de archivo no es soportado. Por favor, intente con un archivo Python.");
            return false;
        }
        return true;
    };

    const handleFileSelection = (e) => {
        const file = e.target.files[0];
        if (validateFile(file)) {
            setArchivoSeleccionado(file);
        } else {
            setArchivoSeleccionado(null);
        }
    };

    const handleCargaArchivos = () => {
        if (!archivoSeleccionado) {
            alert("El archivo seleccionado no es válido. No se guardó el archivo.");
            return;
        }

        const reader = new FileReader();
        reader.onload = () => {
            const contenidoArchivo = reader.result;
            const nombreArchivo = archivoSeleccionado.name;

            fetch(`${process.env.REACT_APP_API_URL}/api/archivos`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    usuarioID: user.usuarioID,
                    contenido: contenidoArchivo,
                    nombre: nombreArchivo,
                }),
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Error al subir archivo.");
                    }
                    alert("Archivo subido exitosamente.");
                    setArchivoSeleccionado(null);
                    fetchArchivos();
                })
                .catch((error) => {
                    console.error(error);
                    alert("No se pudo cargar el archivo.");
                });
        };

        reader.readAsText(archivoSeleccionado);
    };

    const handleDescargaArchivo = (archivo) => {
        const blob = new Blob([archivo.contenido], { type: "text/plain" });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = archivo.nombre || "archivo_sin_nombre.txt";
        link.click();
    };

    const handleCargarAEditor = async (archivo) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/archivos/${archivo.id}`);
            if (!response.ok) {
                throw new Error("Error al cargar el archivo.");
            }
            const data = await response.json();
            setEditorContent(data.contenido);
        } catch (error) {
            console.error(error);
            alert("No se pudo cargar el archivo al editor.");
        }
    };

    const handleEliminarArchivo = async (archivoID) => {
        if (!window.confirm("¿Estás seguro de que deseas eliminar este archivo?")) {
            return;
        }
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/archivos/${archivoID}`, {
                method: "DELETE",
            });
            if (!response.ok) {
                throw new Error("Error al eliminar archivo.");
            }
            alert("Archivo eliminado exitosamente.");
            fetchArchivos();
        } catch (error) {
            console.error("Error eliminando archivo:", error);
            alert("No se pudo eliminar el archivo.");
        }
    };


    return (
        <div className="administrador-archivos">
            <h2>Administrador de Archivos</h2>
            <div className="upload-section">
                <input
                    type="file"
                    onChange={handleFileSelection}
                />
                <button onClick={handleCargaArchivos}>Cargar archivo</button>
            </div>
            <div className="file-list">
                <h3>Archivos guardados:</h3>
                <ul>
                    {files.map((archivo) => (
                        <li key={archivo.id}>
            <span>
                {archivo.nombre || "Archivo sin nombre"} - {archivo.tamano || 0} KB
            </span>
                            <div className="file-actions">
                                <button
                                    className="download-button"
                                    onClick={() => handleDescargaArchivo(archivo)}
                                >
                                    Descargar
                                </button>
                                <button
                                    className="editor-button"
                                    onClick={() => handleCargarAEditor(archivo)}
                                >
                                    Cargar a Editor
                                </button>
                                <button
                                    className="delete-button"
                                    onClick={() => handleEliminarArchivo(archivo.id)}
                                >
                                    Eliminar
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>

            </div>
        </div>
    );

};

export default AdministradorArchivos;
