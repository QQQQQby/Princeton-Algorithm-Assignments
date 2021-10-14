import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() { // construct an empty set of points
        points = new SET<>();
    }

    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    public int size() { // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        checkNull(p);
        points.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        checkNull(p);
        return points.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D p : points)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        checkNull(rect);
        Bag<Point2D> ans = new Bag<>();
        for (Point2D p : points)
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax())
                ans.add(p);
        return ans;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        checkNull(p);
        double minDist = Double.POSITIVE_INFINITY;
        Point2D ans = null;
        for (Point2D q : points) {
            double currDist = p.distanceSquaredTo(q);
            if (currDist < minDist) {
                minDist = currDist;
                ans = q;
            }
        }
        return ans;
    }

    private void checkNull(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }
}