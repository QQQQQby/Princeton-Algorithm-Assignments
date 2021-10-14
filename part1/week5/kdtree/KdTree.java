import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
    private TreeNode root;
    private int n;

    private static class TreeNode {
        private final Point2D point;
        private TreeNode left, right;
        private final RectHV rect;
        private final boolean isSeparatedByX;

        TreeNode(Point2D p, TreeNode left, TreeNode right, RectHV rect, boolean isSeparatedByX) {
            this.point = p;
            this.left = left;
            this.right = right;
            this.rect = rect;
            this.isSeparatedByX = isSeparatedByX;
        }
    }

    public KdTree() { // construct an empty set of points
        root = null;
        n = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return n == 0;
    }

    public int size() { // number of points in the set
        return n;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        checkNull(p);
        if (isEmpty()) {
            root = new TreeNode(p, null, null,
                    new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), true);
            n = 1;
            return;
        }
        TreeNode currNode = root, pre;
        do {
            if (currNode.point.equals(p))
                return;
            pre = currNode;
            if ((currNode.isSeparatedByX && p.x() <= currNode.point.x()) ||
                    (!currNode.isSeparatedByX && p.y() <= currNode.point.y()))
                currNode = currNode.left;
            else
                currNode = currNode.right;

        } while (currNode != null);
        if (pre.isSeparatedByX) {
            if (p.x() <= pre.point.x())
                pre.left = new TreeNode(p, null, null,
                        new RectHV(pre.rect.xmin(), pre.rect.ymin(), pre.point.x(), pre.rect.ymax()), false);
            else
                pre.right = new TreeNode(p, null, null,
                        new RectHV(pre.point.x(), pre.rect.ymin(), pre.rect.xmax(), pre.rect.ymax()), false);
        } else {
            if (p.y() <= pre.point.y())
                pre.left = new TreeNode(p, null, null,
                        new RectHV(pre.rect.xmin(), pre.rect.ymin(), pre.rect.xmax(), pre.point.y()), true);
            else
                pre.right = new TreeNode(p, null, null,
                        new RectHV(pre.rect.xmin(), pre.point.y(), pre.rect.xmax(), pre.rect.ymax()), true);
        }
        ++n;
    }

    private boolean contains(TreeNode currNode, Point2D p) {
        if (currNode == null)
            return false;
        if (p.equals(currNode.point))
            return true;
        if ((currNode.isSeparatedByX && p.x() <= currNode.point.x()) ||
                (!currNode.isSeparatedByX && p.y() <= currNode.point.y()))
            return contains(currNode.left, p);
        else
            return contains(currNode.right, p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        checkNull(p);
        return contains(root, p);
    }

    public void draw() { // draw all points to standard draw
        if (isEmpty())
            return;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode currNode = stack.pop();
            currNode.point.draw();
            if (currNode.left != null)
                stack.push(currNode.left);
            if (currNode.right != null)
                stack.push(currNode.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        checkNull(rect);
        Stack<Point2D> ans = new Stack<>();
        if (isEmpty())
            return ans;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode currNode = stack.pop();
            if (rect.contains(currNode.point))
                ans.push(currNode.point);
            if (currNode.left != null && rect.intersects(currNode.left.rect))
                stack.push(currNode.left);
            if (currNode.right != null && rect.intersects(currNode.right.rect))
                stack.push(currNode.right);
        }
        return ans;
    }

    private TreeNode nearest(TreeNode currNode, TreeNode nearestNode, Point2D p) {
        if (currNode == null)
            return nearestNode;

        if (p.compareTo(currNode.point) == 0)
            return currNode;

        if (p.distanceSquaredTo(currNode.point) < p.distanceSquaredTo(nearestNode.point))
            nearestNode = currNode;

        if ((currNode.isSeparatedByX && p.x() <= currNode.point.x()) ||
                (!currNode.isSeparatedByX && p.y() <= currNode.point.y())) {
            nearestNode = nearest(currNode.left, nearestNode, p);
            if (currNode.right != null &&
                    currNode.right.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point))
                nearestNode = nearest(currNode.right, nearestNode, p);
        } else {
            nearestNode = nearest(currNode.right, nearestNode, p);
            if (currNode.left != null &&
                    currNode.left.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point))
                nearestNode = nearest(currNode.left, nearestNode, p);
        }
        return nearestNode;
    }


    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        checkNull(p);
        if (isEmpty())
            return null;
        return nearest(root, root, p).point;
    }

    private void checkNull(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }


    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        System.out.println(tree.nearest(new Point2D(0.12, 0.74)));
    }
}
