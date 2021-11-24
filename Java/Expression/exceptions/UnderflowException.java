package expression.exceptions;

public class UnderflowException extends EvaluatingException {
    public UnderflowException() {
        super("underflow");
    }
}
