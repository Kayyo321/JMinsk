package Binding;

import java.util.ArrayList;
import java.util.List;

import Parser.*;
import Lexer.*;

public class Binder {
    private final List<String> diagnostics = new ArrayList<>();

    public BoundExpression bindExpr(final Expression syntax) {
        return switch (syntax.getKind()) {
            case UnaryExpr -> bindUnaryExpression((UnaryExpr)syntax);
            case BinaryExpr -> bindBinaryExpression((BinaryExpr)syntax);
            default -> bindLiteralExpression((LiteralExpr)syntax);
        };
    }

    public List<String> getDiagnostics() { return this.diagnostics; }

    private BoundExpression bindBinaryExpression(final BinaryExpr syntax) {
        final BoundExpression boundLeft = bindExpr(syntax.getLeft());
        final BoundExpression boundRight = bindExpr(syntax.getRight());
        final BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(syntax.getOp().getKind(), boundLeft.getType(), boundRight.getType());

        if (boundOperator == null) {
            diagnostics.add("ERROR: binary operator: '" + syntax.getOp().getText() + "' is not defined for types: " + boundLeft.getType().type + ", and " + boundRight.getType().type + "...");
            return boundLeft;
        }

        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundExpression bindUnaryExpression(final UnaryExpr syntax) {
        final BoundExpression boundOperand = bindExpr(syntax.getOper());
        final BoundUnaryOperator boundOperatorKind = BoundUnaryOperator.bind(syntax.getOp().getKind(), boundOperand.getType());
        if (boundOperatorKind == null) {
            diagnostics.add("ERROR: unary operator: '" + syntax.getOp().getText() + "' is not defined for type: " + boundOperand.getType().type + "...");
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundExpression bindLiteralExpression(final LiteralExpr syntax) {
        final Type value = syntax.getToken().getValue() != null ? new Type(syntax.getToken().getValue().type, syntax.getToken().getValue().value) : new Type(null);

        return new BoundLiteralExpression(value);
    }
}
