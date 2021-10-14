import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;


public class Solver {

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final int moves, pri;
        private final Node pre;

        Node(Board board, Node pre) {
            this.board = board;
            moves = pre == null ? 0 : pre.moves + 1;
            this.pre = pre;
            pri = board.manhattan() + moves;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(pri, that.pri);
        }
    }

    private final Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        checkBoard(initial);
        MinPQ<Node> pq = new MinPQ<>();
        pq.insert(new Node(initial, null));

        MinPQ<Node> twinPq = new MinPQ<>();
        twinPq.insert(new Node(initial.twin(), null));

        while (true) {
            Node node = pq.delMin();
            if (node.board.isGoal()) {
                solution = new Stack<>();
                while (node != null) {
                    solution.push(node.board);
                    node = node.pre;
                }
                break;
            }
            for (Board neighbor : node.board.neighbors())
                if (node.pre == null || !neighbor.equals(node.pre.board))
                    pq.insert(new Node(neighbor, node));


            Node twinNode = twinPq.delMin();
            if (twinNode.board.isGoal()) {
                solution = null;
                break;
            }
            for (Board neighbor : twinNode.board.neighbors())
                if (twinNode.pre == null || !neighbor.equals(twinNode.pre.board))
                    twinPq.insert(new Node(neighbor, twinNode));
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solution;
    }

    private void checkBoard(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
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
