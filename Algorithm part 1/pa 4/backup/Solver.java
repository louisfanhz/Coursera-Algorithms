/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: July 19 2020
 *  Description: Solver class
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int move;
    private MinPQ<SearchNode> minPQ;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        minPQ = new MinPQ<SearchNode>(new NodeComparator());
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>(new NodeComparator());
        move = 0;

        minPQ.insert(new SearchNode(move, initial, null));
        minPQTwin.insert(new SearchNode(move, initial.twin(), null));
        while (true) {
            if (minPQ.min().getBoard().isGoal()) {
                solvable = true;
                break;
            }
            if (minPQTwin.min().getBoard().isGoal()) {
                solvable = false;
                break;
            }

            SearchNode delMin = minPQ.delMin();
            for (Board b : delMin.getBoard().neighbors()) {
                if (move > 0 && b.equals(delMin.getPrevious().getBoard()))
                    continue;
                minPQ.insert(new SearchNode(move, b, delMin));
            }


            SearchNode delMinTwin = minPQTwin.delMin();
            for (Board b : delMinTwin.getBoard().neighbors()) {
                if (move > 0 && b.equals(delMinTwin.getPrevious().getBoard()))
                    continue;
                minPQTwin.insert(new SearchNode(move, b, delMinTwin));
            }

            move++;
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int moves;

        public SearchNode(int m, Board b, SearchNode pre) {
            board = b;
            moves = m;
            previous = pre;
        }

        public int priority() {
            return moves + board.manhattan();
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
        else return move;
    }


    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Stack<Board> stack = new Stack<Board>();
        SearchNode lastNode = minPQ.delMin();
        stack.push(lastNode.getBoard());

        while (lastNode.getPrevious() != null) {
            stack.push(lastNode.getPrevious().getBoard());
            lastNode = lastNode.getPrevious();
        }

        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // int[][] tiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 8, 7, 0 } };
        int[][] tiles = { { 8, 6, 7 }, { 2, 5, 4 }, { 1, 3, 0 } };
        // int[][] tiles = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 15, 14, 0 } };
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        Stack<Board> trail = (Stack<Board>) solver.solution();
        if (trail == null)
            StdOut.println("the puzzle is unsolvable");
        else
            while (!trail.isEmpty()) StdOut.println(trail.pop());
        // int[][] tiles2 = { { 1, 2, 4 }, { 0, 5, 6 }, { 7, 8, 3 } };
        // Board board = new Board(tiles);
    }
}
