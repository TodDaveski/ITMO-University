package expression.calculator;

public class DoubleCalculator implements Calculator<Double> {
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double sub(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double mul(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double div(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double not(Double x) {
        return mul(x, -1.0);
    }

    @Override
    public Double parse(String string) {
        return  Double.parseDouble(string);
    }
}
