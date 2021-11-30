import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class BoggleSolver {

    private final TrieSet set;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        set = new TrieSet();
        for (String word : dictionary)
            set.add(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!set.contains(word))
            return 0;

        int len = word.length();
        if (len <= 2)
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

    private static class TrieSet implements Iterable<String> {

        private Node root;      // root of trie
        private int n;          // number of keys in trie

        // R-way trie node
        private static class Node {
            private Node[] next = new Node[26];
            private boolean isString;
        }

        /**
         * Initializes an empty set of strings.
         */
        public TrieSet() {
            root = null;
            n = 0;
        }

        /**
         * Does the set contain the given key?
         *
         * @param key the key
         * @return {@code true} if the set contains {@code key} and
         * {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c], key, d + 1);
        }

        /**
         * Adds the key to the set if it is not already present.
         *
         * @param key the key to add
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString)
                    ++n;
                x.isString = true;
            } else {
                char c = key.charAt(d);
                x.next[c] = add(x.next[c], key, d + 1);
            }
            return x;
        }

        /**
         * Returns the number of strings in the set.
         *
         * @return the number of strings in the set
         */
        public int size() {
            return n;
        }

        /**
         * Is the set empty?
         *
         * @return {@code true} if the set is empty, and {@code false} otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        /**
         * Returns all of the keys in the set, as an iterator.
         * To iterate over all of the keys in a set named {@code set}, use the
         * foreach notation: {@code for (Key key : set)}.
         *
         * @return an iterator to all of the keys in the set
         */
        public Iterator<String> iterator() {
            Queue<String> results = new Queue<>();
            Node x = get(root, "", 0);
            collect(x, new StringBuilder(), results);
            return results.iterator();
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null) return;
            if (x.isString) results.enqueue(prefix.toString());
            for (char c = 0; c < 26; c++) {
                prefix.append(c);
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }
}
