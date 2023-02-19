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

    public BoundBinaryOperator(final SKind skind, final BoundBinaryOperatorKind kind, final Type operandType, final Type resultType) {
        this(skind, kind, operandType, operandType, resultType);
    }

    public BoundBinaryOperator(final SKind skind, final BoundBinaryOperatorKind kind, final Type resultType) {
        this(skind, kind, resultType, resultType, resultType);
    }

    public SKind getSkind() { return this.skind; }
    public BoundBinaryOperatorKind getKind() { return this.kind; }
    public Type getResultType() { return this.resultType; }

    private static final BoundBinaryOperator[] operators = {
        new BoundBinaryOperator(SKind.Plus, BoundBinaryOperatorKind.Addition, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.Minus, BoundBinaryOperatorKind.Subtraction, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.Star, BoundBinaryOperatorKind.Multiplication, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.Div, BoundBinaryOperatorKind.Division, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.Mod, BoundBinaryOperatorKind.Modulus, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.Carrot, BoundBinaryOperatorKind.Exponent, new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.LAnd, BoundBinaryOperatorKind.LogicalAnd, new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LOr, BoundBinaryOperatorKind.LogicalOr, new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LEquals, BoundBinaryOperatorKind.LogicalEquals, new Type(Type.Types.Digit), new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LNotEquals, BoundBinaryOperatorKind.LogicalNotEquals, new Type(Type.Types.Digit), new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LEquals, BoundBinaryOperatorKind.LogicalEquals, new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LNotEquals, BoundBinaryOperatorKind.LogicalNotEquals, new Type(Type.Types.Boolean)),
        new BoundBinaryOperator(SKind.LEquals, BoundBinaryOperatorKind.LogicalEquals, new Type(Type.Types.Nil)),
        new BoundBinaryOperator(SKind.LNotEquals, BoundBinaryOperatorKind.LogicalNotEquals, new Type(Type.Types.Nil)),
        new BoundBinaryOperator(SKind.LEquals, BoundBinaryOperatorKind.LogicalEquals, new Type(Type.Types.Nil), new Type(Type.Types.Digit)),
        new BoundBinaryOperator(SKind.LNotEquals, BoundBinaryOperatorKind.LogicalNotEquals, new Type(Type.Types.Nil), new Type(Type.Types.Digit)),
    };

    public static BoundBinaryOperator bind(final SKind skind, final Type leftType, final Type rightType) {
        for (final BoundBinaryOperator op: operators) {
            if (op.skind == skind && op.leftType.type == leftType.type && op.rightType.type == rightType.type) {
                return op;
            }
        }

        return null;
    }
}
