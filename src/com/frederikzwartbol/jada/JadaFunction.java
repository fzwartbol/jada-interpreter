package com.frederikzwartbol.jada;

import java.util.List;

/**
 * JadaFunction class. Represents a function.
 */
class JadaFunction implements JadaCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    private final boolean isInitializer;
    JadaFunction(Stmt.Function declaration, Environment closure,
                 boolean isInitializer) {
        this.closure = closure;
        this.declaration = declaration;
        this.isInitializer = isInitializer;
    }

    /**
     * Binds the function to an instance. So if the function is reassigned it still knows its original parent class.
     * see following example.jada:
     * 'class Foo { bar() { print "bar"; } }'
     * 'var foo = Foo();'
     * 'var bar = foo.bar;'
     * 'bar();'
     *
     * @param instance
     * @return
     */
    public JadaFunction bind(JadaInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new JadaFunction(declaration, environment, isInitializer);
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
            if (isInitializer) return closure.getAt(0, "this");
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0, "this");
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
