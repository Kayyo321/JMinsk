package Lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String text;
    private int pos = 0;
    private final List<String> diagnostics = new ArrayList<>();

    private Map<String, SKind> keywords = new HashMap<String, SKind>();

    public Lexer(final String text) {
         this.text = text;

         // Initialize Keywords
         keywords.put("true", SKind.TrueKeyword);
         keywords.put("false", SKind.FalseKeyword);
    }

    public List<String> getDiagnostics() { return this.diagnostics; }

    private char getCur() {
        return peek(0);
    }

    private char getAhead() {
        return peek(1);
    }

    private char peek(final int offset) {
        if (this.pos + offset >= this.text.length()) {
            return '\0';
        }

        return this.text.charAt(this.pos + offset);
    }

    private void next() { this.pos++; }

    public SToken lex() {
        if (this.pos >= this.text.length()) {
            return new SToken(SKind.Eof,"\0", null, this.pos-1, this.pos);
        }

        if (Character.isDigit(getCur())) {
            final int start = this.pos;

            while (Character.isDigit(getCur()) || getCur() == '.') {
                next();
            }

            final int length = this.pos;
            final String sub = text.substring(start, length);

            try {
                final float value = Float.parseFloat(sub);

                return new SToken(SKind.Number, sub, value, start, length);
            } catch (final NumberFormatException nfe) {
                diagnostics.add("ERROR: the number " + sub + " cannot be represented by an 32-bit floating-point number...");
                return new SToken(SKind.Bad, sub, null, start, length);
            }
        } else if (Character.isWhitespace(getCur())) {
            final int start = this.pos;

            while (Character.isWhitespace(getCur())) {
                next();
            }

            final int length = this.pos;
            final String sub = text.substring(start, length);

            return new SToken(SKind.WhiteSpace, sub, null, start, length);
        } else if (Character.isLetter(getCur())) {
            final int start = this.pos;

            while (Character.isLetter(getCur()) || getCur() == '_' || Character.isDigit(getCur())) {
                next();
            }

            final int length = this.pos;
            final String sub = text.substring(start, length);

            Object value = sub;

            final SKind sk = keywords.getOrDefault(sub, SKind.Identifier);
            if (sk == SKind.TrueKeyword || sk == SKind.FalseKeyword) {
                value = (sk == SKind.TrueKeyword);
            }

            return new SToken(sk, sub, value, start, length);
        }

        switch (getCur()) {
            case '+' -> {
                return new SToken(SKind.Plus, "+", null, this.pos++, this.pos);
            }
            case '-' -> {
                return new SToken(SKind.Minus, "-", null, this.pos++, this.pos);
            }
            case '*' -> {
                return new SToken(SKind.Star, "*", null, this.pos++, this.pos);
            }
            case '/' -> {
                return new SToken(SKind.Div, "/", null, this.pos++, this.pos);
            }
            case '%' -> {
                return new SToken(SKind.Mod, "%", null, this.pos++, this.pos);
            }
            case '^' -> {
                return new SToken(SKind.Carrot, "^", null, this.pos++, this.pos);
            }
            case '(' -> {
                return new SToken(SKind.LParen, "(", null, this.pos++, this.pos);
            }
            case ')' -> {
                return new SToken(SKind.RParen, ")", null, this.pos++, this.pos);
            }
            case '!' -> {
                return new SToken(SKind.Bang, "!", null, this.pos++, this.pos);
            }
            case '&' -> {
                if (getAhead() == '&') {
                    return new SToken(SKind.LAnd, "&&", null, this.pos += 2, this.pos);
                }
            }
            case '|' -> {
                if (getAhead() == '|') {
                    return new SToken(SKind.LOr, "||", null, this.pos += 2, this.pos);
                }
            }
            default -> {
                diagnostics.add("ERROR: bad character input: \"" + getCur() + "\"...");
            }
        }

        return new SToken(SKind.Bad, this.text.substring(this.pos, this.pos++), null, this.pos - 1, this.pos);
    }
}
