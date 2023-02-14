package Binding;

public class BoundBinaryExpression extends BoundExpression {
    private final BoundExpression left;
    private final BoundBinaryOperator operatorKind;
    private final BoundExpression right;

    public BoundBinaryExpression(final BoundExpression left, final BoundBinaryOperator operatorKind, final BoundExpression right) {
        this.left = left;
        this.operatorKind = operatorKind;
        this.right = right;
    }

    public BoundBinaryOperator getOperatorKind() { return this.operatorKind; }
    public BoundExpression getLeft() { return this.left; }
    public BoundExpression getRight() { return this.right; }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BinaryExpr;
    }

    @Override
    public Object getType() {
        return this.left;
    }
}