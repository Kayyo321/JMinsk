package Binding;

import Lexer.*;

public class BoundBinaryOperator {
    private final SKind skind;
    private final BoundBinaryOperatorKind kind;
    private final Type leftType;
    private final Type rightType;
    private final Type resultType;

    public BoundBinaryOperator(final SKind skind, final BoundBinaryOperatorKind kind, final Type leftType, final Type rightType, final Type resultType) {
        this.skind = skind;
        this.kind = kind;
        this.leftType = leftType;
        this.rightType = rightType;
        this.resultType = resultType;
    }

    public BoundBinaryOperator(final SKind skind, final BoundBinaryOperatorKind kind, final Type resultType) {
        this(skind, kind, resultType, resultType, resultType);
    }

    public SKind getSkind() { return this.skind; }
    public BoundBinaryOperatorKind getKind() { return this.kind; }
    public Type getResultType() { return this.resultType; }
    public Type getRightType() { return rightType; }
    public Type getLeftType() { return leftType; }

    private static final BoundBinaryOperator[] operators = {
        new BoundBinaryOperator(SKind.Plus, BoundBinaryOperatorKind.Addition, Type.Float),
        new BoundBinaryOperator(SKind.Minus, BoundBinaryOperatorKind.Subtraction, Type.Float),
        new BoundBinaryOperator(SKind.Star, BoundBinaryOperatorKind.Multiplication, Type.Float),
        new BoundBinaryOperator(SKind.Div, BoundBinaryOperatorKind.Division, Type.Float),
        new BoundBinaryOperator(SKind.Mod, BoundBinaryOperatorKind.Modulus, Type.Float),
        new BoundBinaryOperator(SKind.Carrot, BoundBinaryOperatorKind.Exponent, Type.Float),
        new BoundBinaryOperator(SKind.LAnd, BoundBinaryOperatorKind.LogicalAnd, Type.Boolean),
        new BoundBinaryOperator(SKind.LOr, BoundBinaryOperatorKind.Addition, Type.Boolean)
    };

    public static BoundBinaryOperator bind(final SKind skind, final Type leftType, final Type rightType) {
        for (final BoundBinaryOperator op: operators) {
            if (op.skind == skind && op.leftType == leftType && op.rightType == rightType) {
                return op;
            }
        }

        return null;
    }
}
