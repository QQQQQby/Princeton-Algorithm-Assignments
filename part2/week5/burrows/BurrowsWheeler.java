import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BurrowsWheeler {
    private static final int EXTENDED_ASCII = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder builder = new StringBuilder();
        while (!BinaryStdIn.isEmpty())
            builder.append(BinaryStdIn.readChar());

        CircularSuffixArray array = new CircularSuffixArray(builder.toString());
        for (int i = 0; i < builder.length(); ++i) {
            if (array.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < builder.length(); ++i)
            BinaryStdOut.write(builder.charAt((array.index(i) + builder.length() - 1) % builder.length()));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();

        StringBuilder builder = new StringBuilder();
        while (!BinaryStdIn.isEmpty())
            builder.append(BinaryStdIn.readChar());

        List<Stack<Integer>> stacks = new ArrayList<>(EXTENDED_ASCII);
        for (int i = 0; i < EXTENDED_ASCII; ++i)
            stacks.add(new Stack<>());
        for (int i = builder.length() - 1; i >= 0; --i)
            stacks.get(builder.charAt(i)).push(i);

        char[] sorted = new char[builder.length()];
        for (int i = 0; i < builder.length(); ++i)
            sorted[i] = builder.charAt(i);
        Arrays.sort(sorted);

        int[] next = new int[builder.length()];
        for (int i = 0; i < builder.length(); ++i)
            next[i] = stacks.get(sorted[i]).pop();

        int curr = next[first];
        for (int i = 1; i < builder.length(); ++i) {
            BinaryStdOut.write(builder.charAt(curr));
            curr = next[curr];
        }

        BinaryStdOut.write(builder.charAt(first));
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            BurrowsWheeler.transform();
        else if (args[0].equals("+"))
            BurrowsWheeler.inverseTransform();
    }
}
