package Binding;

import Diagnostics.VarSymbol;

public class BoundVarExpr extends BoundExpression {
    private final VarSymbol var;

    public BoundVarExpr(final VarSymbol var) {
        this.var = var;
    }

    public VarSymbol getVar() {
        return this.var;
    }

    @Override
    public BoundNodeKind getKind() { return BoundNodeKind.VarExpr; }

    @Override
    public Type getType() {
        return this.var.type();
    }
}
