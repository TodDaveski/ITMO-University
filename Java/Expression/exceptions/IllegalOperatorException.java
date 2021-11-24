package expression.exceptions;

public class IllegalOperatorException extends ParsingException {
    public IllegalOperatorException(String operator, int pos) {
        super("Expected operator, found '" + operator + "'", pos);
    }
}
