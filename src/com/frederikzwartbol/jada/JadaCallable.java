package com.frederikzwartbol.jada;

import com.frederikzwartbol.jada.Interpreter;

import java.util.List;

/**
 * Callable interface. Represents a callable object.
 */
public interface JadaCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
