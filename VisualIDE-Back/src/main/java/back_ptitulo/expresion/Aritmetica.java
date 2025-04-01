package back_ptitulo.expresion;

public class Aritmetica extends Expresion{
    protected Expresion izq;
    protected Expresion der;
    protected String caracter;
    protected Literal resultado;
    protected String linea;

    @Override
    public String toString(){
        return izq.toString() + " " + caracter + " " + der.toString();
    }

    public Literal getResultado() {
        return resultado;
    }

    public Expresion getIzq() {
        return izq;
    }

    public Expresion getDer() {
        return der;
    }

    public String getCaracter() {
        return caracter;
    }

    public String getLinea() {
        return linea;
    }
}
