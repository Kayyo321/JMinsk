package Lexer;

import Binding.Type;
import Diagnostics.DiagnosticBag;
import Diagnostics.TextSpan;

import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private final String text;
    private int pos = 0;
    private long line = 1;
    private final DiagnosticBag diagnostics = new DiagnosticBag();

    private final Map<String, SKind> keywords = new HashMap<>();

    public Lexer(final String text) {
         this.text = text;

         // Initialize Keywords
         keywords.put("true", SKind.TrueKeyword);
         keywords.put("false", SKind.FalseKeyword);
         keywords.put("nil", SKind.NilKeyword);
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
            return new SToken(SKind.Eof,"\0", null, this.pos-1, this.pos, line);
        }

        if (Character.isDigit(getCur())) {
            return readNumberTok();
        } else if (Character.isWhitespace(getCur())) {
            return readWhiteSpaceTok();
        } else if (Character.isLetter(getCur())) {
            return readIdOrKeywordTok();
        }

        switch (getCur()) {
            case '+' -> {
                return new SToken(SKind.Plus, "+", null, this.pos++, this.pos, line);
            }
            case '-' -> {
                return new SToken(SKind.Minus, "-", null, this.pos++, this.pos, line);
            }
            case '*' -> {
                return new SToken(SKind.Star, "*", null, this.pos++, this.pos, line);
            }
            case '/' -> {
                return new SToken(SKind.Div, "/", null, this.pos++, this.pos, line);
            }
            case '%' -> {
                return new SToken(SKind.Mod, "%", null, this.pos++, this.pos, line);
            }
            case '^' -> {
                return new SToken(SKind.Carrot, "^", null, this.pos++, this.pos, line);
            }
            case '(' -> {
                return new SToken(SKind.LParen, "(", null, this.pos++, this.pos, line);
            }
            case ')' -> {
                return new SToken(SKind.RParen, ")", null, this.pos++, this.pos, line);
            }
            case '!' -> {
                if (getAhead() == '=') {
                    this.pos += 2;
                    return new SToken(SKind.LNotEquals, "!=", null, this.pos - 2, this.pos, line);
                }

                return new SToken(SKind.Bang, "!", null, this.pos++, this.pos, line);
            }
            case '&' -> {
                if (getAhead() == '&') {
                    this.pos += 2;
                    return new SToken(SKind.LAnd, "&&", null, this.pos - 2, this.pos, line);
                }
            }
            case '|' -> {
                if (getAhead() == '|') {
                    this.pos += 2;
                    return new SToken(SKind.LOr, "||", null, this.pos - 2, this.pos, line);
                }
            }
            case '=' -> {
                if (getAhead() == '=') {
                    this.pos += 2;

                    return new SToken(SKind.LEquals, "==", null, this.pos - 2, this.pos, line);
                }

                return new SToken(SKind.Equals, "=", null, this.pos++, this.pos, line);
            }
            default -> {
                diagnostics.reportBadChar(this.pos, this.getCur());
                pos++;
            }
        }

        return new SToken(SKind.Bad, this.text.substring(this.pos, this.pos++), null, this.pos - 1, this.pos, line);
    }

    private SToken readIdOrKeywordTok() {
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
        } else if (sk == SKind.NilKeyword) {
            value = null;
            t = Type.Types.Nil;
        }

        return new SToken(sk, sub, new Type(t, value), start, length, line);
    }

    private SToken readWhiteSpaceTok() {
        final int start = this.pos;

        while (Character.isWhitespace(getCur())) {
            next();
        }

        final int length = this.pos;
        final String sub = text.substring(start, length);

        final String findStr = "\n";
        int lastIndex = 0;
        long count = 0;

        while (lastIndex != -1) {
            lastIndex = sub.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }

        this.line += count;

        return new SToken(SKind.WhiteSpace, sub, null, start, length, line);
    }

    private SToken readNumberTok() {
        final int start = this.pos;

        while (Character.isDigit(getCur()) || getCur() == '.') {
            next();
        }

        final int length = this.pos;
        final String sub = text.substring(start, length);

        try {
            final float value = Float.parseFloat(sub);

            return new SToken(SKind.Number, sub, new Type(Type.Types.Digit, value), start, length, line);
        } catch (final NumberFormatException nfe) {
            diagnostics.reportInvalidNumber(new TextSpan(start, length), sub, Type.Types.Digit);
            return new SToken(SKind.Bad, sub, null, start, length, line);
        }
    }
}
