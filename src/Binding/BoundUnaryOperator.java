package Binding;

import Lexer.*;

public class BoundUnaryOperator {
    private final SKind skind;
    private final BoundUnaryOperatorKind kind;
    private final Type operandType;
    private final Type resultType;

    public BoundUnaryOperator(final SKind skind, final BoundUnaryOperatorKind kind, final Type operandType, final Type resultType) {
        this.skind = skind;
        this.kind = kind;
        this.operandType = operandType;
        this.resultType = resultType;
    }

    public BoundUnaryOperator(final SKind skind, final BoundUnaryOperatorKind kind, final Type operandType) {
        this(skind, kind, operandType, operandType);
    }

    public SKind getSkind() { return this.skind; }
    public BoundUnaryOperatorKind getKind() { return this.kind; }
    public Type getOperandType() { return this.operandType; }
    public Type getResultType() { return this.resultType; }

    private static final BoundUnaryOperator[] operators = {
            new BoundUnaryOperator(SKind.Bang, BoundUnaryOperatorKind.LogicalNegation, new Type(Type.Types.Boolean)),
            new BoundUnaryOperator(SKind.Plus, BoundUnaryOperatorKind.Identity, new Type(Type.Types.Float)),
            new BoundUnaryOperator(SKind.Minus, BoundUnaryOperatorKind.Negation, new Type(Type.Types.Float))
    };

    public static BoundUnaryOperator bind(final SKind skind, final Type type) {
        for (final BoundUnaryOperator op: operators) {
            if (op.getSkind() == skind && op.getOperandType() == type) {
                return op;
            }
        }

        return null;
    }
}
