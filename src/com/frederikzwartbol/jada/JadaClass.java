package com.frederikzwartbol.jada;

import java.util.List;
import java.util.Map;

public class JadaClass implements JadaCallable {
    final String name;
    private final Map<String, JadaFunction> methods;

    JadaClass(String name, Map<String, JadaFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    JadaFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        JadaFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(final Interpreter interpreter, final List<Object> arguments) {
        JadaInstance instance = new JadaInstance(this);
        JadaFunction initializer = methods.get("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
