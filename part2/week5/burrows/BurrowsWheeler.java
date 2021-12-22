import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int EXTENDED_ASCII = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();

        CircularSuffixArray array = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); ++i) {
            if (array.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); ++i)
            BinaryStdOut.write(s.charAt((array.index(i) + s.length() - 1) % s.length()));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int[] counts = new int[EXTENDED_ASCII + 1];
        char[] sorted = new char[s.length()];
        int[] next = new int[s.length()];
        for (int i = 0; i < s.length(); ++i)
            ++counts[s.charAt(i) + 1];
        for (int r = 0; r < EXTENDED_ASCII; ++r)
            counts[r + 1] += counts[r];
        for (int i = 0; i < s.length(); ++i) {
            int t = counts[s.charAt(i)]++;
            sorted[t] = s.charAt(i);
            next[t] = i;
        }

        for (int i = 0; i < s.length(); ++i) {
            BinaryStdOut.write(sorted[first]);
            first = next[first];
        }

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
