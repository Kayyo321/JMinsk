package Diagnostics;

public record TextSpan(int start, int length) {
    public int getEnd() {
        return this.start + this.length;
    }

    @Override
    public String toString() {
        final int len = this.start + this.length;
        return "" + this.start + ", " + len + " (" + this.length + ")";
    }
}
