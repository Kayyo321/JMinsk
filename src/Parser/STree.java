package Parser;

import Diagnostics.DiagnosticBag;
import Lexer.*;

public class STree {
    private final DiagnosticBag diagnostics;
    private final Expression root;
    private final SToken eof;

    public STree(final DiagnosticBag diagnostics, final Expression root, final SToken eof) {
        this.diagnostics = diagnostics;
        this.root = root;
        this.eof = eof;
    }

    public DiagnosticBag getDiagnostics() { return diagnostics; }
    public Expression getRoot() { return root; }
    public SToken getEof() { return eof; }

    public static STree getTree(final String text) {
        final Parser parser = new Parser(text);
        return parser.parse();
    }
}
