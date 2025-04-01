package back_ptitulo.expresion;

import back_ptitulo.generated.PythonParser;
import back_ptitulo.generated.PythonParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoParser extends PythonParserBaseVisitor<Expresion> {
    private List<Expresion> expresiones;
    private List<Asignacion> variables;

    public AlgoritmoParser() {
        this.variables = new ArrayList<>();
        this.expresiones = new ArrayList<>();
    }

    public List<Expresion> getExpresiones() {
        return expresiones;
    }

    @Override
    public Expresion visitCompound_stmt(PythonParser.Compound_stmtContext ctx) {
        System.out.println("Compuesto encontrado!: " + ctx.getText());
        return visitChildren(ctx);
    }

    //to-do: Recorrer names_expression (Condicion), expression, disjunction (or), conjunction (and), inversion (not), comparison(>, <, ==, !=)
    @Override
    public Expresion visitIf_stmt(PythonParser.If_stmtContext ctx) {
        if (ctx.getChildCount() > 1) {
            BloqueCondicional bloqueCondicional = new BloqueCondicional(); // Esto se retorna

            // Obtención del primer condicional: IF (named_expression) + block, por índice.
            IfElif condicional = new IfElif();
            Condicion condicion = (Condicion) visitNamed_expression(ctx.named_expression());
            condicional.setCondicion(condicion);
            condicional.setLinea("if " + ctx.named_expression().getText() + ":");
            // Obtencion del primer bloque (if):
            PythonParser.BlockContext blockContext = ctx.block();
            for (PythonParser.StatementContext statementContext : blockContext.statements().statement()){
                Expresion expr = visit(statementContext);
                condicional.agregarExpresion(expr);
            } System.out.println("El primer condicional es: " + condicional.toString());
            bloqueCondicional.agregarCondicionales(condicional);

            // Obtener los demás elementos del bloque (Elif, else)
            for (int i = 0; i <= ctx.getChildCount(); i++) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof PythonParser.Elif_stmtContext) {
                    bloqueCondicional.agregarCondicionales((IfElif) visit(child));
                    System.out.println("Elif agregado");
                }

                 if (child instanceof PythonParser.Else_blockContext) {
                    System.out.println("Se agregó un Else!");
                    bloqueCondicional.agregarCondicionales((Else) visit(child));
                }
            }
            System.out.println("--Resultado final--");
            System.out.println(bloqueCondicional.toString());
            return bloqueCondicional;
        }
        return visitChildren(ctx);
    }

    @Override
    public Expresion visitGroup(PythonParser.GroupContext ctx) {
        return visit(ctx.named_expression());
    }

    @Override
    public Expresion visitElif_stmt(PythonParser.Elif_stmtContext ctx) {
        System.out.println("Entrando a elif_stmt!");
        System.out.println("La linea del elif es: " + ctx.getText());
        IfElif condicional = new IfElif();
        condicional.setLinea("Elif " + ctx.named_expression().getText() + ":");
        Condicion cond = (Condicion) visitNamed_expression(ctx.named_expression());
        condicional.setCondicion(cond);
        PythonParser.BlockContext blockContext = ctx.block();
        for (PythonParser.StatementContext statementContext : blockContext.statements().statement()){
            Expresion expr = visit(statementContext);
            condicional.agregarExpresion(expr);
        }

        return condicional;
    }

    @Override
    public Expresion visitElse_block(PythonParser.Else_blockContext ctx) {
        System.out.println("Procesando bloque else");
        Else condicional = new Else();
        PythonParser.BlockContext blockContext = ctx.block();
        for (PythonParser.StatementContext statementContext : blockContext.statements().statement()){
            Expresion expr = visit(statementContext);
            condicional.agregarExpresion(expr);
        }
        condicional.setLinea("Else:");
        System.out.println(condicional.toString());
        return condicional;
    }

    @Override
    public Expresion visitNamed_expression(PythonParser.Named_expressionContext ctx) {

        // Revisar si hay resultado en nodos más abajo. Si no, evaluar condicion booleana.

        //Aquí se obtiene la Condicion (Clase Condicion)
        return visit(ctx.expression());
    }

    @Override
    public Expresion visitDisjunction(PythonParser.DisjunctionContext ctx) {
        if (ctx.getChildCount() > 1) {
            Comparacion comp = new Comparacion();
            Expresion valorIzq = ctx.getChild(0).accept(this);
            Expresion valorDer = ctx.getChild(2).accept(this);
            System.out.print("Se disyuntan los valores " + valorIzq.toString() + " " + valorDer.toString());

            comp.setOperador("or");
            comp.setIzq(valorIzq);
            comp.setDer(valorDer);
            comp.calcularVeracidad();

            System.out.println("Calculando veracidad de la operacion: " + comp.toString());
            System.out.println("La veracidad es: " + comp.getVeracidad());

            return comp;
        }
        return super.visitDisjunction(ctx);
    }

    @Override
    public Expresion visitConjunction(PythonParser.ConjunctionContext ctx) {
        //AND
        if (ctx.getChildCount() > 1) {
            Comparacion comp = new Comparacion();
            Expresion valorIzq = ctx.getChild(0).accept(this);
            Expresion valorDer = ctx.getChild(2).accept(this);
            System.out.print("Se conjugan los valores " + valorIzq.toString() + valorDer.toString());
            comp.setOperador("and");
            comp.setIzq(valorIzq);
            comp.setDer(valorDer);
            comp.calcularVeracidad();
            System.out.println("Calculando veracidad de la operacion: " + comp.toString());
            System.out.println("La veracidad es: " + comp.getVeracidad());
            return comp;
        }
        return super.visitConjunction(ctx);
    }

    @Override
    public Expresion visitInversion(PythonParser.InversionContext ctx) {
        //NOT
        if(ctx.getChildCount() > 1){
            System.out.println("Hay inversion.");
            ExprBooleana exprBooleana = new ExprBooleana();
            Expresion valorInvertido = ctx.getChild(1).accept(this);
            System.out.println("Se niega el valor: " + valorInvertido.toString() + " de tipo " + valorInvertido.getClass().getSimpleName());

            exprBooleana.setValorAlterado(valorInvertido);
            exprBooleana.calcularVeracidad();
            System.out.println("El valor resultante de la negación es: " + exprBooleana.getVeracidad());
            return exprBooleana;
        }
        return super.visitInversion(ctx);
    }

    @Override
    public Expresion visitComparison(PythonParser.ComparisonContext ctx) {
        if(ctx.getChildCount() == 2) {
            Comparacion comp = null;
            //El lado derecho de la operación puede ser más complejo.
            for (PythonParser.Compare_op_bitwise_or_pairContext compareCtx : ctx.compare_op_bitwise_or_pair()) {
                comp = (Comparacion) visitCompare_op_bitwise_or_pair(compareCtx);
            }

            // Recorrer hacia la izquierda hasta obtener el átomo izquierdo
            System.out.println("Visitando bitwise_or (izquierda)");
            Expresion i = visitBitwise_or(ctx.bitwise_or());
            //Evaluar si es una variable, y si es válido.
            if (i != null) {
                comp.setIzq(i);
            } else {
                System.out.println("Se declaró un elemento nulo. Error lol.");
            }
            System.out.println("Comparando... El resultado es: ");
            if (comp != null) {
                comp.calcularVeracidad();
                System.out.println("Veracidad calculada exitosamente. ");
            }
            System.out.println(comp.getVeracidad());
            System.out.println("La comparacion obtenida es: " + comp.toString());
            System.out.println("Los tipos de dato son: " + comp.getIzq().getClass().getSimpleName() + " y " + comp.getDer().getClass().getSimpleName());

            return comp;
        }
        return visitChildren(ctx);
    }

    @Override
    public Expresion visitCompare_op_bitwise_or_pair(PythonParser.Compare_op_bitwise_or_pairContext ctx) {
        Comparacion comp = new Comparacion();
        ParseTree lhs = ctx.getChild(0).getChild(0);
        ParseTree rhs = ctx.getChild(0).getChild(1);

        String simbolo = lhs.getText();
        Expresion der = visit(rhs);
        System.out.println("Operador: " + simbolo + " Der: " + der.toString());
        comp.setOperador(simbolo);
        comp.setDer(der);
        return comp;
    }

    @Override
    public Expresion visitList(PythonParser.ListContext ctx) {
        System.out.print("Se encontró una lista!");
        return super.visit(ctx);
    }

    @Override
    public Expresion visitBitwise_or(PythonParser.Bitwise_orContext ctx) {
        return super.visitBitwise_or(ctx);
    }

    @Override
    public Expresion visitBitwise_xor(PythonParser.Bitwise_xorContext ctx) {
        return super.visitBitwise_xor(ctx);
    }

    @Override
    public Expresion visitBitwise_and(PythonParser.Bitwise_andContext ctx) {
        return super.visitBitwise_and(ctx);
    }

    @Override
    public Expresion visitShift_expr(PythonParser.Shift_exprContext ctx) {
        return super.visitShift_expr(ctx);
    }

    //Encontrar sumas y restas. La mejor funcion hasta la fecha *u*
    @Override
    public Expresion visitSum(PythonParser.SumContext ctx) {

        // Retorna el directamente el termino. Puede ni siquiera ser una suma.
        if (ctx.getChildCount() == 1) {
            System.out.println("Termino encontrado. Recorriendo:");
            return visitTerm((PythonParser.TermContext) ctx.getChild(0));

        }

        // Si hay multiples componentes, es una suma. Recursivea.
        if (ctx.getChildCount() == 3) {
            Expresion izq = visitaSumaOTermino(ctx.getChild(0));
            String operador = ctx.getChild(1).getText();
            Expresion der = visitaSumaOTermino(ctx.getChild(2));
            System.out.println("---Componentes de la suma encontrada! Guardando " + izq.toString() + " y " + der.toString());
            Suma op = new Suma(ctx.getText(), izq, der, operador);
            System.out.println(op.toString());
            return op;
        }
        return null;
    }

    // Visita suma o termino
    private Expresion visitaSumaOTermino(ParseTree node) {
        if (node instanceof PythonParser.SumContext) {
            return visitSum((PythonParser.SumContext) node);
        } else if (node instanceof PythonParser.TermContext) {
            return visitTerm((PythonParser.TermContext) node);
        }
        return null;
    }


    //Encontrar terminos sueltos u otras operaciones
    @Override
    public Expresion visitTerm(PythonParser.TermContext ctx) {
        if (ctx.getChildCount() == 1) {
            //No es una operación, pero puede ser muchas cosas.
            return visitFactor(ctx.factor());
        } else if (ctx.STAR() != null) {
            Expresion izq = visitTerm(ctx.term());
            Expresion der = visitFactor(ctx.factor());
            Termino ter = new Termino(izq, der, "*");
            return ter;
        } else if (ctx.SLASH() != null) {
            Expresion izq = visitTerm(ctx.term());
            Expresion der = visitFactor(ctx.factor());
            Termino ter = new Termino(izq, der, "/");
            return ter;
        } else if (ctx.PERCENT() != null) {
            Expresion izq = visitTerm(ctx.term());
            Expresion der = visitFactor(ctx.factor());
            Termino ter = new Termino(izq, der, "%");
            return ter;
        }
        return null;
    }

    @Override
    public Expresion visitFactor(PythonParser.FactorContext ctx) {
        if (ctx.getChildCount() == 1) {
            return visitPower(ctx.power());
            /*
            Object valor = valorATipo(ctx.getText());
            String tipo = obtenerTipo(valor);
            Literal lit = new Literal(valor, tipo);
            return lit;
             */
        }
        return visit(ctx.getChild(0));
    }

    @Override
    public Expresion visitPower(PythonParser.PowerContext ctx) {
        if (ctx.await_primary() != null) {
            return visitAwait_primary(ctx.await_primary());
        }
        return visit(ctx.getChild(0));
    }

    @Override
    public Expresion visitAwait_primary(PythonParser.Await_primaryContext ctx) {
        if (ctx.primary() != null) {
            return visitPrimary(ctx.primary());
        }
        return visit(ctx.getChild(0));
    }

    @Override
    public Expresion visitPrimary(PythonParser.PrimaryContext ctx) {
        if (ctx.atom() != null) {
            return visitAtom(ctx.atom());
        }
        return visit(ctx.getChild(0));
    }

    @Override
    public Expresion visitAtom(PythonParser.AtomContext ctx) {
        System.out.println(ctx.getText());
        if(ctx.NAME() != null) {
            String nombreVar = ctx.NAME().getText();
            System.out.println("Esto es una variable: " + nombreVar);
            Variable variableLiteral = buscarVariable(nombreVar);
            if(variableLiteral != null) {
                System.out.println("La variable existe, su valor es: " + buscarValorVariable(nombreVar));
                System.out.println("Tipo de dato: " + buscarValorVariable(nombreVar).getClass().getSimpleName());
                variableLiteral.setValor(buscarValorVariable(nombreVar));
                System.out.println("Variable encontrada, valor establecido.");
                return variableLiteral;
            } else {
               System.out.println("¡Esta variable no ha sido declarada!");
            }
        } else if(ctx.NUMBER() != null) {
            System.out.println("Atomo encontrado! " + ctx.getText());
            Object valor = valorATipo(ctx.getText());
            System.out.println("El tipo de dato obtenido de " + valor.toString() + " es " + valor.getClass().getSimpleName());
            String tipo = obtenerTipo(valor);

            Literal lit = new Literal(valor, tipo);
            return lit;
        } else if (ctx.strings() != null) {
            return visitStrings(ctx.strings());
        } else if (ctx.getChild(0) instanceof PythonParser.GroupContext) {
            System.out.println("Atomo de grupo!");
        } else if(ctx.getChild(0).equals(ctx.TRUE()) || ctx.getChild(0).equals(ctx.FALSE())){
            System.out.println("Atomo TRUE");
            Object valor = valorATipo(ctx.getText());
            String tipo = obtenerTipo(valor);
            Literal lit = new Literal(valor, tipo);
            return lit;
        }

        else {
            System.out.println("Otro tipo de átomo!" + ctx.getText());
            return visit(ctx.getChild(0));
        }
        return visit(ctx.getChild(0));
    }

    @Override
    public Expresion visitStrings(PythonParser.StringsContext ctx) {
        if(ctx.string() != null){
            return visitString(ctx.string(0));
        }
        return null;
    }

    @Override
    public Expresion visitString(PythonParser.StringContext ctx) {
        if(ctx.STRING() != null) {
            String valorString = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
            System.out.println("String encontrado: " + valorString);
            return new Literal(valorString, "String");
        }
        return null;
    }

    @Override
    public Expresion visitSimple_stmts(PythonParser.Simple_stmtsContext ctx){
        return visit(ctx.simple_stmt(0));
    }



    // Visita nodo de asignación, genera el objeto de asignación y lo guarda en la lista de asignaciones
    @Override
    public Asignacion visitAssignment(PythonParser.AssignmentContext ctx) {
        String ID = ctx.getChild(0).getText();
        Expresion x = buscarSuma(ctx.getChild(2));
        System.out.println("Encontrado: " + x.toString());
        Expresion valor;
        valor = x;
        String tipo = obtenerTipo(valor);
        Asignacion a = guardarAsignacion(ID, valor, tipo, ctx.getText());
        return a;
    }

    private Variable buscarVariable(String nombreVar) {
        System.out.println("Buscando variable...");
        if (nombreVar != null) {
            if (!variables.isEmpty()) {
                Variable var = null;
                for (Asignacion v : variables) {
                    if (nombreVar.equals(v.getID())) {
                        System.out.println("Esta es una variable existente!" + v.getID());
                        var = new Variable(v.getID());
                        return var;
                    }
                }
            }
        }
        return null;
    }

    private Literal buscarValorVariable(String nombreVar) {
        for (int i = variables.size() - 1 ; i >= 0 ; i--) {
            Asignacion asignacion = variables.get(i);
            if(asignacion.getID().equals(nombreVar)) {
                System.out.println("Encontrado: " + asignacion.getValor());
                System.out.println("El valor de este dato es: " + asignacion.getValor().getClass().getSimpleName());
                if(asignacion.getValor() instanceof Literal) {
                    return new Literal(((Literal) asignacion.getValor()).getValor(), asignacion.getTipo());
                } else if (asignacion.getValor() instanceof Aritmetica) {
                    return new Literal (((Aritmetica) asignacion.getValor()).getResultado(), ((Aritmetica) asignacion.getValor()).getResultado().getTipo());
                } else {
                    System.out.println("No se encontró el tipo de dato a devolver.");
                    return null;
                }
            }
        }
        return null;
    }

    // Encuentra nodos de suma si corresponde (Suma comprende operaciones de suma y resta)
    private Expresion buscarSuma(ParseTree nodo) {
        if (nodo == null){
            return null;
        }
        if (nodo instanceof PythonParser.SumContext) {
            Expresion exp = visitSum((PythonParser.SumContext) nodo);
            if(exp != null) {
                return exp;
            }
        }

        for(int i = 0; i < nodo.getChildCount(); i++){
            Expresion resultado = buscarSuma(nodo.getChild(i));
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    private Asignacion guardarAsignacion(String ID, Expresion valor, String tipo, String linea){
        Asignacion var = new Asignacion(ID, valor, tipo, linea);
        Boolean existe = false;
        if (!variables.isEmpty()) {
            Asignacion remover = null;
            for (Asignacion v : variables) {
                if (v.getID().equals(ID)) {
                    remover = v;
                    existe = true;
                }
            }
            if (existe.equals(true)) {
                variables.remove(remover);
                variables.add(var);
            }
        }
        if(existe.equals(false)) {
            variables.add(var);
        }
        expresiones.add(var);
        System.out.println(var);
        return(var);
    }

    private Object valorATipo(String valor){
        if (valor.startsWith("\"") || valor.startsWith("'")) {
            return valor.substring(1, valor.length() - 1); //String
        } else if (valor.matches("-?\\d+(\\.\\d+)?")) {
            if (valor.contains(".")) {
                return Double.parseDouble(valor);//Float
            } else {
                return Integer.parseInt(valor);//Integer
            }
        } else if (valor.equals("True") || valor.equals("False")){
            return Boolean.parseBoolean(valor); //Bool
        }
        return valor;
    }

    private String obtenerTipo(Object valor){
        System.out.println("Tipo de dato:"+ valor.getClass().getSimpleName());
        if (valor.getClass() == Integer.class) {
            return "Integer";
        } else if (valor.getClass() == String.class) {
            return "String";
        } else if (valor.getClass() == Double.class) {
            return "Float";
        } else if (valor.getClass() == Boolean.class) {
            return "Boolean";
        } else if (valor.getClass() == Suma.class) {
            return ((Suma) valor).getResultado().getTipo();
        } else if (valor.getClass() == Termino.class) {
            return ((Termino) valor).getResultado().getTipo();
        } else if (valor.getClass() == Literal.class) {
            return ((Literal) valor).getTipo();
        } else if(valor.getClass() == Literal.class) {
            return "test";
        }
        return "Unidentified :c";
    }

    public void debug(){
        System.out.println("Expresiones:");
        for(Expresion exp : expresiones){
            System.out.println(exp.toString());
        }

        System.out.println("Variables declaradas:");
        for(Asignacion var : variables){
            System.out.println(var.toString());
        }
    }

    public List<Asignacion> getVariables() {
        return variables;
    }
}
