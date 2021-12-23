public class CircularSuffixArray {

    private final int[] indexes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        indexes = new int[s.length()];
        for (int i = 0; i < s.length(); ++i)
            indexes[i] = i;
        sort(s, 0, s.length() - 1, 0);
    }

    // 3-way quick sort starting at dth character
    private void sort(String s, int lo, int hi, int d) {
        if (lo >= hi || d >= s.length())
            return;

        int lt = lo, gt = hi;
        int mid = s.charAt((indexes[lo] + d) % s.length());
        int i = lo + 1;
        while (i <= gt) {
            int c = s.charAt((indexes[i] + d) % s.length());
            if (c < mid) swap(lt++, i++);
            else if (c > mid) swap(i, gt--);
            else ++i;
        }

        sort(s, lo, lt - 1, d);
        sort(s, lt, gt, d + 1);
        sort(s, gt + 1, hi, d);
    }

    private void swap(int i, int j) {
        int t = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = t;
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
        CircularSuffixArray array = new CircularSuffixArray("couscous");
        for (int i = 0; i < array.length(); ++i)
            System.out.println(array.index(i));
    }
}
