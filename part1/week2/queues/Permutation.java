import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]), c = 1;

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (int i = 0; i < k; ++i) {
            queue.enqueue(StdIn.readString());
        }

        while (!StdIn.isEmpty()) {
            String string = StdIn.readString();
            if (StdRandom.uniform(c + k) >= c) {
                queue.dequeue();
                queue.enqueue(string);
            }
            ++c;
        }

        for (int i = 0; i < k; ++i)
            StdOut.println(queue.dequeue());

    }
}
