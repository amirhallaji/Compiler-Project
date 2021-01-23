package AST;

public interface Type {
    String getSignature();
    int getAlign();
    PrimitiveType getPrimitive();
}