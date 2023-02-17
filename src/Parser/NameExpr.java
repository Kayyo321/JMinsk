package Parser;

import Lexer.SKind;
import Lexer.SToken;

import java.util.ArrayList;
import java.util.List;

public class NameExpr extends Expression {
    private final SToken id;

    public NameExpr(final SToken id) {
        this.id = id;
    }

    public SToken getId() { return this.id; }

    @Override
    public SKind getKind() {
        return SKind.NameExpr;
    }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> children = new ArrayList<>();
        children.add(this.id);
        return children;
    }
}
