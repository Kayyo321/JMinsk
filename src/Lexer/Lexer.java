package Lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String text;
    private int pos = 0;
    private final List<String> diagnostics = new ArrayList<>();

    public Lexer(final String text) {
         this.text = text;
    }

    public List<String> getDiagnostics() { return this.diagnostics; }

    private char getCur() {
        if (this.pos >= this.text.length()) {
            return '\0';
        }

        return this.text.charAt(this.pos);
    }

    private void next() { this.pos++; }

    public SToken nextToken() {
        if (this.pos >= this.text.length()) {
            return new SToken(SKind.Eof, this.pos, "\0", null);
        }

        if (Character.isDigit(getCur())) {
            final int start = this.pos;

            while (Character.isDigit(getCur())) {
                next();
            }

            final int length = this.pos;
            final String sub = text.substring(start, length);

            try {
                final int value = Integer.parseInt(sub);

                return new SToken(SKind.Number, start, sub, value);
            } catch (final NumberFormatException nfe) {
                diagnostics.add("ERROR: the number " + sub + " cannot be represented by an Int32...");
                return new SToken(SKind.Bad, start, sub, null);
            }
        } else if (Character.isWhitespace(getCur())) {
            final int start = this.pos;

            while (Character.isWhitespace(getCur())) {
                next();
            }

            final int length = this.pos;
            final String sub = text.substring(start, length);

            try {
                return new SToken(SKind.WhiteSpace, start, sub, null);
            } catch (final NumberFormatException nfe) {
                System.out.println(nfe.toString());
                return null;
            }
        }

        switch (getCur()) {
            case '+':
                return new SToken(SKind.Plus, this.pos++, "+", null);

            case '-':
                return new SToken(SKind.Minus, this.pos++, "-", null);

            case '*':
                return new SToken(SKind.Star, this.pos++, "*", null);

            case '/':
                return new SToken(SKind.Div, this.pos++, "/", null);

            case '%':
                return new SToken(SKind.Mod, this.pos++, "%", null);

            case '^':
                return new SToken(SKind.Carrot, this.pos++, "^", null);

            case '(':
                return new SToken(SKind.LParen, this.pos++, "(", null);

            case ')':
                return new SToken(SKind.RParen, this.pos++, ")", null);

            default:
                diagnostics.add("ERROR: bad character input: \"" + getCur() + "\"...");
                break;
        }

        return new SToken(SKind.Bad, this.pos++, this.text.substring(this.pos-1, this.pos), null);
    }
}
