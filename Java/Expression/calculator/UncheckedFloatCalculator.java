package expression.calculator;

public class UncheckedFloatCalculator implements Calculator<Float> {
    @Override
    public Float add(Float x, Float y) {
        return x + y;
    }

    @Override
    public Float sub(Float x, Float y) {
        return x - y;
    }

    @Override
    public Float mul(Float x, Float y) {
        return x * y;
    }

    @Override
    public Float div(Float x, Float y) {
        return x / y;
    }

    @Override
    public Float not(Float x) {
        return -x;
    }

    @Override
    public Float parse(String string) {
        return Float.parseFloat(string);
    }
}
