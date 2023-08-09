package com.frederikzwartbol.jada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * The main class of the Jada interpreter.
 */
public class Jada {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
    static boolean hadRuntimeError = false;

    private static void runFile(String path) throws IOException {
        run(path);
        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String mainPath) {
        ModuleParser moduleParser = new ModuleParser(mainPath);
        // Stop if there was a syntax error.
        if (hadError) return;

        var orderedModules = moduleParser.getTopologicalOrder();

        Resolver resolver = new Resolver(interpreter);
        resolver.resolveModules(orderedModules);

        // Stop if there was a resolution error.
        if (hadError) return;

        System.out.println("Running " + mainPath + "...");
        interpreter.interpretModules(orderedModules);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where,
                               String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}