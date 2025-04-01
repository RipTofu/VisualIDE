import React, { createContext, useState } from "react";

export const ContextoGrilla = createContext();

export const GrillaProvider = ({ children }) => {
    const layoutInicial = [
        { i: 'Editor', x: 0, y: 1, w: 2, h: 10, minH: 7, resizeHandles: ["s", "w", "e", "n"], visible: true },
        { i: 'Consola', x: 1, y: 2, w: 3, h: 5, minH: 4, resizeHandles: ["s", "w", "e", "n"], visible: true },
        { i: 'Visualizador', x: 2, y: 1, w: 1, h: 10, minH: 6, resizeHandles: ["s", "w", "e", "n"], visible: true },
        { i: 'Archivos', x: 0, y: 0, w: 1, h: 10, minH: 5, resizeHandles: ["s", "w", "e", "n"], visible: false },
        { i: 'Usuario', x: 0, y: 0, w: 1, h: 10,minH: 5, resizeHandles: ["s", "w", "e", "n"], visible: false },
        { i: 'Configuracion', x: 0, y: 0, w: 1, h: 10, minH: 5,resizeHandles: ["s", "w", "e", "n"], visible: false }
    ];

    const [layout, setLayout] = useState(layoutInicial);

    const cambiarVisibilidadItem = (itemId) => {
        setLayout((prevLayout) => {
           const visibleMenuItems = prevLayout
                .filter(item => item.visible && ["Archivos", "Usuario", "Configuracion"].includes(item.i))
                .map(item => item.x);

           const nextXPosition = visibleMenuItems.length ? Math.max(...visibleMenuItems) + 1 : 1;
           return prevLayout.map((item) =>
               item.i === itemId
                   ? {
                    ...item,
                        visible: !item.visible,
                        x: item.visible ? item.x : nextXPosition,
                        y: -1,
                    }
                    : item
            );
        });
    };

    return (
        <ContextoGrilla.Provider value={{ layout, setLayout, cambiarVisibilidadItem }}>
            {children}
        </ContextoGrilla.Provider>
    );
};