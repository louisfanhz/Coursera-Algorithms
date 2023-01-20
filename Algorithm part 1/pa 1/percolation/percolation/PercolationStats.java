/* *****************************************************************************
 *  Name:              Haozhi Fan
 *  Coursera User ID:  N/A
 *  Last modified:     27/06/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] fracOpenSites;
    private int ntrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("n-by-n grid must have dimension > 0");
        if (trials <= 0)
            throw new IllegalArgumentException("number of trial must be > 0");

        ntrials = trials;
        fracOpenSites = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            fracOpenSites[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fracOpenSites);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fracOpenSites);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - (1.96 * stddev()) / Math.sqrt(ntrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + (1.96 * stddev()) / Math.sqrt(ntrials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, T);

        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = " + String.format("[%f, %f]",
                                                                    percStats.confidenceLo(),
                                                                    percStats.confidenceHi()));

    }
}
