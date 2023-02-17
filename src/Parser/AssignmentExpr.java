package Parser;

import Lexer.SKind;
import Lexer.SToken;

import java.util.ArrayList;
import java.util.List;

public class AssignmentExpr extends Expression {
    private final SToken id;
    private final SToken equToken;
    private final Expression expression;

    public AssignmentExpr(final SToken id, final SToken equToken, final Expression expression) {
        this.id = id;
        this.equToken = equToken;
        this.expression = expression;
    }

    public SToken getId() { return this.id; }
    public SToken getEquToken() { return equToken; }
    public Expression getExpression() { return expression; }

    @Override
    public SKind getKind() {
        return SKind.AssignmentExpr;
    }

    @Override
    public List<SNode> getChildren() {
        final List<SNode> children = new ArrayList<>();
        children.add(this.id);
        children.add(this.equToken);
        children.add(this.expression);
        return children;
    }
}
