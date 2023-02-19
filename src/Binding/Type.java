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

    public static Types literal(final Object lit) {
        if (lit instanceof Integer) {
            return Types.Integer;
        } else if (lit instanceof Float) {
            return Types.Float;
        } else if (lit instanceof Boolean) {
            return Types.Boolean;
        } else if (lit instanceof String) {
            return Types.String;
        }

        return null;
    }
}