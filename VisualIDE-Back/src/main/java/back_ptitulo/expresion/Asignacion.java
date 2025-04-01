package back_ptitulo.expresion;

public class Asignacion extends Expresion{
    protected String linea;
    protected String ID;
    protected String tipo;
    protected Expresion valor;
    public Asignacion(String ID, Expresion valor, String tipo, String linea) {
        this.linea = linea;
        this.ID = ID;
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Asignacion{ linea=" + linea + ", ID=" + ID + ", tipo=" + tipo + ", valor=" + valor + '}';
    }

    public String getID() {
        return ID;
    }
    public String getTipo(){
        return tipo;
    }
    public Object getValor(){
        return valor;
    }

    public String getLinea(){
        return linea;
    }
}
