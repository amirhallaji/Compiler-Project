package codegen;

import AST.ASTNode;
import AST.PrimitiveType;
import AST.Type;

/**
 * Descriptor of identifiers
 */
public class SymbolInfo implements Symbol {
    private ASTNode node;
    private Type type;
    private int dimensionArray = 0;


    public int getDimensionArray() {
        return dimensionArray;
    }

    public void setDimensionArray(int dimensionArray) {
        this.dimensionArray = dimensionArray;
    }

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

    public SymbolInfo(ASTNode node, Type type) {
        this.node = node;
        this.type = type;
    }

    public SymbolInfo(ASTNode node, PrimitiveType type) {
        this.node = node;
        this.type = type;
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
        String str = "SymbolInfo: " + type;
        return str;
    }
}