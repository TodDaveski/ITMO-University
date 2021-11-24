package expression;

import expression.calculator.Calculator;

public abstract class AbstractUnaryOperator<T extends Number> implements Expression<T> {

    Expression<T> value;
    Calculator<T> calculator;

    public AbstractUnaryOperator(Expression<T> value, Calculator<T> calculator) {
        this.value = value;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return getResult(value.evaluate(x, y, z));
    }

    protected abstract T getResult(T value);
}
