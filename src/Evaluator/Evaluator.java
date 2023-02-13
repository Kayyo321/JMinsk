package Evaluator;

import Lexer.*;
import Parser.*;

public class Evaluator {
    private final Expression root;

    public Evaluator(final Expression root) {
        this.root = root;
    }

    public int eval() throws Exception {
        return evalExpr(root);
    }

    private int evalExpr(final Expression root) throws Exception {
        if (root instanceof NumberExpr) {
            final NumberExpr n = (NumberExpr)root;
            return (int)n.getToken().getValue();
        }

        if (root instanceof BinaryExpr) {
            final BinaryExpr b = (BinaryExpr)root;
            final int left = evalExpr(b.getLeft());
            final int right = evalExpr(b.getRight());

            switch (b.getOp().getKind()) {
                case Plus:
                    return left + right;

                case Minus:
                    return left - right;

                case Star:
                    return left * right;

                case Div:
                    return left / right;

                case Mod:
                    return left % right;

                case Carrot:
                    return (int)Math.pow((double)left, (double)right);

                default:
                    throw new Exception("unexpected binary operator: " + b.getOp().getKind());
            }
        }

        if (root instanceof ParenExpr) {
            final ParenExpr p = (ParenExpr)root;
            return evalExpr(p.getExpr());
        }

        throw new Exception("unexpected node: " + root.getKind());
    }

    private Expression getRoot() { return this.root; }
}
