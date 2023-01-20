/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: 12/20/2020
 *  Description: Type to hole words from input dictionary file
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Dictionary {
    private static final int R = 26;
    private Node root;
    private int n;

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public Dictionary(String filename) {
        In in = new In(filename);

        while (in.hasNextLine())
            add(in.readLine());
    }

    public Dictionary(String[] dict) {
        for (String s : dict) { add(s); }
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        }
        else {
            int c = key.charAt(d) - 'A';
            x.next[c] = add(x.next[c], key, d+1);
        }
        return x;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d+1);
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    public boolean isValidPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        if (x == null) return false;
        if (x.isString) return true;
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) return true;
        }
        return false;
    }

    public int size() { return n; }

    public static void main(String[] args) {
        Dictionary dict = new Dictionary(args[0]);
        In in = new In();

        for (int i = 0; i < 10; i++) {
            StdOut.println(dict.isValidPrefix(in.readLine()));
        }
    }
}
