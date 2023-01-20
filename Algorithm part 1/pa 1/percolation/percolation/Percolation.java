/* *****************************************************************************
 *  Name:              Haozhi Fan
 *  Coursera User ID:  N/A
 *  Last modified:     27/06/2020
 **************************************************************************** */

public class Percolation {
    private int[] parent;
    private int[] size;
    private boolean[] isOpen;
    private int sideLength;
    private int numOpen;
    private int vTop;
    private int vBot;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("cannot generate a n-by-n grid where n <= 0");
        }

        sideLength = n;
        int N = n * n;
        numOpen = 0;
        vTop = N;
        vBot = N + 1;
        isOpen = new boolean[N + 2];
        parent = new int[N + 2];
        size = new int[N + 2];

        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        parent[vTop] = vTop;
        parent[vBot] = vBot;
        size[vTop] = n;
        size[vBot] = n;

        for (int i = 0; i < n; i++) {
            parent[i] = vTop;
            parent[N - 1 - i] = vBot;
        }
    }

    private int convertID(int row, int col) {
        row--;
        col--;
        if (row < 0 || col < 0 || row >= sideLength || col >= sideLength)
            return -1;
        else
            return (row * sideLength + col);
    }

    private int find(int p) {
        while (p != parent[p])
            p = parent[p];
        return p;
    }

    private void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int p = convertID(row, col);

        if (!isOpen(row, col)) {
            isOpen[p] = true;
            numOpen++;
        }
        else return;

        int[] neighbors = new int[4];  // maximum 4 neighbouring sites
        neighbors[0] = convertID(row - 1, col);
        neighbors[1] = convertID(row + 1, col);
        neighbors[2] = convertID(row, col - 1);
        neighbors[3] = convertID(row, col + 1);

        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] != -1 && isOpen[neighbors[i]])
                union(p, neighbors[i]);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int p = convertID(row, col);
        return isOpen[p];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int p = convertID(row, col);
        if (!isOpen[p])
            return false;
        return find(p) == find(vTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return find(vTop) == find(vBot);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
