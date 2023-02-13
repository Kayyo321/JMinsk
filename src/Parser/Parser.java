package Parser;

import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<SToken> tokens = new ArrayList<>();
    private int pos;
    private final List<String> diagnostics = new ArrayList<>();

    public Parser(final String text) {
        final Lexer lexer = new Lexer(text);

        SToken token;

        do {
            token = lexer.nextToken();

            if (token.getKind() != SKind.WhiteSpace && token.getKind() != SKind.Bad) {
                tokens.add(token);
            }
        } while (token.getKind() != SKind.Eof);

        diagnostics.addAll(lexer.getDiagnostics());
    }

    public List<String> getDiagnostics() { return this.diagnostics; }

    private SToken peek(final int offset) {
        final int index = this.pos + offset;

        if (index >= this.tokens.size()) {
            return this.tokens.get(this.tokens.size()-1);
        }

        return this.tokens.get(index);
    }

    private SToken current() { return peek(0); }

    private SToken nextToken() {
        final SToken c = current();
        this.pos++;
        return c;
    }

    private SToken match(final SKind kind) {
        if (current().getKind() == kind) {
            return nextToken();
        }

        diagnostics.add("ERROR: unexpected token <" + current().getKind() + ">, expected <" + kind + ">...");

        return new SToken(kind, current().getPos(), null, null);
    }

    public STree parse() {
        final Expression expr = parseTerm();
        final SToken eofToken = match(SKind.Eof);

        return new STree(this.diagnostics, expr, eofToken);
    }

    /** Helper! */
    private Expression parseExpr() {
        return parseTerm();
    }

    private Expression parseFactor() {
        Expression left = parsePrimary();

        while (current().getKind() == SKind.Star || current().getKind() == SKind.Div   ||
               current().getKind() == SKind.Mod  || current().getKind() == SKind.Carrot) {
            final SToken op = nextToken();
            final Expression right = parsePrimary();

            left = new BinaryExpr(left, op, right);
        }

        return left;
    }

    private Expression parseTerm() {
        Expression left = parseFactor();

        while (current().getKind() == SKind.Plus || current().getKind() == SKind.Minus) {
            final SToken op = nextToken();
            final Expression right = parseFactor();

            left = new BinaryExpr(left, op, right);
        }

        return left;
    }

    private Expression parsePrimary() {
        if (current().getKind() == SKind.LParen) {
            final SToken left = nextToken();
            final Expression expr = parseExpr();
            final SToken right = match(SKind.RParen);
            return new ParenExpr(left, expr, right);
        }

        final SToken numberTok = match(SKind.Number);
        return new NumberExpr(numberTok);
    }
}
