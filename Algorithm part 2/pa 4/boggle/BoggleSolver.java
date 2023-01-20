/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: 12/20/2020
 *  Description: Class to solve the boggle puzzle
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final Dictionary dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        dict = new Dictionary(dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> allValidWords = new SET<String>();
        Bag<Integer>[] adj = buildAdj(board);
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.cols(); c++) {
                search(board, allValidWords, adj, r, c);
            }
        }
        return allValidWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    public int scoreOf(String word) {
        int wordLength = word.length();
        if (!dict.contains(word)) return 0;
        switch (wordLength) {
            case 1: return 0;
            case 2: return 0;
            case 3: return 1;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            default: return 11;
        }
    }

    private void search(BoggleBoard board, SET<String> allValidWords,
                        Bag<Integer>[] adj, int r, int c) {
        boolean[] marked = new boolean[board.rows() * board.cols()];
        int s = get1DIndex(r, c, board.rows(), board.cols());
        StringBuilder sb = new StringBuilder();
        dfs(board, allValidWords, adj, marked, sb, s);
    }

    private void dfs(BoggleBoard board, SET<String> allValidWords, Bag<Integer>[] adj,
                     boolean[] marked, StringBuilder sb, int v) {
        boolean isQ = false;
        char c = board.getLetter(getR(v, board), getC(v, board));
        if (c == 'Q') isQ = true;

        sb.append(c);
        if (isQ) sb.append('U');
        String str = sb.toString();
        if (dict.contains(str) && str.length() > 2) allValidWords.add(str);

        if (str.length() > 3 && !dict.isValidPrefix(str)) {
            sb.deleteCharAt(sb.length() - 1);
            if (isQ) sb.deleteCharAt(sb.length() - 1);
            return;
        }

        marked[v] = true;

        for (int w : adj[v]) {
            if (!marked[w]) {
                dfs(board, allValidWords, adj, marked, sb, w);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        if (isQ) sb.deleteCharAt(sb.length() - 1);
        marked[v] = false;
    }

    private Bag<Integer>[] buildAdj(BoggleBoard board) {
        Bag<Integer>[] adj = (Bag<Integer>[]) new Bag[board.rows() * board.cols()];
        int rows = board.rows();
        int cols = board.cols();
        for (int v = 0; v < rows * cols; v++) {
            adj[v] = new Bag<Integer>();
        }

        // for all dices add adjacency dices one by one
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int i = get1DIndex(r, c, rows, cols);
                int upperLeft = get1DIndex(r - 1, c - 1, rows, cols);
                int left = get1DIndex(r, c - 1, rows, cols);
                int lowerLeft = get1DIndex(r + 1, c - 1, rows, cols);
                int up = get1DIndex(r - 1, c, rows, cols);
                int down = get1DIndex(r + 1, c, rows, cols);
                int upperRight = get1DIndex(r - 1, c + 1, rows, cols);
                int right = get1DIndex(r, c + 1, rows, cols);
                int lowerRight = get1DIndex(r + 1, c + 1, rows, cols);
                if (upperLeft != -1) adj[i].add(upperLeft);
                if (left != -1) adj[i].add(left);
                if (lowerLeft != -1) adj[i].add(lowerLeft);
                if (up != -1) adj[i].add(up);
                if (down != -1) adj[i].add(down);
                if (upperRight != -1) adj[i].add(upperRight);
                if (right != -1) adj[i].add(right);
                if (lowerRight != -1) adj[i].add(lowerRight);
            }
        }
        return adj;
    }

    private int get1DIndex(int r, int c, int rows, int cols) {
        if (r < 0 || c < 0 || r >= rows || c >= cols) return -1;
        return r * cols + c;
    }

    private int getR(int x, BoggleBoard board) { return x / board.cols(); }

    private int getC(int x, BoggleBoard board) { return x % board.cols(); }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        // BoggleBoard board = new BoggleBoard();
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

        /*
        In in = new In(args[0]);
        String[] dict = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dict);
        BoggleBoard board = new BoggleBoard(args[1]);

        for (String s : solver.getAllValidWords(board)) {
            StdOut.println(s);
        }

         */

        long endTime = System.nanoTime();
        StdOut.println("Running time is: " + (endTime - startTime) / 1000000);


        /* FIXME: test build adjacency list
        Bag<Integer>[] adj = solver.buildAdj(board);
        int counter = 0;
        for (Bag<Integer> b : adj) {
            StdOut.print("adj for " + counter++ + " is: ");
            for (int i : b) {
                StdOut.print(i + ", ");
            }
            StdOut.println();
        }
         */

    }
}