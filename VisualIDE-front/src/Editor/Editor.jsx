import React, { useRef, useEffect } from 'react';
import { Editor, useMonaco } from '@monaco-editor/react';
import Monokai from 'monaco-themes/themes/Monokai.json';
import LAZY from 'monaco-themes/themes/LAZY.json'

const MEditor = ({ contenido, setContenido, darkMode, fontSize }) => {
    const editorRef = useRef(null);
    const monaco= useMonaco();

    useEffect(() => {
        if (monaco) {
            monaco.editor.defineTheme('Monokai', Monokai);
            monaco.editor.defineTheme('LAZY', LAZY);
            if (darkMode) {
                monaco.editor.setTheme('Monokai');
            } else {
                monaco.editor.setTheme('LAZY');
            }
        }
    }, [monaco, darkMode]);

    useEffect(() => {
        if(editorRef.current) {
            editorRef.current.updateOptions({
                fontSize: fontSize,
            })
        }
    }, [fontSize]);


    const handleEditorDidMount = (editor) => {
        editorRef.current = editor;
        editor.focus();
    };

    return (

        <div style={{ flex: 1 }}>
            <Editor
                height="92%"
                defaultLanguage="python"
                defaultValue='print("Hola mundo!")'
                value={contenido}
                onChange={(value) => setContenido(value  || '')}
                onMount={handleEditorDidMount}
                options={ {
                    fontSize: fontSize,
                }}
            />
        </div>
    );
};

export default MEditor;