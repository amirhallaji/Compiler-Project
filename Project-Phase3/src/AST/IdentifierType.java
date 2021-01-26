package AST;

public class IdentifierType implements Type {
    private String signature;
    private int align = 10;
    private boolean isArray;

    public IdentifierType(String signature, int align) {
        this.signature = signature;
        this.align = align;
    }

    public IdentifierType(String signature) {
        this.signature = signature;
        this.align = 10;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public int getAlign() {
        return align;
    }

    @Override
    public PrimitiveType getPrimitive() {
        return null;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }
}
