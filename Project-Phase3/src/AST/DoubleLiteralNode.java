package AST;

public class DoubleLiteralNode extends Literal {
    private float value;


    public DoubleLiteralNode(float value) {
        super(PrimitiveType.FLOAT);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
