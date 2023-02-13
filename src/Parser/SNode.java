package Parser;

import Lexer.*;

import java.util.List;

public abstract class SNode {
    public abstract SKind getKind();

    public abstract List<SNode> getChildren();
}
