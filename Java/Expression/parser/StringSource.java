package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class StringSource implements Source {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    public boolean hasNext() {
        return pos < data.length();
    }

    public char next() {
        return data.charAt(pos++);
    }

    public Exception error(final String message) {
        return new Exception(pos - 1 + ": " + message);
    }

    public int getPos() {
        return pos;
    }

    public void prevChar() {
        pos--;
    }
}
