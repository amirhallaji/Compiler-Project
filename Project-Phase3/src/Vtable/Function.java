package Vtable;

import codegen.Scope;
import codegen.SymbolInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Function {
    String Name;
    SymbolInfo returnType;
    Scope scope;
    List<SymbolInfo> argumentsType = new ArrayList<>();
    String accessMode;
    public static Function currentFunction;

    public Function(String name) {
        Name = name;
    }

    public Function(String name, SymbolInfo returnType, Scope scope) {
        Name = name;
        this.returnType = returnType;
        this.scope = scope;
    }

    public Function(String name, SymbolInfo returnType, Scope scope, List<SymbolInfo> argumentsType) {
        Name = name;
        this.returnType = returnType;
        this.scope = scope;
        this.argumentsType = argumentsType;
    }


    public Function(String name, SymbolInfo returnType, List<SymbolInfo> argumentsType) {
        Name = name;
        this.returnType = returnType;
        this.argumentsType = argumentsType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public SymbolInfo getReturnType() {
        return returnType;
    }

    public void setReturnType(SymbolInfo returnType) {
        this.returnType = returnType;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public List<SymbolInfo> getArgumentsType() {
        return argumentsType;
    }

    @Override
    public String toString() {
        return "Function{" +
                "Name='" + Name + '\'' +
                ", returnType=" + returnType +
                ", scope=" + scope +
                '}';
    }

    public void setArgumentsType(List<SymbolInfo> argumentsType) {
        this.argumentsType = argumentsType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return Objects.equals(Name, function.Name) &&
                Objects.equals(scope, function.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, scope);
    }
}

