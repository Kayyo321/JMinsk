package Parser;

import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpr extends Expression {
    private final Expression left;
    private final SToken op;
    private final Expression right;

    public BinaryExpr(final Expression left, final SToken op, final Expression right) {

        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public SKind getKind() { return SKind.BinaryExpr; }

    public Expression getLeft()  { return left;  }
    public SToken     getOp()    { return op;    }
    public Expression getRight() { return right; }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> nodes = new ArrayList<>();
        nodes.add(left);
        nodes.add(op);
        nodes.add(right);
        return nodes;
    }
}
