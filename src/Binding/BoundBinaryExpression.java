package Binding;

public class BoundBinaryExpression extends BoundExpression {
    private final BoundExpression left;
    private final BoundBinaryOperator op;
    private final BoundExpression right;

    public BoundBinaryExpression(final BoundExpression left, final BoundBinaryOperator op, final BoundExpression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public BoundExpression getLeft() { return this.left; }
    public BoundBinaryOperator getOp() { return this.op; }
    public BoundExpression getRight() { return this.right; }

    @Override
    public BoundNodeKind getKind() { return BoundNodeKind.BinaryExpr; }

    @Override
    public Type getType() { return this.op.getResultType(); }
}