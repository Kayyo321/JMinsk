package Lexer;

import Binding.Type;
import Diagnostics.DiagnosticBag;
import Diagnostics.TextSpan;

import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private final String text;
    private int pos = 0;
    private final DiagnosticBag diagnostics = new DiagnosticBag();

    private final Map<String, SKind> keywords = new HashMap<>();

    public Lexer(final String text) {
         this.text = text;

         // Initialize Keywords
         keywords.put("true", SKind.TrueKeyword);
         keywords.put("false", SKind.FalseKeyword);
    }

    public DiagnosticBag getDiagnostics() { return this.diagnostics; }

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

                return new SToken(SKind.Number, sub, new Type(Type.Types.Float, value), start, length);
            } catch (final NumberFormatException nfe) {
                diagnostics.reportInvalidNumber(new TextSpan(start, length), sub, Type.Types.Float);
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
            Type.Types t = Type.Types.Id;

            final SKind sk = keywords.getOrDefault(sub, SKind.Identifier);
            if (sk == SKind.TrueKeyword || sk == SKind.FalseKeyword) {
                value = (sk == SKind.TrueKeyword);
                t = Type.Types.Boolean;
            }

            return new SToken(sk, sub, new Type(t, value), start, length);
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
                if (getAhead() == '=') {
                    this.pos += 2;
                    return new SToken(SKind.LNotEquals, "!=", null, this.pos - 2, this.pos);
                }

                return new SToken(SKind.Bang, "!", null, this.pos++, this.pos);
            }
            case '&' -> {
                if (getAhead() == '&') {
                    this.pos += 2;
                    return new SToken(SKind.LAnd, "&&", null, this.pos - 2, this.pos);
                }
            }
            case '|' -> {
                if (getAhead() == '|') {
                    this.pos += 2;
                    return new SToken(SKind.LOr, "||", null, this.pos - 2, this.pos);
                }
            }
            case '=' -> {
                if (getAhead() == '=') {
                    this.pos += 2;

                    return new SToken(SKind.LEquals, "==", null, this.pos - 2, this.pos);
                }

                return new SToken(SKind.Equals, "=", null, this.pos++, this.pos);
            }
            default -> {
                diagnostics.reportBadChar(this.pos, this.getCur());
            }
        }

        return new SToken(SKind.Bad, this.text.substring(this.pos, this.pos++), null, this.pos - 1, this.pos);
    }
}
