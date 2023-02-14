package Parser;

import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class LiteralExpr extends Expression {
    private final SToken token;
    private final Object value;

    public LiteralExpr(final SToken token, final Object value) {
        this.token = token;
        this.value = value;
    }

    public LiteralExpr(final SToken token) {
        this(token, token.getValue());
    }

    @Override
    public SKind getKind() { return SKind.NumberExpr; }

    public SToken getToken() { return this.token; }

    public Object getValue() { return this.value; }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> nodes = new ArrayList<>();
        nodes.add(token);
        return nodes;
    }
}
