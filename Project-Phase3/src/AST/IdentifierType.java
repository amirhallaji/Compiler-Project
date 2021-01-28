package AST;

public class IdentifierType implements Type {
    private String signature;
    private int align = 10;

    public IdentifierType(String signature) {
        this.signature = signature;
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

}
