package expression;

import expression.calculator.Calculator;

public class Divide<T extends Number> extends AbstractBinaryOperator<T> {
    public Divide(Expression<T> first, Expression<T> second, Calculator<T> calculator) {
        super(first, second, calculator);
    }

    @Override
    protected T getResult(T first, T second) {
        return calculator.div(first, second);
    }
}
