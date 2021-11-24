package expression;

import expression.calculator.Calculator;

public class Negate<T extends Number> extends AbstractUnaryOperator<T> {
    public Negate(Expression<T> value, Calculator<T> calculator) {
        super(value, calculator);
    }

    @Override
    protected T getResult(T value) {
        return calculator.not(value);
    }
}
