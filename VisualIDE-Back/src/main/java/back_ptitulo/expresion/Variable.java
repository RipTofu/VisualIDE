package back_ptitulo.expresion;

public class Variable extends Expresion{
    String ID;
    Literal valor;
    String linea;

    public Variable(String ID) {
        this.ID = ID;
    }
    @Override
    public String toString() {
        return valor.toString();
    }

    public String getID() {
        return ID;
    }

    public void setValor(Literal valor) {
        this.valor = valor;
    }
    public Literal getValor() {
        return valor;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLinea() {
        return linea;
    }
}
