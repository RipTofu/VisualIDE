package back_ptitulo.service;

import back_ptitulo.expresion.ExpresionAPrograma;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ServiceParseador {

    public String parse(String input) {
        back_ptitulo.generated.PythonLexer lexer = new back_ptitulo.generated.PythonLexer(CharStreams.fromString(input));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        back_ptitulo.generated.PythonParser parser = new back_ptitulo.generated.PythonParser(tokenStream);

        ParseTree tree = parser.file_input();
        System.out.println("Parseando ahora!: " + tree.toStringTree(parser));
        ExpresionAPrograma visitor = new ExpresionAPrograma();

        visitor.visit(tree);
        return visitor.enviarJson();
    }

}
