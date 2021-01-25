package AST;

public class IdentifierType  implements Type{
    private String signature;
    private int align;
    private boolean isArray;

    public IdentifierType(String signature, int align) {
        this.signature = signature;
        this.align = align;
    }
    @Override
    public String getSignature() {
        return null;
    }

    @Override
    public int getAlign() {
        return 0;
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
