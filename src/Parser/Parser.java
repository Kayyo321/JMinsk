package Parser;

import Diagnostics.DiagnosticBag;
import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<SToken> tokens = new ArrayList<>();
    private int pos;
    private final DiagnosticBag diagnostics = new DiagnosticBag();

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

    public DiagnosticBag getDiagnostics() { return this.diagnostics; }

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

        diagnostics.reportUnexpectedToken(current().getSpan(), current().getKind(), kind);

        return new SToken(kind, null, null, current().getStartI(), current().getEndI(), -1);
    }

    public STree parse() {
        final Expression expr = parseExpr();
        final SToken eofToken = match(SKind.Eof);

        return new STree(this.diagnostics, expr, eofToken);
    }

    private Expression parseExpr() {
        return parseAssignmentExpr();
    }

    private Expression parseAssignmentExpr() {
        if (peek(0).getKind() == SKind.Identifier &&
            peek(1).getKind() == SKind.Equals) {
            final SToken idToken = nextToken();
            final SToken opToken = nextToken();
            final Expression right = parseAssignmentExpr();
            return new AssignmentExpr(idToken, opToken, right);
        }

        return parseBinaryExpr(0);
    }

    private Expression parseBinaryExpr(final int parent) {
        Expression left;
        final int unaryPrec = getUnaryOperatorPrec(current().getKind());
        if (unaryPrec != 0 && unaryPrec > parent) {
            final SToken op = nextToken();
            final Expression oper = parseBinaryExpr(0);
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
            final Expression right = parseBinaryExpr(prec);
            left = new BinaryExpr(left, op, right);
        }

        return left;
    }

    private static int getBinaryOperatorPrec(final SKind kind) {
        return switch (kind) {
            case Mod, Carrot -> 6;
            case Star, Div -> 5;
            case Plus, Minus -> 4;
            case LEquals, LNotEquals -> 3;
            case LAnd -> 2;
            case LOr -> 1;
            default -> 0;
        };
    }

    private static int getUnaryOperatorPrec(final SKind kind) {
        return switch (kind) {
            case Plus, Minus, Bang -> 7;
            default -> 0;
        };
    }

    private Expression parsePrimary() {
        switch (current().getKind()) {
            case LParen -> {
                final SToken left = nextToken();
                final Expression expr = parseBinaryExpr(0);
                final SToken right = match(SKind.RParen);
                return new ParenExpr(left, expr, right);
            }
            case FalseKeyword, TrueKeyword -> {
                final SToken tok = nextToken();
                final boolean value = tok.getKind() == SKind.TrueKeyword;
                return new LiteralExpr(tok, value);
            }
            case NilKeyword -> {
                final SToken tok = nextToken();
                return new LiteralExpr(tok, null);
            }
            case Identifier -> {
                final SToken idToken = nextToken();
                return new NameExpr(idToken);
            }
            default -> {
                final SToken numberTok = match(SKind.Number);
                return new LiteralExpr(numberTok);
            }
        }
    }
}
