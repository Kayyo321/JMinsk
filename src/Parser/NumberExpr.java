package Parser;

import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class NumberExpr extends Expression {
    private final SToken token;

    public NumberExpr(final SToken token) {

        this.token = token;
    }

    @Override
    public SKind getKind() { return SKind.NumberExpr; }

    public SToken getToken() { return token; }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> nodes = new ArrayList<>();
        nodes.add(token);
        return nodes;
    }
}
