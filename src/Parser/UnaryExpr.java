package Parser;

import Lexer.SKind;
import Lexer.SToken;

import java.util.ArrayList;
import java.util.List;

public class UnaryExpr extends Expression {
    private final SToken op;
    private final Expression oper;

    public UnaryExpr(final SToken op, final Expression oper) {
        this.op = op;
        this.oper = oper;
    }

    @Override
    public SKind getKind() { return SKind.UnaryExpr; }

    public SToken     getOp()    { return op;    }
    public Expression getOper() { return oper; }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> nodes = new ArrayList<>();
        nodes.add(op);
        nodes.add(oper);
        return nodes;
    }
}
