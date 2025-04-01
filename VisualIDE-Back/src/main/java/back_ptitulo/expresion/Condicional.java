package back_ptitulo.expresion;

import java.util.ArrayList;
import java.util.List;

public class Condicional extends Expresion{
   protected List<Expresion> bloque = new ArrayList<>();
   protected String linea;

   protected void agregarExpresion(Expresion exp){
      this.bloque.add(exp);
   }

   public void setLinea(String linea) {
      this.linea = linea;
   }

   public String getLinea() {
      return linea;
   }
}
