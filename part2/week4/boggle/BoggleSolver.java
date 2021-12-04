import edu.princeton.cs.algs4.Bag;

import java.util.HashSet;

public class BoggleSolver {

    private Node trieRoot;

    private BoggleBoard board;
    private boolean[][] visited;
    private Bag<String> words;
    private HashSet<Node> visitedNodes;

    private static int[][] shifts = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trieRoot = null;
        for (String word : dictionary)
            trieRoot = addWord(trieRoot, word, 0);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
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

        char c = board.getLetter(i, j);
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

        for (int[] shift : shifts) {
            int ii = i + shift[0], jj = j + shift[1];
            if (ii >= 0 && ii < board.rows() && jj >= 0 && jj < board.cols() && !visited[ii][jj])
                computeValidWords(p.mid, ii, jj, new StringBuilder(builder));
        }
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
}