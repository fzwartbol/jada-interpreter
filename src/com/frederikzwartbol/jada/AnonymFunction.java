package com.frederikzwartbol.jada;

import java.util.List;

class AnonymFunction implements JadaCallable {
    private final Expr.AnonFunc declaration;
    private final Environment closure;
    AnonymFunction(Expr.AnonFunc declaration, Environment closure) {
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
        return "<fn " + "ano" + ">";
    }
}
