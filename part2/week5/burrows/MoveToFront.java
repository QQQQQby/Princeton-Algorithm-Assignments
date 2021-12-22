import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = new char[EXTENDED_ASCII];
        for (char i = 0; i < EXTENDED_ASCII; ++i)
            chars[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            if (chars[0] == c) {
                BinaryStdOut.write((byte) 0);
                continue;
            }

            char i;
            for (i = 1; i < EXTENDED_ASCII; ++i)
                if (chars[i] == c)
                    break;

            BinaryStdOut.write((byte) i);
            char t = chars[i];
            for (; i >= 1; --i)
                chars[i] = chars[i - 1];
            chars[0] = t;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[EXTENDED_ASCII];
        for (char i = 0; i < EXTENDED_ASCII; ++i)
            chars[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            if (i == 0) {
                BinaryStdOut.write(chars[0]);
                continue;
            }

            BinaryStdOut.write(chars[i]);
            char t = chars[i];
            for (; i >= 1; --i)
                chars[i] = chars[i - 1];
            chars[0] = t;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        } else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
