package expression.exceptions;

public class IllegalVariableException extends ParsingException {
    public IllegalVariableException(String message, int pos) {
        super("Expected variable, found '" + message + "'", pos);
    }
}
