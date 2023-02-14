package Binding;

public class BoundLiteralExpression extends BoundExpression {
    final Type value;

    public  BoundLiteralExpression(final Type value) {
        this.value = value;
    }

    @Override
    public BoundNodeKind getKind() { return BoundNodeKind.LiteralExpr; }

    @Override
    public Object getType() { return value; }
}