/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Jul 19 2020
 *  Description: Board data type
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    private Board(Board b) {
        board = new int[b.dimension()][b.dimension()];
        for (int i = 0; i < b.dimension(); i++) {
            for (int j = 0; j < b.dimension(); j++) {
                board[i][j] = b.board[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String str = Integer.toString(board.length);
        for (int i = 0; i < board.length; i++) {
            str += "\n ";
            for (int j = 0; j < board[0].length; j++) {
                str += board[i][j] + "  ";
            }
        }
        return str;
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] != (i * board.length + j + 1)) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] != (i * board.length + j + 1)) {
                    int tRow = (board[i][j] - 1) / board.length;
                    int tCol = (board[i][j] - 1) % board.length;
                    manhattan += Math.abs(tRow - i) + Math.abs(tCol - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y.getClass() != this.getClass()) return false;
        return this.toString().equals(y.toString());
    }

    private void swap(int row1, int col1, int row2, int col2) {
        int swap;
        swap = board[row2][col2];
        board[row2][col2] = board[row1][col1];
        board[row1][col1] = swap;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int blankRow = -1;
        int blankCol = -1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

        Stack<Board> neighbors = new Stack<Board>();

        if (blankRow - 1 >= 0) {
            swap(blankRow, blankCol, blankRow - 1, blankCol);
            neighbors.push(new Board(this));
            swap(blankRow, blankCol, blankRow - 1, blankCol);
        }

        if (blankRow + 1 < board.length) {
            swap(blankRow, blankCol, blankRow + 1, blankCol);
            neighbors.push(new Board(this));
            swap(blankRow, blankCol, blankRow + 1, blankCol);
        }

        if (blankCol - 1 >= 0) {
            swap(blankRow, blankCol, blankRow, blankCol - 1);
            neighbors.push(new Board(this));
            swap(blankRow, blankCol, blankRow, blankCol - 1);
        }

        if (blankCol + 1 < board.length) {
            swap(blankRow, blankCol, blankRow, blankCol + 1);
            neighbors.push(new Board(this));
            swap(blankRow, blankCol, blankRow, blankCol + 1);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin;
        if (board[0][0] != 0 && board[0][1] != 0) {
            swap(0, 0, 0, 1);
            twin = new Board(this);
            swap(0, 0, 0, 1);
            return twin;
        }
        else {
            swap(1, 0, 1, 1);
            twin = new Board(this);
            swap(1, 0, 1, 1);
            return twin;
        }
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        // int[][] tiles2 = { { 1, 2, 4 }, { 0, 5, 6 }, { 7, 8, 3 } };
        Board board = new Board(tiles);
        // Stack<Board> neighbors = (Stack<Board>) board.neighbors();
        // Board board2 = new Board(tiles2);
        StdOut.println(board);
        StdOut.println("Hamming: " + board.hamming());
        StdOut.println("Manhattan: " + board.manhattan());
        // StdOut.println("Is this board the goal board? " + board.isGoal());
        // StdOut.println("Is this board the same as another board? " + board.equals(board2));
    }
}
