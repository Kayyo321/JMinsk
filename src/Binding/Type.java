package Binding;

public class Type {
    public enum Types {
        Digit,
        Boolean,
        Id, Nil, String
    }

    public Types type;

    public Object value;

    public Type(final Types type) {
        this.type = type;
    }
    public Type(final Types type, final Object value) {
        this.type = type;
        this.value = value;
    }
}