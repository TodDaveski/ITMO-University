package expression.generic;

// :NOTE: wrong project structure. This class should be in `expression.generic` package. Test failed
interface Tabulator {
    Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception;
}