package expression.parser;

import expression.calculator.Calculator;
import expression.exceptions.*;
import expression.*;

import java.util.Map;

public class ExpressionParser<T extends Number> extends BaseParser implements Parser<T> {

    private final Calculator<T> calculator;

    private static final int MAX_PRIORITY = 3;

    private int bracketLevel = 0;

    private final Map<Character, Integer> operationPriority = Map.of(
            ')', -1,
            '+', 1,
            '-', 1,
            '*', 2,
            '/', 2
    );

    public ExpressionParser(Source source, Calculator<T> calculator) {
        super(source);
        this.calculator = calculator;
    }

    public ExpressionParser(Calculator<T> calculator) {
        this(new StringSource(" "), calculator);
        bracketLevel = 0;
    }

    public Expression<T> parse(String stringSource) throws ParsingException {
        newSource(new StringSource(stringSource));
        nextChar();
        bracketLevel = 0;
        skipWhitespace();
        return parsePriority(0);
    }

    private Expression<T> parsePriority(int priority) throws ParsingException {
        skipWhitespace();

        if (priority == MAX_PRIORITY) {
            return parseValue();
        }

        Expression<T> first = parsePriority(priority + 1);

        while (true) {
            if (ch == ')' && bracketLevel == 0)
                throw new InvalidBracketExpressionException("closing without opening", getPos());

            skipWhitespace();
            if (ch == '\0')
                return first;
            if (!operationPriority.containsKey(ch))
                throw new UnknownSymbolException(Character.toString(ch), getPos());
            if (operationPriority.get(ch) != priority) {
                return first;
            }
            if (test('+')) {
                first = new Add(first, parsePriority(priority + 1), calculator);
            } else if (test('-')) {
                first = new Sub(first, parsePriority(priority + 1), calculator);
            } else if (test('*')) {
                first = new Multiply(first, parsePriority(priority + 1), calculator);
            } else if (test('/')) {
                first = new Divide(first, parsePriority(priority + 1), calculator);
            } else {
                throw new IllegalOperatorException(Character.toString(ch), getPos());
            }
        }
    }

    private Expression<T> parseValue() throws ParsingException {
        if (test('(')) {
            skipWhitespace();
            bracketLevel++;
            Expression<T> parsed = parsePriority(0);
            skipWhitespace();
            expect(')');
            bracketLevel--;
            return parsed;
        } else if (test('-')) {
            skipWhitespace();
            if (between('0', '9')) {
                return parseNumber(true);
            }
            return new Negate(parseValue(), calculator);
        } else if (between('0', '9')) {
            return parseNumber(false);
        } else {
            if (test(')') && bracketLevel == 0) {
                throw new InvalidBracketExpressionException("closing without opening", getPos());
            } else return parseVariable();
        }
    }

    private Expression<T> parseVariable() throws IllegalVariableException {
        skipWhitespace();
        final String variable = Character.toString(ch);
        nextChar();
        if (variable.equals("x") || variable.equals("y") || variable.equals("z")) {
            return new Variable<T>(variable);
        }
        throw new IllegalVariableException(variable, getPos());
    }

    private Expression<T> parseNumber(boolean isNegative) throws IllegalConstException {
        final StringBuilder sb = new StringBuilder();
        if (isNegative) {
            sb.append('-');
        }
        while (between('0', '9')) {
            sb.append(ch);
            nextChar();
        }
        try {
            return new Const<>(calculator.parse(sb.toString()));
        } catch (NumberFormatException ex) {
            throw new IllegalConstException(ex.getMessage(), getPos());
        }
    }

    protected void skipWhitespace() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {

        }
    }
}
