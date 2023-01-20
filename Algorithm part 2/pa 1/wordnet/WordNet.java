/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 6 2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Bag<Integer>> nouns;
    private final ArrayList<String> synsets;
    private final ArrayList<Bag<Integer>> hypernyms;
    private final Digraph wordnet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        In s = new In(synsets);
        In h = new In(hypernyms);
        this.synsets = new ArrayList<String>();
        this.hypernyms = new ArrayList<Bag<Integer>>();

        while (s.hasNextLine()) {
            String[] str = s.readLine().split(",");
            String word = str[1];
            this.synsets.add(word);
        }

        while (h.hasNextLine()) {
            String[] row = h.readLine().split(",");
            Bag<Integer> integerBag = new Bag<Integer>();
            for (int i = 1; i < row.length; i++)
                integerBag.add(Integer.parseInt(row[i]));
            this.hypernyms.add(integerBag);
        }

        nouns = parseNouns(this.synsets);
        wordnet = buildDigraph();
    }

    /**
     * parse nouns in every synsets, returns
     *
     * @param s synsets containing all noun synsets in WordNet
     * @return a symbol table that uses noun as key, and its id in synsets as value
     */
    private ST<String, Bag<Integer>> parseNouns(ArrayList<String> s) {
        ST<String, Bag<Integer>> nounSet = new ST<String, Bag<Integer>>();
        for (int i = 0; i < s.size(); i++) {
            String[] str = s.get(i).split(" ");
            for (String si : str) {
                if (nounSet.contains(si)) nounSet.get(si).add(i);
                else {
                    Bag<Integer> newBag = new Bag<Integer>();
                    newBag.add(i);
                    nounSet.put(si, newBag);
                }
            }
        }
        return nounSet;
    }

    /**
     * build a digraph that uses id(s) of synset as vertices and its hypernym(s) as edge(s)
     *
     * @return a built digraph
     */
    private Digraph buildDigraph() {
        Digraph g = new Digraph(hypernyms.size());
        for (int i = 0; i < hypernyms.size(); i++) {
            for (int edge : hypernyms.get(i))
                g.addEdge(i, edge);
        }

        // check if the graph is a DAG
        DirectedCycle checkCycle = new DirectedCycle(g);
        if (checkCycle.hasCycle())
            throw new IllegalArgumentException("Input argument is not a DAG");
        return g;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.contains(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        validateNoun(nounA, nounB);

        SAP query = new SAP(wordnet);
        return query.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        validateNoun(nounA, nounB);

        SAP query = new SAP(wordnet);
        int ancestor = query.ancestor(nouns.get(nounA), nouns.get(nounB));
        return synsets.get(ancestor);
    }

    private void validateNoun(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException("null noun(s)");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("noun(s) not found");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wt = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String a = StdIn.readString();
            String b = StdIn.readString();
            StdOut.printf("distance: %d, ancestor: %s\n", wt.distance(a, b), wt.sap(a, b));
        }

        /*
        StdOut.println("Synsets:");
        for (String s : wt.nouns()) StdOut.println(s);
        StdOut.println("hypernyms:");
        for (Bag<Integer> bag : wt.hypernyms)
            if (bag.isEmpty()) StdOut.println("EMPTY");
            else for (int i : bag) StdOut.println(i);
         */

        /*
        for (int i : wt.synsets.get("unit_cell"))
            StdOut.println(i);

         */

        /*
        StdOut.println("nouns:");
        for (String key : wt.nouns) {
            if (wt.nouns.get(key).size() > 1) {
                StdOut.print("noun: ");
                StdOut.print(key + ", ");
                StdOut.print("id(s) in synsets: ");
                for (int id : wt.nouns.get(key)) {
                    StdOut.print(id + " ");
                }

                StdOut.print("\n");
            }
        }

         */
    }
}
