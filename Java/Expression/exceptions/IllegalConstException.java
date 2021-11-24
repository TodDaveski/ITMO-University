package expression.exceptions;

public class IllegalConstException extends ParsingException {
    public IllegalConstException(String message, int pos) {
        super("Illegal Constant " + message, pos);
    }
}
