package Evaluator;

import Binding.*;
import Diagnostics.VarSymbol;

import java.util.Map;
import java.util.Objects;

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
            for (final VarSymbol vs: this.variables.keySet()) {
                if (Objects.equals(vs.name(), v.getVar().name())) {
                    return this.variables.get(vs);
                }
            }

            throw new Exception("Variable call wasn't resolved: " + v.getVar());
        }

        if (root instanceof final BoundAssignmentExpr a) {
            final Object value = evalExpr(a.getExpr());

            // Cleanup old variable (if any).
            final String dupName = a.getVar().name();
            final Type newVarType = new Type(a.getType().type, a.getType().value);
            final VarSymbol newVar = new VarSymbol(a.getVar().name(), newVarType);

            for (final VarSymbol vs: variables.keySet()) {
                if (Objects.equals(vs.name(), dupName)) {
                    if (variables.get(vs) != a.getVar()) {
                        variables.remove(vs);
                    }
                }
            }

            this.variables.put(newVar, value);

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

            // Null values can be used for logical operations.
            if (b.getOp().getKind() != BoundBinaryOperatorKind.LogicalEquals || b.getOp().getKind() != BoundBinaryOperatorKind.LogicalNotEquals) {
                if (left == null && right == null) {
                    throw new Exception("Left hand, and right hand side of binary-expression were null");
                } else if (left == null) {
                    throw new Exception("Left hand side of binary-expression was null");
                } else if (right == null) {
                    throw new Exception("Right hand side of binary-expression was null");
                }
            }

            return switch (b.getOp().getKind()) {
                case Addition -> (float) left + (float) right;
                case Subtraction -> (float) left - (float) right;
                case Multiplication -> (float) left * (float) right;
                case Division -> (float) left / (float) right;
                case Modulus -> (float) left % (float) right;
                case LogicalAnd -> (boolean) left && (boolean) right;
                case LogicalOr -> (boolean) left || (boolean) right;
                case LogicalEquals -> (left == null || right == null) || left.equals(right);
                case LogicalNotEquals -> (left == null || right == null) || !left.equals(right);
                case Exponent -> (float) Math.pow((double) left, (double) right);
            };
        }

        throw new Exception("Unexpected node: " + root.getKind());
    }
}
