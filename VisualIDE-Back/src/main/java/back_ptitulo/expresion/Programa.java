package back_ptitulo.expresion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.weaver.ast.Var;

import java.util.*;

public class Programa {
    public List<Expresion> expresiones;
    private List<Variable> variables = new ArrayList<>();
    private Set<String> variablesCompletadas = new HashSet<>();
    private int lineaActualID;

    public Programa(){
        this.expresiones = new ArrayList<>();
        lineaActualID = 0;
    }

    public void agregarExpresion(Expresion exp){
        expresiones.add(exp);
    }

    public String procesarPasos() {
        boolean variableExiste = false;
        List<Map<String, Object>> pasos = new ArrayList<>();
        System.out.println("Comenzando conversión a JSON de las siguientes expresiones:");
        for (Expresion exp : expresiones) {
            System.out.println(exp.toString());
        }

        for (Expresion exp : expresiones) {
            // Manejo de asignaciones
            if (exp instanceof Asignacion asignacion) {
                procesarAsignacion((Asignacion) exp, pasos);
            } else if (exp instanceof BloqueCondicional){
                procesarBloqueCondicional((BloqueCondicional) exp, pasos);
            }
        }

        return generarJson(pasos);
    }

    private String generarJson(List<Map<String, Object>> pasos) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pasos));
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pasos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private void procesarAsignacion(Asignacion asignacion, List<Map<String, Object>> pasos) {
        if (pasos.isEmpty() || !asignacion.getLinea().equals(pasos.get(pasos.size() - 1).get("linea"))) {
            lineaActualID++;
        }
        boolean variableExiste = variableExiste(asignacion.getID());
        System.out.println("Generando json apara la línea: " + asignacion.getLinea());

        // Paso 1 - Inicialización vacía
        Map<String, Object> paso1 = new HashMap<>();
        paso1.put("id", lineaActualID);
        paso1.put("blackboard", "");
        paso1.put("variables", variablesAString(false));
        paso1.put("linea", asignacion.getLinea());
        pasos.add(paso1);

        if (variableExiste) {
            modificarVariable(asignacion);
        } else {
            agregarVariable(asignacion);
        }
        if(contieneVariable(asignacion)) {
            // Paso 2 - Mostrar asignación (Con IDs de variable)
            Map<String, Object> paso2 = new HashMap<>();
            paso2.put("id", lineaActualID);
            paso2.put("blackboard", obtenerValorBlackboard(asignacion, false));
            paso2.put("variables", variablesAString(false));
            paso2.put("linea", asignacion.getLinea());
            pasos.add(paso2);
        }
        // Paso 3 - Mostrar asignacion con datos completos
        Map<String, Object> paso3 = new HashMap<>();
        paso3.put("id", lineaActualID);
        paso3.put("blackboard", obtenerValorBlackboard(asignacion, true));
        paso3.put("variables", variablesAString(false));
        paso3.put("linea", asignacion.getLinea());
        pasos.add(paso3);

        if (asignacion.getValor() instanceof Aritmetica arit) {
            procesarOperacionesComplejas(variableExiste, asignacion, pasos, asignacion.getLinea());
        } else {
            // Paso final, asignación completa
            Map<String, Object> pasoFinal = new HashMap<>();
            pasoFinal.put("id", lineaActualID);
            pasoFinal.put("blackboard", "");
            pasoFinal.put("variables", variablesAString(true));
            pasoFinal.put("linea", asignacion.getLinea());
            pasos.add(pasoFinal);
        }
    }

    private void procesarBloqueCondicional(BloqueCondicional bloqueCondicional, List<Map<String, Object>> pasos) {
        boolean bloqueEjecutado = false;
        System.out.println("Bloque ejecutado: " + bloqueEjecutado);
        for (Condicional condicional : bloqueCondicional.getCondicionales()) {
            System.out.println("Ejecutando: " + condicional.toString());
            if (bloqueEjecutado) {
                continue;
            }
            if (condicional instanceof IfElif ifElif) {
                if(!bloqueEjecutado) {
                    procesarIfElif(ifElif, pasos);
                    if (ifElif.getCondicion().getVeracidad()) {
                        bloqueEjecutado = true;
                    }
                }
            } else if (condicional instanceof Else elseBlock) {
                if (!bloqueEjecutado) {
                    procesarElse(elseBlock, pasos);
                }
            }
        }
    }

    private void procesarIfElif(IfElif ifElif, List<Map<String, Object>> pasos) {
        Condicion condicion = ifElif.getCondicion();
        System.out.println("Evaluando condicion: " + condicion);

        if (pasos.isEmpty() || !ifElif.getLinea().equals(pasos.get(pasos.size() - 1).get("linea"))) {
            lineaActualID++;
        }

        // Paso 1: Vacío
        Map<String, Object> pasoInicial = new HashMap<>();
        pasoInicial.put("id", lineaActualID);
        pasoInicial.put("blackboard", "");
        pasoInicial.put("variables", variablesAString(false));
        pasoInicial.put("linea", ifElif.getLinea());
        pasos.add(pasoInicial);

        // Paso 2: Condición completa inicial
        Map<String, Object> pasoCondicion = new HashMap<>();
        pasoCondicion.put("id", lineaActualID);
        pasoCondicion.put("blackboard", condicion.toString());
        pasoCondicion.put("variables", variablesAString(false));
        pasoCondicion.put("linea", ifElif.getLinea());
        pasos.add(pasoCondicion);

        // Paso 3+: Evaluaciones intermediasa
        if (condicion instanceof Comparacion comparacion) {
            evaluarCondicionPasoAPaso(ifElif, pasos);
        }

        // Ejecuta el bloque si la condicion es verdadera
        if (condicion.getVeracidad()) {
            for (Expresion expr : ifElif.bloque) {
                if (expr instanceof Asignacion asignacion) {
                    procesarAsignacion(asignacion, pasos);
                }
            }
        }
    }

    private void procesarElse(Else elseBlock, List<Map<String, Object>> pasos) {
        Map<String, Object> pasoElse = new HashMap<>();
        pasoElse.put("id", lineaActualID);
        pasoElse.put("blackboard", "");
        pasoElse.put("variables", variablesAString(false));
        pasoElse.put("linea", "Else");
        pasos.add(pasoElse);
        for (Expresion expr : elseBlock.bloque) {
            if (expr instanceof Asignacion asignacion) {
                procesarAsignacion(asignacion, pasos);
            }
        }
    }

    private void evaluarCondicionPasoAPaso(IfElif ifElif, List<Map<String, Object>> pasos) {
        Comparacion comparacion = (Comparacion) ifElif.getCondicion();
        String condicionStr = comparacion.toString();
        System.out.println("Tipos de dato: " + comparacion.getIzq().getClass().getSimpleName() + " y " + comparacion.getDer().getClass().getSimpleName());

        while(contieneComparacionesSinEvaluar(comparacion)) {
            condicionStr = evaluarProximoPasoComparacion(comparacion);
            System.out.print("Agregando paso para: " + condicionStr);
            Map<String, Object> pasoEvaluacion = new HashMap<>();
            pasoEvaluacion.put("id", lineaActualID);
            pasoEvaluacion.put("blackboard", condicionStr);
            pasoEvaluacion.put("variables", variablesAString(false));
            pasoEvaluacion.put("linea", ifElif.getLinea());
            pasos.add(pasoEvaluacion);
        }
        Map<String, Object> pasoFinal = new HashMap<>();
        System.out.println("Veracidad final: " + comparacion.getVeracidad());
        pasoFinal.put("id", lineaActualID);
        pasoFinal.put("blackboard", String.valueOf(comparacion.getVeracidad()));
        pasoFinal.put("variables", variablesAString(false));
        pasoFinal.put("linea", ifElif.getLinea());
        pasos.add(pasoFinal);
    }

    private boolean contieneComparacionesSinEvaluar(Comparacion comparacion) {
        return (comparacion.getIzq() instanceof Comparacion || comparacion.getDer() instanceof Comparacion);
    }

    private String evaluarProximoPasoComparacion(Comparacion comparacion) {
        if(comparacion.getIzq() instanceof Comparacion comparacionIzq) {
            comparacion.setIzq(new Literal (comparacionIzq.getVeracidad(), "boolean"));
            return comparacion.toString();
        } else if (comparacion.getIzq() instanceof Literal) {
            System.out.println("Nada que evaluar aquí");
        }
        if (comparacion.getDer() instanceof Comparacion comparacionDer) {
            comparacion.setDer(new Literal(comparacionDer.getVeracidad(), "boolean"));
            return comparacion.toString();
        } else if (comparacion.getDer() instanceof Literal) {
            System.out.println("Nada que evaluar aquí~");
        }

        comparacion.calcularVeracidad();
        return comparacion.toString();
    }


    private void procesarOperacionesComplejas(boolean variableExiste, Asignacion asignacion, List<Map<String, Object>> pasos, String linea){
        Object valor = asignacion.getValor();

        if (valor instanceof Aritmetica arit) {
            if(arit instanceof Suma) {
                procesarNodos((Suma) arit, pasos, linea, valor.toString());
            } else if (arit instanceof Termino){
                procesarNodos((Termino) arit, pasos, linea, valor.toString());
            }
            // Paso final, sin resultado. Ya se debe encontrar en la variable habilitada.
            //Modificar existencia de variable
            if(variableExiste){
                modificarVariable(asignacion);
            } else {
                agregarVariable(asignacion);
            }
            Map<String, Object> pasoFinal = new HashMap<>();
            pasoFinal.put("id", lineaActualID);
            pasoFinal.put("blackboard", "");
            pasoFinal.put("variables", variablesAString(true));
            pasoFinal.put("linea", (arit).getLinea());
            pasos.add(pasoFinal);
        }
    }

    private void procesarNodos(Aritmetica lado, List<Map<String, Object>> pasos, String linea, String blackboardActual) {
        if (!(lado.getIzq() instanceof Aritmetica)) {
            //Construir el string para mostrar los avances en la operación matemática
            blackboardActual = procesarNodoActual(lado, blackboardActual, pasos, linea);
        } else if (lado.getIzq() instanceof Aritmetica) {
            procesarNodoActual(lado, blackboardActual, pasos, linea);
        }
    }

    private String procesarNodoActual(Aritmetica arm, String blackboardActual, List<Map<String, Object>> pasos, String linea) {
        if (arm.getIzq() instanceof Aritmetica) {
            blackboardActual = procesarNodoActual((Aritmetica) arm.getIzq(), blackboardActual, pasos, linea);
        }

        if (arm.getDer() instanceof Aritmetica) {
            blackboardActual = procesarNodoActual((Aritmetica) arm.getDer(), blackboardActual, pasos, linea);
        }


        String[] partes = blackboardActual.split(" ");

        String resultado = arm.getResultado().toString();

        System.out.println("Ahora procesando el numero: " + arm.toString());
        String operador = arm.getCaracter();

        //if (arm.getIzq() instanceof Variable){Crear nuevo paso en el json}


        // Construir resultado por partez
        blackboardActual = construirBlackboard(partes, resultado, operador);
        System.out.println("Actualizado: " + blackboardActual);
        Map<String, Object> pasoIntermedio = new HashMap<>();
        pasoIntermedio.put("id", lineaActualID);
        pasoIntermedio.put("blackboard", blackboardActual);
        pasoIntermedio.put("variables", variablesAString(false));
        pasoIntermedio.put("linea", linea);
        pasos.add(pasoIntermedio);
        System.out.println("Paso agregado: " + pasoIntermedio);

        return blackboardActual;
    }

    private boolean contieneVariable(Expresion expresion) {
        System.out.println("Evaluando para: " + expresion.toString() + " de clase " + expresion.getClass().getSimpleName());
        if (expresion instanceof Variable) {
            return true;
        } else if (expresion instanceof Suma suma) {
            return contieneVariable(suma.getIzq()) || contieneVariable(suma.getDer());
        } else if (expresion instanceof Termino termino) {
            return contieneVariable(termino.getIzq()) || contieneVariable(termino.getDer());
        } else if (expresion instanceof Comparacion comparacion) {
            return contieneVariable(comparacion.getIzq()) || contieneVariable(comparacion.getDer());
        } else if (expresion instanceof ExprBooleana exprBooleana) {

            return contieneVariable(exprBooleana.getValorAlterado());
        } else if (expresion instanceof Asignacion asignacion) {
            return contieneVariable((Expresion) asignacion.getValor());
        }
        System.out.println("Retornando false!");
        return false;
    }

    private String construirBlackboard(String[] partes, String resultado, String operador) {
        StringBuilder blackboardActualizado = new StringBuilder();

        for (int i = 0; i < partes.length - 2; i += 2) {
            if (partes[i].matches("\\d+(\\.\\d+)?") && partes[i + 1].equals(operador) && partes[i + 2].matches("\\d+(\\.\\d+)?")) {
                for (int j = 0; j < i; j++) {
                    blackboardActualizado.append(partes[j]).append(" ");
                }
                blackboardActualizado.append(resultado);
                for (int j = i + 3; j < partes.length; j++) {
                    blackboardActualizado.append(" ").append(partes[j]);
                }
                break;
            }
        }

        return blackboardActualizado.toString().trim();
    }

    private Object obtenerValorBlackboardLiteral(Expresion valor) {
        if (valor instanceof Literal) {
            return ((Literal) valor).toString();
        }
        return "";
    }

    private String procesarExpresionRecursiva(Expresion expresion, boolean useValues) {
        if (expresion instanceof Literal) {
            Literal literal = (Literal) expresion;
            return String.valueOf(literal.getValor());
        } else if (expresion instanceof Variable) {
            Variable variable = (Variable) expresion;
            if (useValues && variable.getValor() != null) {
                return String.valueOf(variable.getValor().getValor());
            } else {
                return variable.getID();
            }
        } else if (expresion instanceof Suma) {
            Suma suma = (Suma) expresion;
            String ladoIzq = procesarExpresionRecursiva(suma.getIzq(), useValues);
            String ladoDer = procesarExpresionRecursiva(suma.getDer(), useValues);
            return ladoIzq + suma.getCaracter() + ladoDer;
        } else if (expresion instanceof Termino) {
            Termino termino = (Termino) expresion;
            String ladoIzq = procesarExpresionRecursiva(termino.getIzq(), useValues);
            String ladoDer = procesarExpresionRecursiva(termino.getDer(), useValues);
            return ladoIzq + termino.getCaracter() + ladoDer;
        }
        return "";
    }

    private String obtenerValorBlackboard(Expresion exp, boolean useValues) {
        System.out.println("Entrando a obtener el valor del blackboard!");

        if (exp instanceof Asignacion asignacion) {
            return procesarExpresionRecursiva((Expresion) asignacion.getValor(), useValues);
        } else if (exp instanceof Suma suma) {
            return procesarExpresionRecursiva(suma, useValues);
        } else if (exp instanceof Termino termino) {
            return procesarExpresionRecursiva(termino, useValues);
        } else if (exp instanceof Comparacion comparacion) {
            return procesarExpresionRecursiva(comparacion, useValues);
        } else if (exp instanceof ExprBooleana exprBooleana) {
            return "not " + procesarExpresionRecursiva(exprBooleana.getValorAlterado(), useValues);
        } else {
            return procesarExpresionRecursiva(exp, useValues);
        }
    }

    public String variablesAString(boolean isFinalStep) {
        System.out.println("Procesando variables: ");
        if (variables == null || variables.isEmpty()) {
            System.out.println("No hay variables que agregar este paso.");
            return "{}";
        }

        List<Map<String, Object>> jsonVariables = new ArrayList<>();

        // Itera sobre las variables y genera la estructura de JSON
        for (Variable variable : variables) {
            System.out.println("Variable: " + variable);
            Map<String, Object> variableMap = new HashMap<>();

            variableMap.put("ID", variable.getID());
            variableMap.put("linea", variable.getLinea());
            System.out.println("Valores agregados.");

            boolean completado = variablesCompletadas.contains(variable.getID()) || isFinalStep;
            if (isFinalStep) {
                variablesCompletadas.add(variable.getID()); // La variable se completa permanentemente
            }
            variableMap.put("Completado", completado);

            if (variable.getValor() != null) {
                System.out.println("La variable contiene un dato de tipo: " + variable.getValor().getClass().getSimpleName());
                Literal literal = variable.getValor();
                Map<String, Object> literalMap = new HashMap<>();

                Object literalValue = literal.getValor();
                if (literalValue instanceof Number || literalValue instanceof Boolean) {
                    literalMap.put("valor", literalValue);
                } else {
                    literalMap.put("valor", literalValue.toString());
                }
                literalMap.put("tipo", literal.getTipo());

                variableMap.put("valor", literalMap);
            }

            jsonVariables.add(variableMap);
            System.out.println("Se agregó correctamente el dato de la variable.");
        }

        // Convierte los objetos a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonVariables);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private boolean variableExiste(String id) {
        if (this.variables != null) {
            for (Variable variable : this.variables) {
                if (Objects.equals(variable.getID(), id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void agregarVariable(Asignacion asig) {
        Literal valor;
        if (asig.getValor() instanceof Aritmetica) {
            System.out.println("Agregando nueva variable aritmética");
            valor = ((Aritmetica) asig.getValor()).getResultado();
        } else if(asig.getValor() instanceof Variable) {
            valor = (Literal) ((Variable) asig.getValor()).getValor();
        }
        else {
            System.out.println("Agregando nueva variable literal");
            valor = (Literal) asig.getValor();
        }

        Variable variable = new Variable(asig.getID());
        variable.setValor(valor);
        variable.setLinea(asig.getLinea());
        variables.add(variable);
        System.out.println("Nueva variable agregada: " + asig.getID());
    }

    private void modificarVariable(Asignacion asig) {
        Literal valor;

        if (asig.getValor() instanceof Aritmetica) {
            valor = ((Aritmetica) asig.getValor()).getResultado();
        } else {
            System.out.println("Casteando a literal el valor: " + asig.getValor().getClass().getSimpleName());
            valor = (Literal) asig.getValor();
        }

        for (Variable variable : this.variables) {
            if (Objects.equals(variable.getID(), asig.getID())) {
                System.out.println("Variable encontrada, modificando valor.");
                variable.setValor(valor);
                variable.setLinea(asig.getLinea());
                return;
            }
        }
    }

    //Debug!
    public void mostrarExpresiones() {
        for(Expresion exp : expresiones){
            System.out.println("Exp:");
            System.out.println(exp.toString());
        }
    }
}
