package expression.calculator;

public class UncheckedByteCalculator implements Calculator<Byte> {
    @Override
    public Byte add(Byte x, Byte y) {
        return (byte) (x + y);
    }

    @Override
    public Byte sub(Byte x, Byte y) {
        return (byte) (x - y);
    }

    @Override
    public Byte mul(Byte x, Byte y) {
        return (byte) (x * y);
    }

    @Override
    public Byte div(Byte x, Byte y) {
        return (byte) (x / y);
    }

    @Override
    public Byte not(Byte x) {
        return (byte) (-x);
    }

    @Override
    public Byte parse(String string) {
        return (byte) Integer.parseInt(string);
    }
}
