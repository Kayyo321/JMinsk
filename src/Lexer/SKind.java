package Lexer;

public enum SKind {
    WhiteSpace, Plus, Minus, Star, Div, Mod, Carrot, LParen, RParen,
    Bad {
        @Override
        public boolean valid() {
            return false;
        }
    },
    Eof, NumberExpr, BinaryExpr, ParenExpr, Number, UnaryExpr, TrueKeyword, FalseKeyword, Identifier, Bang, LAnd, LOr;

    public boolean valid() { return true; }
}
