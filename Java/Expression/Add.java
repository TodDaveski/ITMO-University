package expression;

import expression.calculator.Calculator;

public class Add<T extends Number> extends AbstractBinaryOperator<T> {
    public Add(Expression<T> first, Expression<T> second, Calculator<T> calculator) {
        super(first, second, calculator);
    }

    @Override
    protected T getResult(T first, T second) {
        return calculator.add(first, second);
    }

}
