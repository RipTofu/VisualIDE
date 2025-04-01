import React, { useEffect, useRef } from 'react';
import hljs from 'highlight.js/lib/core';
import python from 'highlight.js/lib/languages/python';
import './Monokai.css';
import './LAZY.css';

hljs.registerLanguage('python', python);

const Highlighter = ({ codeLine, language = 'python', darkMode, fontSize, esLineaActual }) => {
    const codeRef = useRef(null);

    useEffect(() => {
        if (codeRef.current?.dataset.highlighted) {
            delete codeRef.current.dataset.highlighted;
        }
        hljs.highlightElement(codeRef.current);
    }, [codeLine, darkMode]);

    const lineStyle = {
        textAlign: 'center',
        margin: '0 auto',
        fontSize: `${fontSize * 1.5}px`,
        padding: '5px',
        display: 'inline-block',
        opacity: esLineaActual ? 1 : 0.6,
        transform: esLineaActual ? 'scale(1)' : 'scale(0.95)',
        transition: esLineaActual ? 'opacity 0.3s ease, transform 0.3s ease' : 'none',
    };

    return (
        <pre
            style={lineStyle}
            className={darkMode ? "monokai-theme" : "lazy-theme"}
        >
            <code ref={codeRef} className={`language-${language}`}>
                {codeLine}
            </code>
        </pre>
    );
};

export default Highlighter;
