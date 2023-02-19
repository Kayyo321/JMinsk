package Binding;

import Diagnostics.DiagnosticBag;
import Diagnostics.VarSymbol;
import Parser.*;

import java.util.Map;
import java.util.Objects;

public class Binder {
    private final DiagnosticBag diagnostics = new DiagnosticBag();
    private final Map<VarSymbol, Object> variables;

    public Binder(final Map<VarSymbol, Object> variables) {
        this.variables = variables;
    }

    public BoundExpression bindExpr(final Expression syntax) throws Exception {
        return switch (syntax.getKind()) {
            case UnaryExpr -> bindUnaryExpression((UnaryExpr)syntax);
            case BinaryExpr -> bindBinaryExpression((BinaryExpr)syntax);
            case ParenExpr -> bindParenExpr((ParenExpr)syntax);
            case NameExpr -> bindNameExpr((NameExpr)syntax);
            case AssignmentExpr -> bindAssignmentExpr((AssignmentExpr)syntax);
            default -> bindLiteralExpression((LiteralExpr)syntax);
        };
    }

    private BoundExpression bindAssignmentExpr(final AssignmentExpr syntax) throws Exception {
        final String name = syntax.getId().getText();
        final BoundExpression boundExpression = bindExpr(syntax.getExpr());

        VarSymbol existingVariable = null;

        for (final VarSymbol vs: this.variables.keySet()) {
            if (Objects.equals(vs.name(), name)) {
                existingVariable = vs;
                break;
            }
        }

        if (existingVariable != null) {
            this.variables.remove(existingVariable);
        }

        final VarSymbol variable = new VarSymbol(name, boundExpression.getType());
        this.variables.put(variable, null);

        return new BoundAssignmentExpr(variable, boundExpression);
    }

    private BoundExpression bindNameExpr(final NameExpr syntax) {
        final String name = syntax.getId().getText();
        VarSymbol variable = null;

        for (final VarSymbol vs: this.variables.keySet()) {
            if (Objects.equals(vs.name(), name)) {
                variable = vs;
                break;
            }
        }

        if (variable == null) {
            diagnostics.reportUndefinedName(syntax.getId().getSpan(), name);
            return new BoundLiteralExpression(null);
        }

        return new BoundVarExpr(variable);
    }

    private BoundExpression bindParenExpr(final ParenExpr syntax) throws Exception {
        return bindExpr(syntax.getExpr());
    }

    public DiagnosticBag getDiagnostics() { return this.diagnostics; }
    //public Map<VarSymbol, Object> getVariables() { return this.variables; }

    private BoundExpression bindBinaryExpression(final BinaryExpr syntax) throws Exception {
        final BoundExpression boundLeft = bindExpr(syntax.getLeft());
        final BoundExpression boundRight = bindExpr(syntax.getRight());
        final BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(syntax.getOp().getKind(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(syntax.getOp().getSpan(), syntax.getOp().getText(), boundLeft.getType().type, boundRight.getType().type);
            return boundLeft;
        }

        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundExpression bindUnaryExpression(final UnaryExpr syntax) throws Exception {
        final BoundExpression boundOperand = bindExpr(syntax.getOper());
        final BoundUnaryOperator boundOperatorKind = BoundUnaryOperator.bind(syntax.getOp().getKind(), boundOperand.getType());
        if (boundOperatorKind == null) {
            diagnostics.reportUndefinedUnaryOperator(syntax.getOp().getSpan(), syntax.getOp().getText(), boundOperand.getType().type);
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundExpression bindLiteralExpression(final LiteralExpr syntax) {
        final Type value = syntax.getToken().getValue() != null ? new Type(syntax.getToken().getValue().type, syntax.getToken().getValue().value) : new Type(null);

        return new BoundLiteralExpression(value);
    }
}
