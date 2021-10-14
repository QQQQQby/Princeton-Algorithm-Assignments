import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;

public class WordNet {

    private final HashMap<String, Bag<Integer>> nounToIndexesMap;
    private final String[] synsets;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String pathToSynsets, String pathToHypernyms) {
        checkNull(pathToSynsets);
        checkNull(pathToHypernyms);

        In in = new In(pathToSynsets);
        String[] lines = in.readAll().strip().split("\n");
        in.close();
        nounToIndexesMap = new HashMap<>();
        synsets = new String[lines.length];
        for (String line : lines) {
            String[] words = line.split(",");
            int v = Integer.parseInt(words[0]);
            synsets[v] = words[1];
            for (String noun : words[1].split(" "))
                nounToIndexesMap.computeIfAbsent(noun, key -> new Bag<>()).add(v);
        }

        Digraph graph = new Digraph(lines.length);
        in = new In(pathToHypernyms);
        while (!in.isEmpty()) {
            String[] ids = in.readLine().split(",");
            int v = Integer.parseInt(ids[0]);
            for (int j = 1; j < ids.length; ++j)
                graph.addEdge(v, Integer.parseInt(ids[j]));
        }
        in.close();

        boolean flag = false;
        for (int v = 0; v < graph.V(); ++v) {
            if (graph.outdegree(v) == 0) {
                if (flag)
                    throw new IllegalArgumentException();
                flag = true;
            }
        }

        int[] statuses = new int[graph.V()];
        for (int v = 0; v < graph.V(); ++v)
            if (statuses[v] == 0)
                checkCircle(graph, v, statuses);
        sap = new SAP(graph);
    }

    private void checkCircle(Digraph graph, int v, int[] statuses) {
        statuses[v] = 2;
        for (int w : graph.adj(v)) {
            if (statuses[w] == 2)
                throw new IllegalArgumentException();
            if (statuses[w] == 0)
                checkCircle(graph, w, statuses);
        }
        statuses[v] = 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIndexesMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounToIndexesMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        if (nounA.equals(nounB))
            return 0;
        return sap.length(nounToIndexesMap.get(nounA), nounToIndexesMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return synsets[sap.ancestor(nounToIndexesMap.get(nounA), nounToIndexesMap.get(nounB))];
    }


    private void checkNull(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            int distance = wordNet.distance(nounA, nounB);
            String sap = wordNet.sap(nounA, nounB);
            StdOut.printf("distance = %d, sap = \"%s\"\n", distance, sap);
        }
    }
}
