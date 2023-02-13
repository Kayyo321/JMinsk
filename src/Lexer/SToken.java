package Lexer;

import Parser.SNode;

import java.util.Iterator;
import java.util.List;

public class SToken extends SNode {
    private final SKind kind;
    private final int pos;
    private final String text;
    private final Object value;

    public SToken(SKind kind, final int pos, final String text, Object value) {
        this.kind = kind;
        this.pos = pos;
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.kind +
                ": '" +
                this.text +
                "'");

        if (this.value != null) {
            sb.append(" ");
            sb.append(this.value);
        }

        return sb.toString();
    }

    public SKind  getKind()  { return this.kind; }

    @Override
    public List<SNode> getChildren() {
        return null;
    }

    public int    getPos()   { return this.pos;  }
    public String getText()  { return text;      }
    public Object getValue() { return value;     }
}
