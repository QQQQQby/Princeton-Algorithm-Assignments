import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        Node head = new Node((char) 0), p = head, pre;
        for (char c = 1; c < EXTENDED_ASCII; ++c) {
            p.next = new Node(c);
            p = p.next;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            if (head.c == c) {
                BinaryStdOut.write((byte) 0);
                continue;
            }

            int i = 1;
            pre = head;
            p = head.next;
            while (p.c != c) {
                pre = p;
                p = p.next;
                ++i;
            }

            BinaryStdOut.write((byte) i);
            pre.next = p.next;
            p.next = head;
            head = p;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        Node head = new Node((char) 0), p = head, pre;
        for (char c = 1; c < EXTENDED_ASCII; ++c) {
            p.next = new Node(c);
            p = p.next;
        }

        while (!BinaryStdIn.isEmpty()) {
            char n = BinaryStdIn.readChar();
            if (n == 0) {
                BinaryStdOut.write(head.c);
                continue;
            }

            pre = head;
            for (int i = 1; i < n; ++i)
                pre = pre.next;
            p = pre.next;

            BinaryStdOut.write(p.c);
            pre.next = p.next;
            p.next = head;
            head = p;
        }
        BinaryStdOut.close();
    }

    private static class Node {
        private final char c;
        private Node next;

        private Node(char c) {
            this.c = c;
        }
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
