/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 6 2020
 *  Description: Given a list of WordNet nouns, find which is the least related
 *               to the others
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int d;
        int max = Integer.MIN_VALUE;
        String s = null;
        for (String i : nouns) {
            d = 0;
            for (String j : nouns)
                d += wordnet.distance(i, j);
            if (d > max) {
                max = d;
                s = i;
            }
        }
        return s;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
