package Binding;

public class BoundUnaryExpression extends BoundExpression {
    private final BoundUnaryOperator operatorKind;
    private final BoundExpression operand;

    public BoundUnaryExpression(final BoundUnaryOperator operatorKind, final BoundExpression operand) {
        this.operatorKind = operatorKind;
        this.operand = operand;
    }

    public BoundUnaryOperator getOperatorKind() { return this.operatorKind; }
    public BoundExpression getOperand() { return this.operand; }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UnaryExpr;
    }

    @Override
    public Object getType() {
        return operand;
    }
}