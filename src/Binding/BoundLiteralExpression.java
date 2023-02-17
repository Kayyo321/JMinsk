package Binding;

public class BoundLiteralExpression extends BoundExpression {
    private final Type value;

    public  BoundLiteralExpression(final Type value) {
        this.value = value;
    }

    @Override
    public BoundNodeKind getKind() { return BoundNodeKind.LiteralExpr; }

    @Override
    public Type getType() { return value; }
}