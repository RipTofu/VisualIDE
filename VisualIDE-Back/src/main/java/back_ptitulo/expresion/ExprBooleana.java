package back_ptitulo.expresion;

public class ExprBooleana extends Condicion{
    Expresion valorAlterado;

    public void setValorAlterado(Expresion valorAlterado) {
        this.valorAlterado = valorAlterado;
    }

    public void calcularVeracidad () {
        boolean veracidadFinal = !this.veracidad;
        setVeracidad(veracidadFinal);
    }

    public Expresion getValorAlterado() {
        return valorAlterado;
    }

    @Override
    public String toString() {
        return " not " + valorAlterado;
    }
}
