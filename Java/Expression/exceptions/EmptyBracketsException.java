package expression.exceptions;

public class EmptyBracketsException extends ParsingException {
    public EmptyBracketsException(int pos) {
        super("Empty brackets ", pos);
    }
}
