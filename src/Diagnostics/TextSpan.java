package Diagnostics;

public class TextSpan {
    private final int start;
    private final int length;

    public TextSpan(final int start, final int length) {
        this.start = start;
        this.length = length;
    }


    public int getLength() {
        return this.length;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.start + this.length;
    }

    @Override
    public String toString() {
        final int len = this.start + this.length;
        return "" + this.start + ", " + len + " (" + this.length + ")";
    }
}
