package expression;

import expression.calculator.Calculator;

public class Multiply<T extends Number> extends AbstractBinaryOperator<T> {
    public Multiply(Expression<T> first, Expression<T> second, Calculator<T> calculator) {
        super(first, second, calculator);
    }

    @Override
    protected T getResult(T first, T second) {
        return calculator.mul(first, second);
    }
}
