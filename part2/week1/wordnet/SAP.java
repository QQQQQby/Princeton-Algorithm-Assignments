import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        checkNull(graph);
        this.graph = new Digraph(graph);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v == w) {
            checkVertex(v);
            return 0;
        }
        Stack<Integer> stackV = new Stack<>(), stackW = new Stack<>();
        stackV.push(v);
        stackW.push(w);
        return length(stackV, stackW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v == w) {
            checkVertex(v);
            return v;
        }
        Stack<Integer> stackV = new Stack<>(), stackW = new Stack<>();
        stackV.push(v);
        stackW.push(w);
        return ancestor(stackV, stackW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertexes(v);
        checkVertexes(w);
        int[] distancesFromV = distancesFrom(v);
        int[] distancesFromW = distancesFrom(w);
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); ++i)
            if (distancesFromV[i] != Integer.MAX_VALUE && distancesFromW[i] != Integer.MAX_VALUE)
                ans = Math.min(ans, distancesFromV[i] + distancesFromW[i]);
        if (ans == Integer.MAX_VALUE)
            return -1;
        return ans;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertexes(v);
        checkVertexes(w);
        int[] distancesFromV = distancesFrom(v);
        int[] distancesFromW = distancesFrom(w);
        int ans = -1, minLength = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); ++i) {
            if (distancesFromV[i] != Integer.MAX_VALUE && distancesFromW[i] != Integer.MAX_VALUE) {
                int temp = distancesFromV[i] + distancesFromW[i];
                if (temp < minLength) {
                    minLength = temp;
                    ans = i;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE)
            return -1;
        return ans;
    }


    private int[] distancesFrom(Iterable<Integer> v) {
        Queue<Integer> queue = new Queue<>();
        int[] ans = new int[graph.V()];
        for (int i = 0; i < graph.V(); ++i)
            ans[i] = Integer.MAX_VALUE;

        boolean[] visited = new boolean[graph.V()];
        for (int i : v) {
            visited[i] = true;
            queue.enqueue(i);
            ans[i] = 0;
        }
        while (!queue.isEmpty()) {
            int i = queue.dequeue();

            for (int j : graph.adj(i)) {
                if (!visited[j]) {
                    queue.enqueue(j);
                    visited[j] = true;
                    ans[j] = ans[i] + 1;
                }
            }
        }
        return ans;
    }


    private void checkVertexes(Iterable<Integer> v) {
        checkNull(v);
        for (Integer i : v) {
            checkNull(i);
            checkVertex(i);
        }
    }

    private void checkNull(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= graph.V())
            throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        SAP sap = new SAP(new Digraph(in));
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
