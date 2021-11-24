package expression.exceptions;

public class NonIntegerNumberException extends EvaluatingException {
    public NonIntegerNumberException(String operation) {
        super("The result of operation " + operation + " is not an integer number");
    }
}
