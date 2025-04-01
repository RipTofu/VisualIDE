import React from "react";
import './Consola.css'

const Consola = ({stdout, stderr}) =>{

    const formatearOutput = (output) => {

        if (output === undefined) {
            return (
                <React.Fragment>
                    Error al procesar código.
                </React.Fragment>
            );
        }

        return output.split('\n').map((linea, index) => (
            <React.Fragment key={index}>
                {linea}
                <br />
            </React.Fragment>
        ));
    }

    return (
            <div className="consola">
                <div className="stdout">{formatearOutput(stdout)} </div>
                <div className="stderr">{formatearOutput(stderr)}</div>
            </div>
    );
};

export default Consola;
