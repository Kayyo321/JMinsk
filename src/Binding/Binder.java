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
        final BoundBinaryOperatorKind boundOperatorKind = bindBinaryOperatorKind(syntax.getOp().getKind());
        final BoundExpression boundRight = bindExpr(syntax.getRight());

        if (boundOperatorKind == null) {
            diagnostics.add("ERROR: unary operator: '" + syntax.getOp().getText() + "' is not defined for type: " + boundLeft.getType() + ", and " +  boundRight.getType() + "...");
            return null;
        }

        return new BoundBinaryExpression(boundLeft, boundOperatorKind, boundRight);
    }

    private BoundBinaryOperatorKind bindBinaryOperatorKind(final SKind kind) {
        return switch (kind) {
            case Plus -> BoundBinaryOperatorKind.Addition;
            case Minus -> BoundBinaryOperatorKind.Subtraction;
            case Star -> BoundBinaryOperatorKind.Multiplication;
            case Div -> BoundBinaryOperatorKind.Division;
            case Mod -> BoundBinaryOperatorKind.Modulus;
            case Carrot -> BoundBinaryOperatorKind.Exponent;
            case LAnd -> BoundBinaryOperatorKind.LogicalAnd;
            case LOr -> BoundBinaryOperatorKind.LogicalOr;
            default -> null;
        };
    }

    private BoundExpression bindUnaryExpression(final UnaryExpr syntax) {
        final BoundExpression boundOperand = bindExpr(syntax.getOper());
        final BoundUnaryOperatorKind boundOperatorKind = BoundUnaryOperator.bind(syntax.getOp().getKind(), boundOperand.getType());
        if (boundOperatorKind == null) {
            diagnostics.add("ERROR: unary operator: '" + syntax.getOp().getText() + "' is not defined for type: " + boundOperand.getType() + "...");
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundUnaryOperatorKind bindUnaryOperatorKind(final SKind kind) {
        return switch (kind) {
            case Plus -> BoundUnaryOperatorKind.Identity;
            case Minus -> BoundUnaryOperatorKind.Negation;
            case Bang -> BoundUnaryOperatorKind.LogicalNegation;
            default -> null;
        };
    }

    private BoundExpression bindLiteralExpression(final LiteralExpr syntax) {
        final Object value = syntax.getToken().getValue() != null ? syntax.getToken().getValue() : 0;
        return new BoundLiteralExpression(value);
    }
}
