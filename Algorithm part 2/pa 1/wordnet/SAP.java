/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 6 2020
 *  Description: shortest ancestral path
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Digraph is null");
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        DoubleBFS findSAP = new DoubleBFS(G, v, w);
        return findSAP.sap();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        DoubleBFS findSCA = new DoubleBFS(G, v, w);
        return findSCA.sca();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        DoubleBFS findSAP = new DoubleBFS(G, v, w);
        return findSAP.sap();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        DoubleBFS findSCA = new DoubleBFS(G, v, w);
        return findSCA.sca();
    }

    // for unit testing
    public static void main(String[] args) {

    }
}
