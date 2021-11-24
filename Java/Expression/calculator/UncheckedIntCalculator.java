package expression.calculator;

public class UncheckedIntCalculator implements Calculator<Integer> {
    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer sub(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer mul(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer div(Integer x, Integer y) {
        return x / y;
    }

    @Override
    public Integer not(Integer x) {
        return -x;
    }

    @Override
    public Integer parse(String string) {
        return Integer.parseInt(string);
    }
}
