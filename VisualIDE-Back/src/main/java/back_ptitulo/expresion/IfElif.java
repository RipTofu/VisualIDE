package back_ptitulo.expresion;

import java.util.List;

public class IfElif extends Condicional{
    private Condicion condicion;

    public Condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("IfElif: ").append(this.condicion.toString()).append(" ");

        if (bloque != null && !bloque.isEmpty()) {
            result.append("Bloque: [");
            for (Expresion expr : bloque) {
                result.append(expr.toString()).append(", ");
            }
            result.setLength(result.length() - 2);
            result.append("]");
        } else {
            result.append("[No hay líneas que ejecutar al interior del bloque!]");
        }

        return result.toString();
    }
}
