package Binding;

import Diagnostics.VarSymbol;

public class BoundAssignmentExpr extends BoundExpression {
    private final VarSymbol var;
    private final BoundExpression expr;

    public BoundAssignmentExpr(final VarSymbol var, final BoundExpression expr) {
        this.var = var;
        this.expr = expr;
    }

    public BoundExpression getExpr() {
        return this.expr;
    }

    public VarSymbol getVar() {
        return this.var;
    }

    @Override
    public BoundNodeKind getKind() { return BoundNodeKind.AssignmentExpr; }

    @Override
    public Type getType() {
        return this.expr.getType();
    }
}
