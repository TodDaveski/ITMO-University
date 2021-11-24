package expression;

public class Variable<T extends Number> implements Expression<T> {
    String name;

    public Variable(String name) {
        this.name = name;
    }


    @Override
    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return null;
        }
    }
}
