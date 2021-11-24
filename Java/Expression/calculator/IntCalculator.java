package expression.calculator;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntCalculator implements Calculator<Integer> {
    @Override
    public Integer add(Integer left, Integer right) {
        if ((left > 0 && right > 0 && Integer.MAX_VALUE - left < right)
                || (left < 0 && right < 0 && Integer.MIN_VALUE - left > right))
            throw new OverflowException();
        return (left + right);
    }

    @Override
    public Integer sub(Integer left, Integer right) {
        if (left == 0 && right == Integer.MIN_VALUE)
            throw new OverflowException();
        if ((left >= 0 && right >= 0) || (left <= 0 && right <= 0)) {
            return left - right;
        }
        if (left > 0) {
            if (left > Integer.MAX_VALUE + right)
                throw new OverflowException();
        } else {
            if (left < Integer.MIN_VALUE + right)
                throw new OverflowException();
        }
        return (left - right);
    }

    @Override
    public Integer mul(Integer left, Integer right) {
        if (left < right) {
            return mul(right, left);
        }
        if (right > 0) {
            if (right > Integer.MAX_VALUE / left)
                throw new OverflowException();
        } else if (left > 0) {
            if (right < Integer.MIN_VALUE / left)
                throw new OverflowException();
        } else if (left < 0){
            if (left < Integer.MAX_VALUE / right)
                throw new OverflowException();
        }
        return (right * left);
    }

    @Override
    public Integer div(Integer left, Integer right) {
        if (right == 0)
            throw new DivisionByZeroException();
        if (left == Integer.MIN_VALUE && right == -1)
            throw new OverflowException();
        return left/right;
    }

    @Override
    public Integer not(Integer x) {
        return mul(x, -1);
    }


    @Override
    public Integer parse(String string) {
        return Integer.parseInt(string);
    }
}
