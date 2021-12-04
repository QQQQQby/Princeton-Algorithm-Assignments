import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {

    private final Node trieRoot;

    private BoggleBoard currBoard;
    private boolean[][] visited;
    private Bag<String> words;
    private HashSet<Node> visitedNodes;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        Node root = null;
        for (String word : dictionary)
            if (word.length() >= 3)
                root = addWord(root, word, 0);
        this.trieRoot = root;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.currBoard = board;
        words = new Bag<>();
        visited = new boolean[board.rows()][board.cols()];
        visitedNodes = new HashSet<>();
        for (int i = 0; i < board.rows(); ++i)
            for (int j = 0; j < board.cols(); ++j)
                computeValidWords(trieRoot, i, j, new StringBuilder());
        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if (len <= 2 || !wordIsIn(trieRoot, word, 0))
            return 0;
        if (len <= 4)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        return 11;
    }

    private void computeValidWords(Node p, int i, int j, StringBuilder builder) {
        char c = currBoard.getLetter(i, j);
        while (p != null) {
            if (p.c < c)
                p = p.right;
            else if (p.c > c)
                p = p.left;
            else
                break;
        }
        if (p == null)
            return;
        if (c == 'Q') {
            p = p.mid;
            while (p != null) {
                if (p.c < 'U')
                    p = p.right;
                else if (p.c > 'U')
                    p = p.left;
                else
                    break;
            }
            if (p == null) {
                return;
            }
            builder.append(c);
            c = 'U';
        }

        visited[i][j] = true;
        builder.append(c);
        if (p.isEnding && !visitedNodes.contains(p)) {
            words.add(builder.toString());
            visitedNodes.add(p);
        }
        for (int ii = i - 1; ii <= i + 1; ++ii)
            for (int jj = j - 1; jj <= j + 1; ++jj)
                if ((ii != i || jj != j) && ii >= 0 && ii < currBoard.rows() && jj >= 0 && jj < currBoard.cols() && !visited[ii][jj])
                    computeValidWords(p.mid, ii, jj, new StringBuilder(builder));
        visited[i][j] = false;
    }

    private static Node addWord(Node p, String s, int i) {
        char c = s.charAt(i);
        if (p == null) {
            p = new Node();
            p.c = c;
        }
        if (c < p.c)
            p.left = addWord(p.left, s, i);
        else if (c > p.c)
            p.right = addWord(p.right, s, i);
        else if (i < s.length() - 1)
            p.mid = addWord(p.mid, s, i + 1);
        else
            p.isEnding = true;
        return p;
    }

    private static boolean wordIsIn(Node p, String s, int i) {
        if (p == null)
            return false;
        char c = s.charAt(i);
        if (c < p.c)
            return wordIsIn(p.left, s, i);
        if (c > p.c)
            return wordIsIn(p.right, s, i);
        if (i < s.length() - 1)
            return wordIsIn(p.mid, s, i + 1);
        return p.isEnding;
    }

    private static class Node {
        private boolean isEnding;
        private char c;
        private Node left, mid, right;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
