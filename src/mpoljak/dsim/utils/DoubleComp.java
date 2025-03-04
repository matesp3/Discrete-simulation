package mpoljak.dsim.utils;

public class DoubleComp {
    private static final double EPSILON = 0.0001;

    /**
     * Compares value 'a' to value 'b'
     * @param a
     * @param b
     * @return case a < b then -1; case a == b(regarding set epsilon) then 0; case a > b then 1
     */
    public static int compare(double a, double b) {
        return compare(a, b, EPSILON);
    }

    public static int compare(double a, double b, double epsilon) {
        double diff = a - b;
        return (Math.abs(diff) < epsilon) ? 0 : (int) Math.signum(diff);
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

    public static void showExamplesOfComparing() {
        System.out.println("\n -- COMPARING 2 double values: a, b --\n POSSIBLE RESULTS of compare(a,b):");
        System.out.println("    * compare(a,b) = -1 ['a' is less than 'b']");
        System.out.println("    * compare(a,b) =  0 ['a' is is equal to 'b' for given epsilon]");
        System.out.println("    * compare(a,b) =  1 ['a' is greater than 'b']\n");

        printRes(1.543,1.5432, EPSILON);
        printRes(1.5435,1.5432, EPSILON);
        printRes(1.5429,1.5432, EPSILON);
        printRes(1.542,1.5432, EPSILON);
        printRes(1.5431,1.5432, EPSILON);
        printRes(1.54311,1.5432, EPSILON);
        printRes(1.5431,1.54321, EPSILON);
    }

    private static void printRes(double a, double b, double eps) {
        System.out.println(String.format("compare(a=%.5f; b=%.5f) => %d [epsilon=%f]", a, b, compare(a, b), eps));
    }

    public static void main(String[] args) {
        showExamplesOfComparing();
    }
}
