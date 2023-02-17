package Binding;

public class Type {
    public static enum Types {
        Integer,
        Float,
        Boolean,
        Op,
        Id, String
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