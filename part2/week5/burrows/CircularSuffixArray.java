import java.util.Arrays;

public class CircularSuffixArray {

    private final int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        Integer[] mids = new Integer[s.length()];
        for (int i = 0; i < s.length(); ++i)
            mids[i] = i;
        Arrays.sort(mids, (i, j) -> {
            for (int k = 0; k < s.length(); ++i, ++j, ++k) {
                int t = Character.compare(s.charAt(i % s.length()), s.charAt(j % s.length()));
                if (t != 0)
                    return t;
            }
            return 0;
        });

        indexes = new int[s.length()];
        for (int i = 0; i < s.length(); ++i)
            indexes[i] = mids[i];
    }

    // length of s
    public int length() {
        return indexes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= indexes.length)
            throw new IllegalArgumentException();
        return indexes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray array = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; ++i)
            System.out.println(array.index(i));
    }
}
