package back_ptitulo.expresion;

import back_ptitulo.generated.PythonParser;
import back_ptitulo.generated.PythonParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;

public class ExpresionAPrograma extends PythonParserBaseVisitor<Programa> {

    public List<String> erroresSemanticos;
    private Programa prog = new Programa();

    @Override
    public Programa visitStatements(PythonParser.StatementsContext ctx) {

        System.out.println("Visitando statements!:");
        erroresSemanticos = new ArrayList<>();
        AlgoritmoParser expVisitor = new AlgoritmoParser();
        for(int i = 0 ; i < ctx.getChildCount(); i++){
            Expresion recibido = expVisitor.visit(ctx.getChild(i));
            System.out.println(recibido);
            prog.agregarExpresion(recibido);
        }
        System.out.print("Las expresiones encontradas son: ");
        prog.mostrarExpresiones();
        return prog;
    }

    public String enviarJson(){
        return prog.procesarPasos();
    }


}
