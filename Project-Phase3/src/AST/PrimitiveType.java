package AST;

import java.util.ArrayList;
import java.util.List;

/**
 * An enum that shows type of a var
 */
public enum PrimitiveType implements Type {
    BOOL(".word", 1),
    INT(".word", 4),
    DOUBLE(".float", 8),
    //todo
    VOID("void", 0),
    STRING(".ascii", 6);

    private final String signature;
    private final int align;
    private boolean isArray;

    PrimitiveType(String signature, int align) {
        this.signature = signature;
        this.align = align;
    }

    public String getSignature() {
        return signature;
    }


    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    @Override
    public String toString() {
        return signature;
    }

    public int getAlign() {
        return align;
    }

    @Override
    public PrimitiveType getPrimitive() {
        return this;
    }

    public String getInitialValue() {
        String value = "";
        switch (this.signature) {
            case ".word":
                value = "0";
                break;
            case ".float":
                value = "0.0";
                break;
            case ".ascii":
                value = "\"\"";
                break;
        }
        return value;
    }

}
