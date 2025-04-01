package back_ptitulo.expresion;

public class Termino extends Aritmetica{

    public Termino(Expresion izq, Expresion der, String caracter){
        this.izq = izq;
        this.der = der;
        this.caracter = caracter;
        this.resultado = calcularResultado();
    }

    private Literal calcularResultado() {
        System.out.println("Componentes del término:");
        System.out.println("Los tipos de dato son: " + izq.getClass().getSimpleName() + " y " + der.getClass().getSimpleName());
        System.out.println("izq: " + this.izq.toString() + " der: " + this.der.toString());

        if (izq instanceof Literal && der instanceof Literal) {
            return calcularLiteral((Literal) izq, (Literal) der);
        } else if (izq instanceof Aritmetica || der instanceof Aritmetica || izq instanceof Variable || der instanceof Variable) {
            System.out.println("Calculando para aritmética!");
            Literal literalIzq = null;
            Literal literalDer = null;

            if (izq instanceof Termino) {
                System.out.println("Extrayendo resultado del término izq: " + ((Termino) izq).getResultado());
                literalIzq = ((Termino) izq).getResultado();
            } else if (izq instanceof Suma) {
                literalIzq = ((Suma) izq).getResultado();
            } else if (izq instanceof Literal) {
                literalIzq = (Literal) izq;
            } else if (izq instanceof Variable) {
                literalIzq = ((Variable) izq).getValor();
            }

            if (der instanceof Termino) {
                System.out.println("Extrayendo resultado del termino der: " + ((Termino) der).getResultado());
                literalDer = ((Termino) der).getResultado();
            } else if (der instanceof Suma) {
                literalDer = ((Suma) der).getResultado();
            } else if (der instanceof Literal) {
                literalDer = (Literal) der;
            } else if (der instanceof Variable) {
                literalDer = ((Variable) der).getValor();
            }

            if (literalIzq != null && literalDer != null) {
                System.out.println("retornando pq no hay nulos acá");
                return calcularLiteral(literalIzq, literalDer);
            }
        }
        System.out.println("Puras gueas nulas");
        return null;
    }

    private Literal calcularLiteral(Literal literalIzq, Literal literalDer) {
        if (literalIzq.valor instanceof Number && literalDer.valor instanceof Number) {
            Number valorIzq = (Number) literalIzq.valor;
            Number valorDer = (Number) literalDer.valor;
            Object resultadoValor = null;
            String resultadoTipo = "Float"; //default

            switch (caracter) {
                case "*":
                    resultadoValor = valorIzq.doubleValue() * valorDer.doubleValue();
                    break;
                case "/":
                    resultadoValor = valorIzq.doubleValue() / valorDer.doubleValue();
                    break;
                case "%":
                    resultadoValor = valorIzq.doubleValue() % valorDer.doubleValue();
                    break;
            }

            if (literalIzq.valor instanceof Integer && literalDer.valor instanceof Integer) {
                resultadoTipo = "Integer";
                switch (caracter) {
                    case "*":
                        resultadoValor = valorIzq.intValue() * valorDer.intValue();
                        break;
                    case "/":
                        resultadoTipo = "Float";
                        resultadoValor = (double) valorIzq.intValue() / valorDer.intValue();
                        break;
                    case "%":
                        resultadoValor = valorIzq.intValue() % valorDer.intValue();
                        break;
                }
            }

            resultado = new Literal(resultadoValor, resultadoTipo);
            return resultado;
        }
        return null;
    }


}
