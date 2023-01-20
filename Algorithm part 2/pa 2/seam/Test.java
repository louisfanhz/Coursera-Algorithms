/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Test {
    public static void main(String[] args) {
        Queue<Integer> q = new Queue<Integer>();

        for (int i = 0; i < 10000; i++)
            q.enqueue(i);

        while (!q.isEmpty()) {
            int i = q.dequeue();
            if (i > 9990)
                StdOut.println(i);
        }
    }
}
