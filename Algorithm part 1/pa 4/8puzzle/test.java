/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class test {
    public void getC() {
        System.out.println(this.getClass());
    }

    public static void main(String[] args) {
        String str = "test if this \n is gonna change line";
        int num = 1;
        int divideBy = 3;
        test tt = new test();
        test kk = new test();
        System.out.println(tt.getC() == kk.getC());
    }
}
