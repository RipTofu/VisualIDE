import React, { useState, useEffect } from 'react';
import Variable from './Variable';
import Highlighter from "./Highlighter";

const Visualizador = ({ jsonData, darkMode, fontSize}) => {
    const [indexActual, setIndexActual] = useState(0);
    const [variablesHistory, setVariablesHistory] = useState([{}]);
    const [pasos, setPasos] = useState([]);
    const [lineaActual, setLineaActual] = useState(null);

    useEffect(() => {
        if (!jsonData) {

            setIndexActual(0);
            setVariablesHistory([{}]);
            setPasos([]);
            setLineaActual(null)
        }

        if (jsonData) {
            const pasosProcesados = jsonData.map(step => ({
                ...step,
                variables: JSON.parse(step.variables),
            }));
            setPasos(pasosProcesados);
            if (pasosProcesados.length > 0) {
                setLineaActual(pasosProcesados[0].id);
            }
        }
    }, [jsonData]);

    const handleSig = () => {
        if (indexActual < pasos.length - 1) {
            const nextIndex = indexActual + 1;
            const nextLineId = pasos[nextIndex].id;
            if (nextLineId !== lineaActual) {
                setLineaActual(nextLineId);
            }
            procesarPaso(nextIndex);
            setIndexActual(nextIndex);
        }
    };

    const handlePrev = () => {
        if (indexActual > 0) {
            const prevIndex = indexActual - 1;
            const prevLineId = pasos[prevIndex].id;
            if (prevLineId !== lineaActual) {
                setLineaActual(prevLineId);
            }
            setIndexActual(prevIndex);
        }
    };

    const procesarPaso = (indexPaso) => {
        const paso = pasos[indexPaso];
        const prevVariables = variablesHistory[variablesHistory.length - 1];
        const updatedVariables = { ...prevVariables };
        paso.variables.forEach(variable => {
            updatedVariables[variable.ID] = {
                valor: variable.Completado ? variable.valor.valor : undefined,
                linea: variable.linea,
                tipoDato: variable.valor.tipo,
            };
        });
        setVariablesHistory(prevHistory => [...prevHistory, updatedVariables]);
    };

    const pasoActual = pasos[indexActual];
    const currentLine = pasoActual?.linea || '';
    const previousLine = pasos.find(paso => paso.id === pasoActual?.id - 1)?.linea || '';
    const nextLine = pasos.find(paso => paso.id === pasoActual?.id + 1)?.linea || '';
    const variables = variablesHistory[indexActual];

    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            alignItems: 'center',
            justifyContent: 'space-between',
            height: '100%',
            padding: '10px',
            overflow: 'auto'
        }}>
            <div>
                <button onClick={handlePrev}>&uarr;</button>
                <button onClick={handleSig}>&darr;</button>
            </div>

            <div style={{
                width: '100%',
                textAlign: 'center',
                flexGrow: 1,
                display: 'flex',
                flexDirection: 'column',
                gap: '3px',
                alignItems: 'center',
                justifyContent: 'center'
            }}>
                {previousLine && (
                    <Highlighter
                        codeLine={previousLine}
                        language="python"
                        darkMode={darkMode}
                        fontSize={fontSize * 0.7}
                        esLineaActual={false}
                    />
                )}
                <Highlighter
                    codeLine={currentLine}
                    language="python"
                    darkMode={darkMode}
                    fontSize={fontSize}
                    esLineaActual={true}
                />
                {nextLine && (
                    <Highlighter
                        codeLine={nextLine}
                        language="python"
                        darkMode={darkMode}
                        fontSize={fontSize * 0.7}
                        esLineaActual={false}
                    />
                )}
            </div>

            <div style={{
                display: "flex",
                width: "100%",
                flexGrow: 3
            }}>
                <div style={{
                    flex: 7,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    padding: '10px',
                    boxSizing: 'border-box'
                }}>
                    {pasoActual?.blackboard && (
                        <div style={{ fontSize: '2rem', color: 'var(--primario)' }}>
                            {pasoActual.blackboard}
                        </div>
                    )}
                </div>

                <div style={{
                    flex: 3,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'flex-start',
                    padding: '10px',
                    boxSizing: 'border-box',
                    overflowY: 'auto',
                }}>
                    {Object.keys(variables || {}).map(id => (
                        <Variable key={id}
                                  id={id}
                                  valor={variables[id].valor}
                                  linea={variables[id].linea}
                                  tipoDato={variables[id].tipoDato}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Visualizador;
