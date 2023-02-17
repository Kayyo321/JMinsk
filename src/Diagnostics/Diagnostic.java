package Diagnostics;

public class Diagnostic {
    private final TextSpan span;
    private final String msg;

    public Diagnostic(final TextSpan span, final String msg) {
        this.span = span;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public TextSpan getSpan() {
        return this.span;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
