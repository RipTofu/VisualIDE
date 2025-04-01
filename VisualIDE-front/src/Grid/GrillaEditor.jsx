import React, { useContext, useState, useEffect } from 'react';
import { Responsive, WidthProvider } from "react-grid-layout";
import 'react-grid-layout/css/styles.css';
import 'react-resizable/css/styles.css';
import MEditor from "../Editor/Editor";
import Ejecutar from "../Editor/Ejecutar";
import './GrillaEditor.css';
import Visualizador from "../Visualizador/Visualizador";
import Consola from "../Consola/Consola";
import Configuracion from "../Menues/Configuracion";
import AdministradorArchivos from "../Menues/AdministradorArchivos";
import { ContextoGrilla } from "../Context/ContextoGrilla";
import Parsear from "../ElementosDebug/Parsear";
import Perfil from "../Menues/Perfil";

const ResponsiveGridLayout = WidthProvider(Responsive);

const GrillaEditor = () => {
    const { layout } = useContext(ContextoGrilla);

    const [darkMode, setDarkMode] = useState(() => {
        const temaGuardado = localStorage.getItem('theme');
        return temaGuardado === 'dark' || !temaGuardado;
    });

    const [fontSize, setFontSize] = useState(() => {
        const tamanoFuenteGuardado = localStorage.getItem('fontSize');
        return tamanoFuenteGuardado ? parseInt(tamanoFuenteGuardado, 10) : 14;
    });

    const [contenido, setContenido] = useState('print("Hola mundo!")');
    const [stdout, setStdout] = useState('');
    const [stderr, setStderr] = useState('');
    const [jsonData, setJsonData] = useState(null);
    const [oscurecido, setOscurecido] = useState(false);

    const layoutVisible = layout.filter(item => item.visible);

    useEffect(() => {
        document.documentElement.setAttribute('data-theme', darkMode ? 'dark' : 'light');
        document.documentElement.style.fontSize = `${fontSize}px`;
    }, [darkMode, fontSize]);

    useEffect(() => {
        localStorage.setItem('theme', darkMode ? 'dark' : 'light');
    }, [darkMode]);

    useEffect(() => {
        localStorage.setItem('fontSize', fontSize);
    }, [fontSize]);

    const limpiarVisualizador = () => {
        setJsonData(null);
        setOscurecido(false);
    };

    const renderComponente = (item) => {
        const titulos = {
            'Editor': 'Editor',
            'Visualizador': 'Visualizador',
            'Consola': 'Consola',
            'Archivos': 'Archivos',
            'Configuracion': 'Configuración',
            'Usuario': 'Usuario',
        };

        return (
            <div key={item.i} className="grid-item">
                <div className="topbar-arrastrable">
                    <div className="titulo-arrastrable">
                        {titulos[item.i] || item.i}
                        {item.i === 'Editor' && (
                            <div className="boton-accion">
                                <Ejecutar
                                    codigo={contenido}
                                    setStdout={setStdout}
                                    setStderr={setStderr}
                                    darkMode={darkMode}
                                    limpiarVisualizador={limpiarVisualizador}
                                />
                            </div>
                        )}
                        {item.i === 'Visualizador' && (
                            <div className="boton-accion">
                                <Parsear contenido={contenido} setJsonData={setJsonData} />
                            </div>
                        )}
                    </div>
                </div>
                {item.i === 'Editor' && <MEditor contenido={contenido} setContenido={setContenido} darkMode={darkMode} fontSize={fontSize} />}
                {item.i === 'Consola' && <Consola stdout={stdout} stderr={stderr} />}
                {item.i === 'Visualizador' && <Visualizador jsonData={jsonData} darkMode={darkMode} fontSize={fontSize} />}
                {item.i === 'Archivos' && <AdministradorArchivos setEditorContent={setContenido} />}
                {item.i === 'Configuracion' && <Configuracion darkMode={darkMode} setDarkMode={setDarkMode} fontSize={fontSize} setFontSize={setFontSize} />}
                {item.i === 'Usuario' && <Perfil />}
            </div>
        );
    };

    return (
        <div className={`grid-contenedor ${oscurecido ? 'oscurecido' : ''}`}>
            <ResponsiveGridLayout
                className="layout"
                layouts={{ lg: layoutVisible, md: layoutVisible, sm: layoutVisible, xs: layoutVisible, xxs: layoutVisible }}
                breakpoints={{ lg: 1200, md: 996, sm: 768, xs: 480, xxs: 0 }}
                cols={{ lg: 3, md: 3, sm: 3, xs: 2, xxs: 2 }}
                rowHeight={30}
                autoSize
                margin={[10, 10]}
                compactType="vertical"
                draggableHandle=".topbar-arrastrable"
                draggableCancel=".boton-accion"
            >
                {layoutVisible.map(renderComponente)}
            </ResponsiveGridLayout>
        </div>
    );
};

export default GrillaEditor;
