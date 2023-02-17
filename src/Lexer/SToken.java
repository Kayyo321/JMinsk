package Lexer;

import Binding.Type;
import Diagnostics.TextSpan;
import Parser.SNode;

import java.util.List;

public class SToken extends SNode {
    private final SKind kind;
    private final String text;
    private final Type value;

    public final int startI, endI;

    public SToken(SKind kind, final String text, final Type value, final int startI, final int endI) {
        this.kind = kind;
        this.text = text;
        this.value = value;
        this.startI = startI;
        this.endI = endI;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.kind +
                ": '" +
                this.text +
                "'");

        if (this.value != null) {
            sb.append(" ");
            sb.append(this.value.value);
        }

        return sb.toString();
    }

    public SKind getKind()  { return this.kind; }

    @Override
    public List<SNode> getChildren() {
        return null;
    }

    public int    getStartI() { return this.startI; }
    public int    getEndI()   { return this.startI; }
    public String getText()   { return text;        }
    public Type getValue()    { return value;       }
    public TextSpan getSpan() { return new TextSpan(this.startI, this.endI); }
}
