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

        int[] indexes = new int[EXTENDED_ASCII + 1];
        for (int i = 0; i < s.length(); ++i)
            ++indexes[s.charAt(i) + 1];
        for (int i = 1; i <= EXTENDED_ASCII; ++i)
            indexes[i] += indexes[i - 1];

        char[] sorted = new char[s.length()];
        int[] next = new int[s.length()];
        for (int i = 0; i < s.length(); ++i) {
            int j = indexes[s.charAt(i)]++;
            sorted[j] = s.charAt(i);
            next[j] = i;
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
