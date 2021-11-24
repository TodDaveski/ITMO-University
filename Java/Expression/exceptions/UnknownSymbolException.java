package expression.exceptions;

public class UnknownSymbolException extends ParsingException {
    public UnknownSymbolException(String message, int pos) {
        super("Unknown word '" + message + "' " , pos);
    }
}
