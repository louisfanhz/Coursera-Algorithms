/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    public List<Integer> luckyNumbers(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[] mi = new int[m], mx = new int[n];
        Arrays.fill(mi, Integer.MAX_VALUE);
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                mi[i] = Math.min(matrix[i][j], mi[i]);
                mx[j] = Math.max(matrix[i][j], mx[j]);
            }
        }

        for (int i : mi) System.out.print(i + " ");
        System.out.println(" ");
        for (int i : mx) System.out.print(i + " ");

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j)
                if (mi[i] == mx[j])
                    res.add(mi[i]);
        return res;
    }

    public int isGreater(int a, int b) {
        if (a >= b) System.out.println("a > b");
        else return 1;
        return 0;
    }

    public static void main(String[] args) {
        //RectHV t = new RectHV(0.5, 0.5, 1, 1);
        Point2D p = new Point2D(0.7, 0.4);
        Point2D q = new Point2D(0.9, 0.6);
        Point2D r = new Point2D(0.86, 0.490);
        System.out.println(p.distanceSquaredTo(r));
        System.out.println(q.distanceSquaredTo(r));

        //while (!ans.isEmpty()) {
        //    System.out.println(ans.remove(0));
        //  }
    }
}
