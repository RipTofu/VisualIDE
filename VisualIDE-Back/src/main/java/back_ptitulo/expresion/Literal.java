package back_ptitulo.expresion;


import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Literal extends Expresion{
    Object valor;
    String tipo;

    public Literal(Object valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
        System.out.println("Se creó un literal cuyo tipo de valor es: " + this.getValor().getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return valor.toString();
    }
    public Object getValor() {
        if (valor instanceof Double) {
            return Math.round((Double) valor * 1000) / 1000.0;
        }
        return valor;
    }
    public String getTipo() {
        return tipo;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }


}
