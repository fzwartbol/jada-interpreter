package com.frederikzwartbol.jada;

import java.util.*;

/**
 * Parses modules and their dependencies.
 * First creates a map of all modules, adds new modules if found during parsing. Afterwards it creates a graph of modules and their dependencies to find the topological ordering.
 */
public class ModuleParser {
    private final static String MAIN_MODULE_NAME = "main";
    private final Map<String,Module> modules = new HashMap<>();
    private final ModuleGraph moduleGraph = new ModuleGraph();
    private boolean finishedParsing = false;

    /**
     * @param path Path to the main module.
     */
    public ModuleParser(String path) {
        parseModule(MAIN_MODULE_NAME, path);
        parse();
    }

    /**
     * Parse method: Parses all modules and their dependencies.
     * Checks if modules map contains a module which has not been parsed yet. Repeats process until all modules have
     * been parsed. If a new module is found during parsing, it is added to the map.
     */
    public void parse () {
        while (containsUnparsedModule()) {
            var unparsedModules = modules.entrySet().stream()
                    .filter(entry -> !entry.getValue().parsed).toList();

            unparsedModules.forEach(entry -> {parseModule(entry.getValue().moduleName, entry.getValue().modulePath);
                    });
        }
        this.finishedParsing = true;
    }

    private  List<Module> findDependencies(List<Stmt> statements) {
        List<Module> dependenciesModules = new ArrayList<>();
        for (Stmt statement : statements) {
            if (statement instanceof Stmt.Module stmt) {
                dependenciesModules.add(new Module(stmt.name.lexeme, (String) stmt.path.literal));
            }
        }
        return dependenciesModules;
    }

    private boolean containsUnparsedModule() {
        return modules.entrySet().stream().anyMatch(entry -> !entry.getValue().parsed);
    }

    private void parseModule(String moduleName, String modulePath) {
        Scanner scanner = new Scanner(new FileLoader().readFromPath(modulePath));
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        Module module = new Module(moduleName, modulePath, findDependencies(statements), statements, true);
        modules.put(moduleName, module);

        module.dependencies.forEach(dependency -> {
            modules.putIfAbsent(dependency.moduleName,new Module(dependency.moduleName, dependency.modulePath));
        });
    }


    /**
     * Module class: Contains all information about a module.
     */
    static class Module {
        private final String moduleName;
        private final String modulePath;
        private final List<Stmt> statements = new ArrayList<>();
        private final List<Module> dependencies = new ArrayList<>();
        private boolean parsed = false;

        public Module(String moduleName, String modulePath) {
            this.moduleName = moduleName;
            this.modulePath = modulePath;
        }

        public Module(String moduleName, String modulePath, List<Module> dependencies, List<Stmt> statements, boolean parsed) {
            this.moduleName = moduleName;
            this.modulePath = modulePath;
            this.dependencies.addAll(dependencies);
            this.statements.addAll(statements);
            this.parsed = parsed;
        }

        public List<Stmt> getStatements() {
            return statements;
        }

        @Override
        public String toString() {
            return "Module{" +
                    "moduleName='" + moduleName + '\'' +
                    ", modulePath='" + modulePath + '\'' +
                    ", dependencies=" + dependencies +
                    ", parsed=" + parsed +
                    '}';
        }
    }

    /**
     * ModuleGraph class: Creates a graph of modules and their dependencies.
     */
    class ModuleGraph {
        private final Map<Module, List<Module>> graph = new HashMap<>();

        public void addModule(Module module) {
            graph.put(module, new ArrayList<>());
        }

        public void addDependency(Module module, Module dependency) {
            graph.get(module).add(dependency);
        }

        public List<Module> getDependencies(Module module) {
            return graph.get(module);
        }


    }
    public List<Module> getTopologicalOrder() {
        modules.forEach((key, module) -> {
            moduleGraph.addModule(module);
        });
        modules.forEach((key, module) -> {
            module.dependencies.forEach(dependency -> {
                moduleGraph.addDependency(module, modules.get(dependency.moduleName));
            });
        });
        List<Module> topologicalOrder = new ArrayList<>();
        List<Module> visited = new ArrayList<>();
        modules.forEach((key, module) -> {
            if (!visited.contains(module)) {
                visit(module, visited, topologicalOrder, moduleGraph);
            }
        });
        return topologicalOrder;
    }

    void visit(Module module, List<Module> visited, List<Module> topologicalOrder, ModuleGraph graph) {
        visited.add(module);
        graph.getDependencies(module).forEach(dependency -> {
            if (!visited.contains(dependency)) {
                visit(dependency, visited, topologicalOrder, graph);
            }
        });
        topologicalOrder.add(module);
    }
}
