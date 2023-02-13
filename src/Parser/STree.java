package Parser;

import Lexer.*;

import java.util.List;

public class STree {
    private final List<String> diagnostics;
    private final Expression root;
    private final SToken eof;

    public STree(final List<String> diagnostics, final Expression root, final SToken eof) {
        this.diagnostics = diagnostics;
        this.root = root;
        this.eof = eof;
    }

    public List<String> getDiagnostics() { return diagnostics; }
    public Expression getRoot() { return root; }
    public SToken getEof() { return eof; }

    public static STree getAST(final String text) {
        final Parser parser = new Parser(text);
        return parser.parse();
    }
}
