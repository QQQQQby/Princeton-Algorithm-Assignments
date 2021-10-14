import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {

    private final int[][] tiles;
    private final int n;
    private int rowBlank, colBlank;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; ++i)
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, n);
        rowBlank = -1;
        colBlank = -1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) {
                    rowBlank = i;
                    colBlank = j;
                    break;
                }
            }
            if (rowBlank != -1)
                break;
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n);
        builder.append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(' ');
                builder.append(tiles[i][j]);
                builder.append(' ');
            }
            if (i != n - 1)
                builder.append('\n');
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int ans = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != i * n + j + 1)
                    ++ans;
        return ans;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int ans = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != 0)
                    ans += Math.abs((tiles[i][j] - 1) / n - i) + Math.abs((tiles[i][j] - 1) % n - j);
        return ans;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return rowBlank == n - 1 && colBlank == n - 1 && hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null || y.getClass() != getClass())
            return false;
        Board that = (Board) y;
        if (dimension() != that.dimension())
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (tiles[i][j] != that.tiles[i][j])
                    return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return () -> new Iterator<>() {

            private final List<Board> neighborList = getNeighbors();
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < neighborList.size();
            }

            @Override
            public Board next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return neighborList.get(i++);
            }
        };
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i1 = 0, j1 = 0, i2 = 0, j2 = 1;
        if (tiles[i1][j1] == 0)
            ++i1;
        if (tiles[i2][j2] == 0)
            ++i2;
        Board that = new Board(tiles);
        that.swap(i1, j1, i2, j2);
        return that;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        int temp = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = temp;
    }

    private List<Board> getNeighbors() {
        List<Board> ans = new ArrayList<>();
        if (rowBlank - 1 >= 0) {
            Board that = new Board(tiles);
            that.swap(that.rowBlank, that.colBlank, that.rowBlank - 1, that.colBlank);
            --that.rowBlank;
            ans.add(that);
        }
        if (rowBlank + 1 < n) {
            Board that = new Board(tiles);
            that.swap(that.rowBlank, that.colBlank, that.rowBlank + 1, that.colBlank);
            ++that.rowBlank;
            ans.add(that);
        }
        if (colBlank - 1 >= 0) {
            Board that = new Board(tiles);
            that.swap(that.rowBlank, that.colBlank, that.rowBlank, that.colBlank - 1);
            --that.colBlank;
            ans.add(that);
        }
        if (colBlank + 1 < n) {
            Board that = new Board(tiles);
            that.swap(that.rowBlank, that.colBlank, that.rowBlank, that.colBlank + 1);
            ++that.colBlank;
            ans.add(that);
        }
        return ans;
    }

    public static void main(String[] args) {
        Board board = new Board(new int[][]{{7, 8, 0}, {4, 5, 6}, {1, 2, 3}});
        System.out.println(board.isGoal());
        System.out.println(board);
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor.isGoal());
            System.out.println(neighbor);
            System.out.println(neighbor.hamming());
            System.out.println(neighbor.manhattan());
        }
    }
}