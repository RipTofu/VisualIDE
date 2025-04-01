package back_ptitulo.expresion;

public class Else extends Condicional{

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Else ");

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
