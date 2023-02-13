package Parser;

import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class ParenExpr extends Expression {
    private final SToken lParen;
    private final Expression expr;
    private final SToken rParen;

    public ParenExpr(final SToken lParen, final Expression expr, final SToken rParen) {

        this.lParen = lParen;
        this.expr = expr;
        this.rParen = rParen;
    }

    @Override
    public SKind getKind() { return SKind.ParenExpr; }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> nodes = new ArrayList<>();
        nodes.add(lParen);
        nodes.add(expr);
        nodes.add(rParen);
        return nodes;
    }

    public SToken getlParen() { return lParen; }
    public Expression getExpr() { return expr; }
    public SToken getrParen() { return rParen; }
}
