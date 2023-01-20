/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: July 19 2020
 *  Description: Solver class
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private boolean solvable;
    private SearchNode lastNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(new NodeComparator());
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(new NodeComparator());

        minPQ.insert(new SearchNode(0, initial, null));
        minPQTwin.insert(new SearchNode(0, initial.twin(), null));
        while (true) {
            SearchNode delMin = minPQ.delMin();
            SearchNode delMinTwin = minPQTwin.delMin();
            int move = delMin.moves();

            if (delMin.getBoard().isGoal()) {
                solvable = true;
                lastNode = delMin;
                break;
            }
            if (delMinTwin.getBoard().isGoal()) {
                solvable = false;
                break;
            }

            for (Board b : delMin.getBoard().neighbors()) {
                if (delMin.getPrevious() != null && b.equals(delMin.getPrevious().getBoard()))
                    continue;
                minPQ.insert(new SearchNode(move + 1, b, delMin));
            }


            for (Board b : delMinTwin.getBoard().neighbors()) {
                if (delMinTwin.getPrevious() != null &&
                        b.equals(delMinTwin.getPrevious().getBoard()))
                    continue;
                minPQTwin.insert(new SearchNode(move + 1, b, delMinTwin));
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previous;
        private final int moves;

        public SearchNode(int m, Board b, SearchNode pre) {
            board = b;
            moves = m;
            previous = pre;
        }

        public int priority() {
            return moves + board.manhattan();
        }

        public int moves() {
            return moves;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int compareTo(SearchNode that) {
            return (this.priority() - that.priority());
        }
    }

    private class NodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return (a.compareTo(b));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        else return lastNode.moves();
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Stack<Board> stack = new Stack<Board>();
        stack.push(lastNode.getBoard());

        while (lastNode.getPrevious() != null) {
            stack.push(lastNode.getPrevious().getBoard());
            lastNode = lastNode.getPrevious();
        }

        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
