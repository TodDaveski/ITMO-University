package expression;

import expression.calculator.Calculator;

public class Sub extends AbstractBinaryOperator {
    public Sub(Expression first, Expression second, Calculator calculator) {
        super(first, second, calculator);
    }

    @Override
    protected Number getResult(Number first, Number second) {
        return calculator.sub(first, second);
    }
}
