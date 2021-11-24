package expression.parser;

import expression.Expression;
import expression.exceptions.ParsingException;

public interface Parser<T extends Number> {
    Expression<T> parse(String expression) throws ParsingException;
}