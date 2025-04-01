package back_ptitulo.expresion;


public class Suma extends Aritmetica {

    public Suma(String linea, Expresion izq, Expresion der, String caracter) {
        this.linea = linea;
        this.izq = izq;
        this.der = der;
        this.caracter = caracter;
        this.resultado = calcularResultado();
        System.out.println("El resultado de la suma " + this.toString() + " es: " + this.resultado);
    }

    private Literal calcularResultado() {
        System.out.println("El lado izq es: " + this.getIzq().getClass().getSimpleName() + " El lado der es: " + this.getDer().getClass().getSimpleName());
        if (izq instanceof Literal && der instanceof Literal) {
            return calcularLiteral((Literal) izq, (Literal) der);
        } else if (izq instanceof Suma || der instanceof Suma || izq instanceof Termino || der instanceof Termino || izq instanceof Variable || der instanceof Variable) {
            Literal literalIzq = null;
            Literal literalDer = null;

            if (izq instanceof Suma) {
                Suma sumaIzq = (Suma) izq;
                literalIzq = sumaIzq.getResultado();
            } else if (izq instanceof Termino) {
                Termino terminoIzq = (Termino) izq;
                literalIzq = terminoIzq.getResultado();
            } else if (izq instanceof Literal) {
                literalIzq = (Literal) izq;
            } else if (izq instanceof Variable) {
                System.out.println("El lado izquierdo es una variable.");
                Variable variableIzq = (Variable) izq;
                literalIzq = variableIzq.getValor();
            }

            if (der instanceof Suma) {
                Suma sumaDer = (Suma) der;
                literalDer = sumaDer.getResultado();
            } else if (der instanceof Termino) {
                Termino terminoDer = (Termino) der;
                literalDer = terminoDer.getResultado();
            } else if (der instanceof Literal) {
                literalDer = (Literal) der;
            } else if (der instanceof Variable) {
                Variable variableDer = (Variable) der;
                literalDer = variableDer.getValor();
            }

            if (literalIzq != null && literalDer != null) {
                System.out.println("Calculando literal! Suma entre: " + literalIzq.getTipo() + " y " + literalDer.getTipo());
                return calcularLiteral(literalIzq, literalDer);
            }
        }
        return null;
    }

    private Literal calcularLiteral(Literal literalIzq, Literal literalDer) {
        Object valorIzq = literalIzq.getValor();
        Object valorDer = literalDer.getValor();

        if (valorIzq instanceof Literal) {
            valorIzq = ((Literal) valorIzq).getValor();
        }
        if (valorDer instanceof Literal) {
            valorDer = ((Literal) valorDer).getValor();
        }

        System.out.println("Los tipos al calcular son: " + valorIzq.getClass().getSimpleName() + " y " + valorDer.getClass().getSimpleName());

        if (valorIzq instanceof Number && valorDer instanceof Number) {
            Number numIzq = (Number) valorIzq;
            Number numDer = (Number) valorDer;
            Object resultadoValor = null;
            String resultadoTipo = "Float";

            System.out.println("Calculando! " + numIzq + " " + this.getCaracter() + " " + numDer);

            if (caracter.equals("+")) {
                resultadoValor = numIzq.doubleValue() + numDer.doubleValue();
            } else if (caracter.equals("-")) {
                resultadoValor = numIzq.doubleValue() - numDer.doubleValue();
            }

            if (valorIzq instanceof Integer && valorDer instanceof Integer) {
                resultadoTipo = "Integer";
                if (caracter.equals("+")) {
                    resultadoValor = numIzq.intValue() + numDer.intValue();
                } else if (caracter.equals("-")) {
                    resultadoValor = numIzq.intValue() - numDer.intValue();
                }
            }

            resultado = new Literal(resultadoValor, resultadoTipo);
            return resultado;
        }

        return null;
    }
}