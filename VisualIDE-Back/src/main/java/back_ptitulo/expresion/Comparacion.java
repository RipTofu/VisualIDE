package back_ptitulo.expresion;

public class Comparacion extends Condicion {
    private Expresion Izq;
    private Expresion Der;
    private String operador; // "==", "!=", "<", ">", "and", "or"

    public Expresion getIzq() { return Izq; }
    public Expresion getDer() { return Der; }
    public String getOperador() { return operador; }

    public void setOperador(String operador) { this.operador = operador; }
    public void setIzq(Expresion izq) { Izq = izq; }
    public void setDer(Expresion der) { Der = der; }

    @Override
    public String toString() {
        return getIzq().toString() + " " + (operador != null ? operador : "") + " " + getDer().toString();
    }

    private Object obtenerValor(Expresion exp) {
        System.out.println("La clase de esta expresion es: " + exp.getClass().getSimpleName());
        if (exp instanceof Literal) {
            return ((Literal) exp).getValor();
        } else if (exp instanceof Variable) {
            Variable var = (Variable) exp;
            Literal varLiteral = var.getValor();
            if (varLiteral != null) {
                if (varLiteral.getValor() instanceof Literal) {
                    return ((Literal) varLiteral.getValor()).getValor();
                }
                return varLiteral.getValor();
            } else {
                throw new IllegalArgumentException("La variable " + var.getID() + " no tiene valor");
            }
        } else if (exp instanceof Comparacion) {
            ((Comparacion) exp).calcularVeracidad();
            return ((Comparacion) exp).getVeracidad();
        }
        return null;
    }

    private boolean esVerdadero(Object valor) {
        if (valor == null) return false;
        if (valor instanceof Boolean) return (Boolean) valor;
        if (valor instanceof Number) return ((Number) valor).doubleValue() != 0;
        if (valor instanceof String) return !((String) valor).isEmpty();
        return true;
    }

    public void calcularVeracidad() {
        Object valorIzq = obtenerValor(Izq);
        Object valorDer = obtenerValor(Der);

        if (operador == null) {
            veracidad = esVerdadero(valorIzq);
            return;
        }

        switch (operador) {
            case "==":
                veracidad = valorIzq.equals(valorDer);
                break;
            case "!=":
                veracidad = !valorIzq.equals(valorDer);
                break;
            case ">":
                veracidad = compararValores(valorIzq, valorDer) > 0;
                break;
            case "<":
                veracidad = compararValores(valorIzq, valorDer) < 0;
                break;
            case ">=":
                veracidad = compararValores(valorIzq, valorDer) >= 0;
                break;
            case "<=":
                veracidad = compararValores(valorIzq, valorDer) <= 0;
                break;
            case "and":
                veracidad = esVerdadero(valorIzq) && esVerdadero(valorDer);
                break;
            case "or":
                veracidad = esVerdadero(valorIzq) || esVerdadero(valorDer);
                break;
            default:
                throw new IllegalArgumentException("Operador no soportado: " + operador);
        }
    }

    private int compararValores(Object val1, Object val2) {
        if (val1 instanceof Number && val2 instanceof Number) {
            Double d1 = ((Number) val1).doubleValue();
            Double d2 = ((Number) val2).doubleValue();
            return d1.compareTo(d2);
        } else if (val1 instanceof String && val2 instanceof String) {
            return ((String) val1).compareTo((String) val2);
        } else if (val1 instanceof Boolean && val2 instanceof Boolean) {
            return ((Boolean) val1).compareTo((Boolean) val2);
        } else {
            throw new IllegalArgumentException("No se pueden comparar los valores de tipos: " + val1.getClass() + " y " + val2.getClass());
        }
    }
}