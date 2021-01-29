package Vtable;

import codegen.SymbolInfo;

public class Field {
    String name;
    SymbolInfo symbolInfo;
    AccessMode accessMode = AccessMode.Public;
    ClassDecaf classDecaf = null;
    public static AccessMode currentAccessMode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolInfo getSymbolInfo() {
        return symbolInfo;
    }

    public void setSymbolInfo(SymbolInfo symbolInfo) {
        this.symbolInfo = symbolInfo;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public ClassDecaf getClassDecaf() {
        return classDecaf;
    }

    public void setClassDecaf(ClassDecaf classDecaf) {
        this.classDecaf = classDecaf;
    }

    public static AccessMode getCurrentAccessMode() {
        return currentAccessMode;
    }

    public static void setCurrentAccessMode(AccessMode currentAccessMode) {
        Field.currentAccessMode = currentAccessMode;
    }

    public Field(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", symbolInfo=" + symbolInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        return classDecaf != null ? classDecaf.equals(field.classDecaf) : field.classDecaf == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (classDecaf != null ? classDecaf.hashCode() : 0);
        return result;
    }
}
