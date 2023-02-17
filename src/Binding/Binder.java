package Binding;

import Diagnostics.DiagnosticBag;
import Parser.*;

public class Binder {
    private final DiagnosticBag diagnostics = new DiagnosticBag();

    public BoundExpression bindExpr(final Expression syntax) {
        return switch (syntax.getKind()) {
            case UnaryExpr -> bindUnaryExpression((UnaryExpr)syntax);
            case BinaryExpr -> bindBinaryExpression((BinaryExpr)syntax);
            case ParenExpr -> bindExpr(((ParenExpr)syntax).getExpr());
            
            default -> bindLiteralExpression((LiteralExpr)syntax);
        };
    }

    public DiagnosticBag getDiagnostics() { return this.diagnostics; }

    private BoundExpression bindBinaryExpression(final BinaryExpr syntax) {
        final BoundExpression boundLeft = bindExpr(syntax.getLeft());
        final BoundExpression boundRight = bindExpr(syntax.getRight());
        final BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(syntax.getOp().getKind(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.reportUndefinedBinaryOperator(syntax.getOp().getSpan(), syntax.getOp().getText(), boundLeft.getType().type, boundRight.getType().type);
            return boundLeft;
        }

        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundExpression bindUnaryExpression(final UnaryExpr syntax) {
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
