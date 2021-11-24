package expression;


import expression.calculator.Calculator;

public abstract class AbstractBinaryOperator<T extends Number> implements Expression<T> {
    Expression<T> first, second;
    Calculator<T> calculator;

    public AbstractBinaryOperator(Expression<T> first, Expression<T> second, Calculator<T> calculator) {
        this.first = first;
        this.second = second;
        this.calculator = calculator;
    }

    public T evaluate(T x, T y, T z) {
        return getResult(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    protected abstract T getResult(T first, T second);

}
