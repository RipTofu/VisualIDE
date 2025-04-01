package back_ptitulo.expresion;

import java.util.ArrayList;
import java.util.List;

public class BloqueCondicional extends Expresion {
    private List<Condicional> condicionales = new ArrayList<>();

    public void agregarCondicionales(Condicional condicional) {
        condicionales.add(condicional);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("BloqueCondicional:\n");

        if (!condicionales.isEmpty()) {
            for (Condicional condicional : condicionales) {
                result.append(condicional.toString()).append("\n");
            }
        } else {
            result.append("[No hay condicionales en el bloque]");
        }

        return result.toString();
    }

    public List<Condicional> getCondicionales() {
        return condicionales;
    }
}
