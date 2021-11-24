package expression.parser;

import expression.exceptions.InvalidBracketExpressionException;
import expression.exceptions.ParsingException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private  Source source;
    protected char ch;

    protected BaseParser(final Source source) {
        this.source = source;
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String expected) {
        int i = 0;
        for(char c : expected.toCharArray()) {
            if (!test(c)) {
                while (i-- > 0) {
                    source.prevChar();
                }
                return false;
            }
            i++;
        }
        return true;
    }

    protected void expect(final char c) throws ParsingException {
        if (c == ')' && ch != c) {
            throw new InvalidBracketExpressionException("opening without closing ", getPos());
        }
        if (ch != c) {
            throw new ParsingException("Expected '" + c + "', found '" + ch + "'" , getPos());
        }
        nextChar();
    }

    protected void expect(final String value) throws ParsingException {
        StringBuilder found = new StringBuilder();
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    public void newSource(Source source) {
        this.source = source;
    }


    protected Exception error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected int getPos() {
        return source.getPos();
    }
}
