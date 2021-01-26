package codegen;

import AST.ASTNode;
import AST.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Descriptor of identifiers
 */
public class SymbolInfo implements Symbol {
    private ASTNode node;
    private Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SymbolInfo that = (SymbolInfo) o;

        if (node != null ? !node.equals(that.node) : that.node != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    SymbolInfo(ASTNode node) {
        this.node = node;
    }

    public ASTNode getNode() {
        return node;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String toString() {
        String str = "SymbolInfo: " + type ;
        return str;
    }
}