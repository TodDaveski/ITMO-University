package expression.calculator;

import java.math.BigInteger;

public class BigIntegerCalculator implements Calculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger sub(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger mul(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger div(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    @Override
    public BigInteger not(BigInteger x) {
        return mul(x, BigInteger.valueOf(-1));
    }

    @Override
    public BigInteger parse(String string) {
        return new BigInteger(string);
    }

}
