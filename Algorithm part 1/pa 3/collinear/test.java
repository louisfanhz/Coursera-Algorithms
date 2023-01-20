/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class test {
    private static final String str = "testing testing";

    public void outerMethod() {
        System.out.println(str);
    }

    private class inner {
        public void print() {
        }
    }

    public static void main(String[] args) {
        double a = Double.POSITIVE_INFINITY;
        double b = Double.POSITIVE_INFINITY;
        System.out.println(Double.compare(a, b));
    }
}
