public class BoggleSolver {

    private Node trieRoot;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trieRoot = null;
        for (String word : dictionary)
            trieRoot = addWord(trieRoot, word, 0);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
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

    private Node addWord(Node p, String s, int i) {
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

    private boolean wordIsIn(Node p, String s, int i) {
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
