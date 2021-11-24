package expression.generic;

public class Main {
    public static void main(String[] args) {
        GenericTabulator tab = new GenericTabulator();
        String expression = args[1];
        try {
            switch (args[0]) {
                case "-i":
                    tab.tabulate("i", expression, -2, 2, -2, 2, -2, 2);
                case "-d":
                    tab.tabulate("d", expression, -2, 2, -2, 2, -2, 2 );
                case "-bi":
                    tab.tabulate("bi", expression, -2, 2, -2, 2, -2, 2 );
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
