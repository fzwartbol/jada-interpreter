package com.frederikzwartbol.jada;

import java.util.List;

interface JadaCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
