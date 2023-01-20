/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 06 2020
 *  Description: find shortest ancestral path using BFS
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class DoubleBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] markedA, markedB;
    private int[] distToA, distToB;
    private int sca, sap;

    public DoubleBFS(Digraph G, int a, int b) {
        initialize(G);
        validateVertex(a);
        validateVertex(b);
        bfs(G, a, b);
    }

    public DoubleBFS(Digraph G, Iterable<Integer> a, Iterable<Integer> b) {
        initialize(G);
        validateVertices(a);
        validateVertices(b);
        bfs(G, a, b);
    }

    private void initialize(Digraph G) {
        markedA = new boolean[G.V()];
        markedB = new boolean[G.V()];
        distToA = new int[G.V()];
        distToB = new int[G.V()];
        sca = -1;
        sap = INFINITY;

        for (int v = 0; v < G.V(); v++) {
            distToA[v] = INFINITY;
            distToB[v] = INFINITY;
        }
    }

    // bfs from a single source
    private void bfs(Digraph G, int a, int b) {
        Queue<Integer> qa = new Queue<Integer>();
        Queue<Integer> qb = new Queue<Integer>();
        markedA[a] = true;
        markedB[b] = true;
        distToA[a] = 0;
        distToB[b] = 0;
        qa.enqueue(a);
        qb.enqueue(b);

        search(G, qa, qb);
    }

    private void bfs(Digraph G, Iterable<Integer> a, Iterable<Integer> b) {
        Queue<Integer> qa = new Queue<Integer>();
        Queue<Integer> qb = new Queue<Integer>();
        for (int s : a) {
            markedA[s] = true;
            distToA[s] = 0;
            qa.enqueue(s);
        }
        for (int s : b) {
            markedB[s] = true;
            distToB[s] = 0;
            qb.enqueue(s);
        }

        search(G, qa, qb);
    }

    private void search(Digraph G, Queue<Integer> qa, Queue<Integer> qb) {
        if (qa.size() == 1 && qb.size() == 1 && qa.peek().equals(qb.peek())) {
            sap = 0;
            sca = qa.dequeue();
            return;
        }


        while (!(qa.isEmpty() && qb.isEmpty())) {
            if (!qa.isEmpty()) {
                int u = qa.dequeue();
                for (int w : G.adj(u)) {
                    if (markedB[w]) {
                        int distTotal = distToB[w] + distToA[u];
                        if (sap > distTotal) {
                            sap = distTotal + 1;
                            sca = w;
                        }
                        else return;
                    }
                    else if (!markedA[w]) {
                        qa.enqueue(w);
                        markedA[w] = true;
                        distToA[w] = distToA[u] + 1;
                    }
                }
            }

            if (!qb.isEmpty()) {
                int u = qb.dequeue();
                for (int w : G.adj(u)) {
                    if (markedA[w]) {
                        int distTotal = distToA[w] + distToB[u];
                        if (sap > distTotal) {
                            sap = distTotal + 1;
                            sca = w;
                        }
                        else return;
                    }
                    else if (!markedB[w]) {
                        qb.enqueue(w);
                        markedB[w] = true;
                        distToB[w] = distToB[u] + 1;
                    }
                }
            }
        }
    }

    public int sap() {
        if (sca == -1) return -1;
        return sap;
    }

    public int sca() {
        return sca;
    }

    /*
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        validateVertex(v);

        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }
     */

    private void validateVertex(int v) {
        if (v < 0 || v >= markedA.length)
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (markedA.length - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        for (Integer v : vertices) {
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(v);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println(G);
        /*
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            DoubleBFS sap = new DoubleBFS(G, v, w);
            // int length = sap.length(v, w);
            int ancestor = sap.sca();
            int length = sap.sap();
            // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.printf("associated length = %d\nancestor = %d\n", length, ancestor);
        }

         */

        int[] a = { 3, 3 };
        int[] b = { 10, 9 };
        ArrayList<Integer> v = new ArrayList<Integer>();
        ArrayList<Integer> w = new ArrayList<Integer>();

        for (int i : a) v.add(i);
        for (int i : b) w.add(i);

        DoubleBFS sap = new DoubleBFS(G, v, w);
        int ancestor = sap.sca();
        int length = sap.sap();
        StdOut.printf("associated length = %d\nancestor = %d\n", length, ancestor);
    }
}
