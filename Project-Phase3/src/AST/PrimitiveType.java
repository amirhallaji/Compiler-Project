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
    STRING(".ascii", 6),
    INPUTSTRING(".space", 12);
    private final String signature;
    private final int align;


    PrimitiveType(String signature, int align) {
        this.signature = signature;
        this.align = align;
    }

    public String getSignature() {
        return signature;
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
