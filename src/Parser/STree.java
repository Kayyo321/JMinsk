package Parser;

import Diagnostics.DiagnosticBag;
import Lexer.*;

public record STree(DiagnosticBag diagnostics, Expression root, SToken eof) {
    public static STree getTree(final String text) {
        final Parser parser = new Parser(text);
        return parser.parse();
    }
}
