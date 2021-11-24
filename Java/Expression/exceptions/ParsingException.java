package expression.exceptions;
public class ParsingException extends Exception {
    public ParsingException(String message, int pos) {
        super(message + " at position " + pos);
    }
}
