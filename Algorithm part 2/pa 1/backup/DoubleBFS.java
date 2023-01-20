/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 06 2020
 *  Description: find shortest ancestral path using BFS
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class DoubleBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] markedA, markedB;
    private int[] edgeToA, edgeToB;
    private int[] distToA, distToB;
    private int sca;

    public DoubleBFS(Digraph G, int a, int b) {
        markedA = new boolean[G.V()];
        markedB = new boolean[G.V()];
        distToA = new int[G.V()];
        distToB = new int[G.V()];
        edgeToA = new int[G.V()];
        edgeToB = new int[G.V()];

        for (int v = 0; v < G.V(); v++) {
            distToA[v] = INFINITY;
            distToB[v] = INFINITY;
        }
        validateVertex(a);
        validateVertex(b);
        sca = bfs(G, a, b);
    }

    // bfs from a single source
    private int bfs(Digraph G, int a, int b) {
        Queue<Integer> qa = new Queue<Integer>();
        Queue<Integer> qb = new Queue<Integer>();
        qa.enqueue(a);
        qb.enqueue(b);
        markedA[a] = true;
        markedB[b] = true;
        distToA[a] = 0;
        distToB[b] = 0;

        while (!(qa.isEmpty() && qb.isEmpty())) {
            if (!qa.isEmpty()) {
                int u = qa.dequeue();
                for (int w : G.adj(u)) {
                    if (markedB[w]) return w;
                    else if (!markedA[w]) {
                        qa.enqueue(w);
                        markedA[w] = true;
                        edgeToA[w] = u;
                        distToA[w] = distToA[u] + 1;
                    }
                }
            }
        }
        return -1;
    }

    private int move(Digraph G, Queue<Integer> q, int[] marked, int[] edgeTo, int[] distTo) {
        if (!q.isEmpty()) {
            int u = q.dequeue();
            for (int w : G.adj(u)) {
                if (markedA[w]) return w;
                else if (!markedB[w]) {
                    q.enqueue(w);
                    markedB[w] = true;
                    edgeToB[w] = u;
                    distToB[w] = distToB[u] + 1;
                }
            }
        }
    }

    /*
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
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

    public int sca() {
        return sca;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            DoubleBFS sap = new DoubleBFS(G, v, w);
            // int length = sap.length(v, w);
            int ancestor = sap.sca();
            // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.printf("ancestor = %d\n", ancestor);
        }
    }
}
