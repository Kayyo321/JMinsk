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
            token = lexer.lex();

            if (token.getKind() != SKind.WhiteSpace && token.getKind().valid()) {
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

        return new SToken(kind, null, null, current().getStartI(), current().getEndI());
    }

    public STree parse() {
        final Expression expr = parseExpr(0);
        final SToken eofToken = match(SKind.Eof);

        return new STree(this.diagnostics, expr, eofToken);
    }

    private Expression parseExpr(final int parent) {
        Expression left;
        final int unaryPrec = getUnaryOperatorPrec(current().getKind());
        if (unaryPrec != 0 && unaryPrec > parent) {
            final SToken op = nextToken();
            final Expression oper = parseExpr(0);
            left = new UnaryExpr(op, oper);
        } else {
            left = parsePrimary();
        }

        while (true) {
            final int prec = getBinaryOperatorPrec(current().getKind());
            if (prec == 0 || prec <= parent) {
                break;
            }

            final SToken op = nextToken();
            final Expression right = parseExpr(prec);
            left = new BinaryExpr(left, op, right);
        }

        return left;
    }

    private static int getBinaryOperatorPrec(final SKind kind) {
        return switch (kind) {
            case Mod, Carrot -> 4;
            case Star, Div -> 3;
            case Plus, Minus -> 2;
            case LAnd, LOr -> 1;
            default -> 0;
        };
    }

    private static int getUnaryOperatorPrec(final SKind kind) {
        return switch (kind) {
            case Plus, Minus, Bang -> 5;
            default -> 0;
        };
    }

    private Expression parsePrimary() {
        switch (current().getKind()) {
            case LParen -> {
                final SToken left = nextToken();
                final Expression expr = parseExpr(0);
                final SToken right = match(SKind.RParen);
                return new ParenExpr(left, expr, right);
            }
            case FalseKeyword, TrueKeyword -> {
                final SToken tok = nextToken();
                final boolean value = tok.getKind() == SKind.TrueKeyword;
                return new LiteralExpr(tok, value);
            }

            default -> {
                final SToken numberTok = match(SKind.Number);
                return new LiteralExpr(numberTok);
            }
        }
    }
}
