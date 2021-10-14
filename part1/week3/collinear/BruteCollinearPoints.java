import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] segments;

    /**
     * Finds all line segments containing 4 points.
     */
    public BruteCollinearPoints(final Point[] points) {
        checkPoints(points);
        if (points.length < 4) {
            segments = new LineSegment[0];
            return;
        }
        List<LineSegment> segmentList = new ArrayList<>();
        boolean[] flags = new boolean[points.length];
        for (int i = 0; i < points.length; ++i) {
            Arrays.fill(flags, false);
            for (int j = i + 1; j < points.length; ++j) {
                double slope = points[i].slopeTo(points[j]);

                int k = j + 1;
                while (k < points.length && (flags[k] || points[i].slopeTo(points[k]) != slope))
                    ++k;
                if (k >= points.length)
                    continue;
                flags[k] = true;

                int f = k + 1;
                while (f < points.length && (flags[f] || points[i].slopeTo(points[f]) != slope))
                    ++f;
                if (f >= points.length)
                    continue;
                flags[f] = true;
                Point p = points[i], q = points[i];
                if (points[j].compareTo(p) < 0)
                    p = points[j];
                if (points[j].compareTo(q) > 0)
                    q = points[j];

                if (points[k].compareTo(p) < 0)
                    p = points[k];
                if (points[k].compareTo(q) > 0)
                    q = points[k];

                if (points[f].compareTo(p) < 0)
                    p = points[f];
                if (points[f].compareTo(q) > 0)
                    q = points[f];

                segmentList.add(new LineSegment(p, q));
            }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
