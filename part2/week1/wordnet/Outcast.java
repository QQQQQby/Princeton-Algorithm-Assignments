import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {         // constructor takes a WordNet object
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {   // given an array of WordNet nouns, return an outcast
        int[] distanceSums = new int[nouns.length];
        for (int i = 0; i < nouns.length; ++i) {
            for (int j = i + 1; j < nouns.length; ++j) {
                int temp = wordNet.distance(nouns[i], nouns[j]);
                distanceSums[i] += temp;
                distanceSums[j] += temp;
            }
        }
        int maxIndex = 0, maxDist = distanceSums[0];
        for (int i = 0; i < nouns.length; i++) {
            if (distanceSums[i] > maxDist) {
                maxDist = distanceSums[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) { // see test client below
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        In in = new In(args[2]);
        String[] nouns = in.readAllStrings();
        StdOut.println(outcast.outcast(nouns));
    }
}