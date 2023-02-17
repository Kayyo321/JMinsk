package Diagnostics;

import Binding.Type;
import Lexer.SKind;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticBag {
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    public void report(final TextSpan span, final String msg) {
        diagnostics.add(new Diagnostic(span, msg));
    }

    public List<Diagnostic> getDiagnostics() {return this.diagnostics;}

    public void addAll(final DiagnosticBag db) {
        this.diagnostics.addAll(db.getDiagnostics());
    }

    public boolean isEmpty() {
        return this.diagnostics.isEmpty();
    }

    public void reportInvalidNumber(final TextSpan textSpan, final String sub, final Type.Types type) {
        final String msg = "ERROR: The number " + sub + " isn't valid type " + type;
        report(textSpan, msg);
    }

    public void reportBadChar(final int pos, final char cur) {
        final String msg = "ERROR: Bad character input: '" + cur + "'";
        report(new TextSpan(pos, 1), msg);
    }

    public void reportUnexpectedToken(final TextSpan span, final SKind found, final SKind wanted) {
        final String msg = "ERROR: Unexpected token: <" + found + ">, expected <" + wanted + ">";
        report(span, msg);
    }

    public void reportUndefinedUnaryOperator(final TextSpan span, final String text, final Type.Types type) {
        final String msg = "ERROR: Unary operator '" + text + "' is not defined for type " + type;
        report(span, msg);
    }

    public void reportUndefinedBinaryOperator(final TextSpan span, final String text, final Type.Types lType, final Type.Types rType) {
        final String msg = "ERROR: Binary operator '" + text + "' is not defined between types " + lType + ", and " + rType;
        report(span, msg);
    }
}
