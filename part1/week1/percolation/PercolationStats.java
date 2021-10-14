import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double mean, stddev, confLo, confHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        double[] thresholds = new double[trials];
        Percolation percolation;
        for (int t = 0; t < trials; ++t) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row, col;
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                } while (percolation.isOpen(row, col));
                percolation.open(row, col);
            }
            thresholds[t] = (double) percolation.numberOfOpenSites() / n / n;
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        confLo = mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
        confHi = mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("mean\t\t\t\t\t= %.16f%n", stats.mean());
        System.out.printf("stddev\t\t\t\t\t= %.16f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]%n", stats.confidenceLo(), stats.confidenceHi());
    }
}
