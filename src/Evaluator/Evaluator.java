package Evaluator;

import Binding.*;
import Diagnostics.VarSymbol;

import java.util.Map;

public class Evaluator {
    private final BoundExpression root;
    private final Map<VarSymbol, Object> variables;

    public Evaluator(final BoundExpression root, final Map<VarSymbol, Object> variables) {
        this.root = root;
        this.variables = variables;
    }

    public Object eval() throws Exception {
        return evalExpr(root);
    }

    private Object evalExpr(final BoundExpression root) throws Exception {
        if (root instanceof final BoundLiteralExpression n) {
            return n.getType().value;
        }

        if (root instanceof final BoundVarExpr v) {
            return this.variables.get(v.getVar());
        }

        if (root instanceof final BoundAssignmentExpr a) {
            final Object value = evalExpr(a.getExpr());
            this.variables.put(a.getVar(), value);
            return value;
        }

        if (root instanceof final BoundUnaryExpression u) {
            final Object oper = evalExpr(u.getOperand());

            return switch (u.getOperatorKind().getKind()) {
                case Negation -> -(float) oper;
                case Identity -> oper;
                case LogicalNegation -> !(boolean) oper;
            };
        }

        if (root instanceof final BoundBinaryExpression b) {
            final Object left = evalExpr(b.getLeft());
            final Object right = evalExpr(b.getRight());

            return switch (b.getOp().getKind()) {
                case Addition -> (float) left + (float) right;
                case Subtraction -> (float) left - (float) right;
                case Multiplication -> (float) left * (float) right;
                case Division -> (float) left / (float) right;
                case Modulus -> (float) left % (float) right;
                case LogicalAnd -> (boolean) left && (boolean) right;
                case LogicalOr -> (boolean) left || (boolean) right;
                case LogicalEquals -> left.equals(right);
                case LogicalNotEquals -> !left.equals(right);
                case Exponent -> (float) Math.pow((double) left, (double) right);
            };
        }

        throw new Exception("unexpected node: " + root.getKind());
    }

    public Map<VarSymbol, Object> getVariables() { return this.variables; }
    private BoundExpression getRoot() { return this.root; }
}
