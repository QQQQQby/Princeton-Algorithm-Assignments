import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf, uf2;
    private final int n;
    private int numOpen;
    private boolean[] openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        numOpen = 0;
        openSites = new boolean[n * n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateCoordinate(row, col);
        int currIndex = mapIndex(row, col);
        if (openSites[currIndex])
            return;
        openSites[currIndex] = true;
        ++numOpen;
        if (row - 1 >= 1 && openSites[mapIndex(row - 1, col)]) {
            uf.union(currIndex, mapIndex(row - 1, col));
            uf2.union(currIndex, mapIndex(row - 1, col));
        }
        if (row + 1 <= n && openSites[mapIndex(row + 1, col)]) {
            uf.union(currIndex, mapIndex(row + 1, col));
            uf2.union(currIndex, mapIndex(row + 1, col));
        }
        if (col - 1 >= 1 && openSites[mapIndex(row, col - 1)]) {
            uf.union(currIndex, mapIndex(row, col - 1));
            uf2.union(currIndex, mapIndex(row, col - 1));
        }
        if (col + 1 <= n && openSites[mapIndex(row, col + 1)]) {
            uf.union(currIndex, mapIndex(row, col + 1));
            uf2.union(currIndex, mapIndex(row, col + 1));
        }

        if (row == 1) {
            uf.union(currIndex, n * n);
            uf2.union(currIndex, n * n);
        }
        if (row == n)
            uf.union(currIndex, n * n + 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateCoordinate(row, col);
        return openSites[mapIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateCoordinate(row, col);
        return openSites[mapIndex(row, col)] && (uf2.find(mapIndex(row, col)) == uf2.find(n * n));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(n * n) == uf.find(n * n + 1);
    }

    private void validateCoordinate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
    }

    private int mapIndex(int row, int col) {
        return (row - 1) * n + col - 1;
    }
}
