package expression.exceptions;

public class InvalidBracketExpressionException extends ParsingException {
    public InvalidBracketExpressionException(String message, int pos) {
            super( "Wrong Bracket Expression, " + message, pos);
    }
}
