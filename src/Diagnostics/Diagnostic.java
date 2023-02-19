package Diagnostics;

public record Diagnostic(TextSpan span, String msg) {
    @Override
    public String toString() {
        return this.msg;
    }
}
