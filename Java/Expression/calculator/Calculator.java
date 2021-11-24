package expression.calculator;

public interface Calculator<T extends Number> {
    T add(T x, T y);

    T sub(T x, T y);

    T mul(T x, T y);

    T div(T x, T y);

    T not(T x);

    T parse(String string);
}
