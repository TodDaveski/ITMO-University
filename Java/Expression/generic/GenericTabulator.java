package expression.generic;

import expression.calculator.*;
import expression.exceptions.EvaluatingException;
import expression.Expression;
import expression.parser.ExpressionParser;

public class GenericTabulator implements Tabulator {
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        switch (mode) {
            case "i":
                return calculateTable(new ExpressionParser<>(new IntCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new IntCalculator());
            case "d":
                return calculateTable(new ExpressionParser<>(new DoubleCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new DoubleCalculator());
            case "bi":
                return calculateTable(new ExpressionParser<>(new BigIntegerCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new BigIntegerCalculator());
            case "u":
                return calculateTable(new ExpressionParser<>(new UncheckedIntCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new UncheckedIntCalculator());
            case "f":
                return calculateTable(new ExpressionParser<>(new UncheckedFloatCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new UncheckedFloatCalculator());
            case "b":
                return calculateTable(new ExpressionParser<>(new UncheckedByteCalculator()).parse(expression), x1, x2, y1, y2, z1, z2, new UncheckedByteCalculator());
            default:
                return null;
        }
    }

    private <T extends Number> Object[][][] calculateTable(Expression<T> expression, int x1, int x2, int y1, int y2, int z1, int z2, Calculator<T> calculator) {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = expression.evaluate(calculator.parse(Long.toString(i)), calculator.parse(Long.toString(j)), calculator.parse(Long.toString(k)));
                    } catch (EvaluatingException | ArithmeticException ex) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
        return result;
    }


}
