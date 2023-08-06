package com.frederikzwartbol.jada;

import java.util.List;

class JadaFunction implements JadaCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    JadaFunction(Stmt.Function declaration, Environment closure) {
        this.closure = closure;
        this.declaration = declaration;
    }


    /**
     * Creates a new environment for the function (SCOPE) and executes the body.
     * For every function call a new environment is created. This is enables possibility of recursion,
     * and the variables are not shared between function calls.
     * @param interpreter
     * @param arguments
     * @return
     */
    @Override
    public Object call(Interpreter interpreter,
                       List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme,
                    arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
