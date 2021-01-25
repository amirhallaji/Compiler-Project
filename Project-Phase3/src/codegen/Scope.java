package codegen;

import java.util.HashMap;

public class Scope {

    private String name;
    private HashMap<String, Symbol> variables;

    public Scope(String name) {
        this.name = name;
        this.variables = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        return name != null ? name.equals(scope.name) : scope.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public HashMap<String, Symbol> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return "Scope{" +
                "name='" + name + '\'' +
                '}';
    }
}
