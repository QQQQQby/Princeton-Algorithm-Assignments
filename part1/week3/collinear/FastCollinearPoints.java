import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] segments;
    private final Point[] aux;

    /**
     * Finds all line segments containing 4 points.
     */
    public FastCollinearPoints(final Point[] ps) {
        checkPoints(ps);
        if (ps.length < 4) {
            aux = null;
            segments = new LineSegment[0];
            return;
        }
        Point[] points = ps.clone();
        aux = new Point[points.length];

        Point[] pointsCopy = new Point[points.length];
        sort(points, Point::compareTo);

        List<LineSegment> segmentList = new ArrayList<>();
        for (Point point : points) {
            System.arraycopy(points, 0, pointsCopy, 0, points.length);
            sort(pointsCopy, point.slopeOrder());
            // Compare point(pointsCopy[0]) with each point in pointsCopy[1:]
            double lastSlope = point.slopeTo(pointsCopy[1]);
            int len = 2;
            for (int j = 2; j < points.length; ++j) {
                double currSlope = point.slopeTo(pointsCopy[j]);
                if (currSlope == lastSlope) {
                    ++len;
                } else {
                    if (len >= 4 && point.compareTo(pointsCopy[j - len + 1]) < 0)
                        segmentList.add(new LineSegment(point, pointsCopy[j - 1]));
                    len = 2;
                    lastSlope = currSlope;
                }
            }
            if (len >= 4 && point.compareTo(pointsCopy[points.length - len + 1]) < 0)
                segmentList.add(new LineSegment(point, pointsCopy[points.length - 1]));
        }
        segments = new LineSegment[segmentList.size()];
        segmentList.toArray(segments);
    }

    /**
     * The number of line segments.
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * The line segments.
     */
    public LineSegment[] segments() {
        return segments.clone();
    }

    private void sort(Point[] points, Comparator<Point> comparator) {
        sort(points, 0, points.length - 1, comparator);
    }

    private void sort(Point[] points, int lo, int hi, Comparator<Point> comparator) {
        if (lo >= hi)
            return;
        int mid = lo + (hi - lo) / 2;
        sort(points, lo, mid, comparator);
        sort(points, mid + 1, hi, comparator);
        merge(points, lo, mid, hi, comparator);
    }

    private void merge(Point[] points, int lo, int mid, int hi, Comparator<Point> comparator) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            if (i > mid)
                aux[k] = points[j++];
            else if (j > hi)
                aux[k] = points[i++];
            else if (comparator.compare(points[j], points[i]) < 0)
                aux[k] = points[j++];
            else
                aux[k] = points[i++];
        }
        System.arraycopy(aux, lo, points, lo, hi - lo + 1);
    }

    private void checkPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (Point point : points)
            if (point == null)
                throw new IllegalArgumentException();
        for (int i = 0; i < points.length; ++i)
            for (int j = i + 1; j < points.length; ++j)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        // read the n points from a file
        int n = StdIn.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setPenRadius(0.01);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        StdDraw.setPenRadius(0.001);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
