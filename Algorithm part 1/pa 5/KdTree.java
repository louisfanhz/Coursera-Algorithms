/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Aug 13 2020
 *  Description: 2d Tree implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;    // root of the 2d-Tree
    private int n;        // total number of the node in the tree

    // helper node data type
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    /***************************************************************************
     * implementation of 2D-Tree
     **************************************************************************/
    // constructor, construct an empty tree
    public KdTree() {
        root = null;
        n = 0;
    }

    // return true if the set is empty
    public boolean isEmpty() {
        return n == 0;
    }

    // number of points in the set
    public int size() {
        return n;
    }

    /***************************************************************************
     * 2D-Tree insertion and search method
     **************************************************************************/
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, null, p, true, false);
    }

    private Node insert(Node h, Node parent, Point2D p, boolean compareX,
                        boolean isRtChild) {
        if (h == null) {
            n++;
            if (parent == null) return new Node(p, new RectHV(0, 0, 1, 1));
            return new Node(p, splitRect(parent, !compareX, isRtChild));
        }

        int cmp;
        if (compareX) cmp = Double.compare(p.x(), h.p.x());
        else cmp = Double.compare(p.y(), h.p.y());

        if (cmp < 0) h.lb = insert(h.lb, h, p, !compareX, false);
        else if (cmp > 0) h.rt = insert(h.rt, h, p, !compareX, true);
        else if (h.p.compareTo(p) == 0) return h;
        else h.rt = insert(h.rt, h, p, !compareX, true);

        return h;
    }

    // return true if the tree contains point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls contains() with a null point");
        return contains(root, p, true) != null;
    }

    private Point2D contains(Node h, Point2D p, boolean compareX) {
        if (h == null) return null;

        int cmp;
        if (compareX) cmp = Double.compare(p.x(), h.p.x());
        else cmp = Double.compare(p.y(), h.p.y());

        if (cmp < 0) return contains(h.lb, p, !compareX);
        else if (cmp > 0) return contains(h.rt, p, !compareX);
        else if (h.p.compareTo(p) == 0) return h.p;
        else return contains(h.rt, p, !compareX);
    }

    /***************************************************************************
     * 2D-Tree Helper Method
     **************************************************************************/
    // split the rectangle according to specification
    private RectHV splitRect(Node h, boolean compareX, boolean getRt) {
        double xmin = h.rect.xmin();
        double ymin = h.rect.ymin();
        double xmax = h.rect.xmax();
        double ymax = h.rect.ymax();

        if (getRt) {
            if (compareX) xmin = h.p.x();
            else ymin = h.p.y();
        }
        else {
            if (compareX) xmax = h.p.x();
            else ymax = h.p.y();
        }
        return new RectHV(xmin, ymin, xmax, ymax);
    }

    // helper method for finding nearest point
    private double getOriD(Node h, Point2D p, boolean compareX, boolean getRt) {
        RectHV rect = splitRect(h, compareX, getRt);
        return rect.distanceSquaredTo(p);
    }


    /***************************************************************************
     * 2D-Tree Draw Method
     **************************************************************************/
    // traverse tree and draw each node
    private void recursiveDraw(Node x, boolean compareX) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(x.p.x(), x.p.y(), 0.005);
        if (compareX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        recursiveDraw(x.lb, !compareX);
        recursiveDraw(x.rt, !compareX);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setScale(-0.05, 1.05);
        StdDraw.square(0.5, 0.5, 0.5);
        recursiveDraw(root, true);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points, true);

        return points;
    }

    private void range(Node h, RectHV rect, ArrayList<Point2D> points, boolean compareX) {
        if (h == null) return;
        if (rect.contains(h.p)) points.add(h.p);

        // make sub-tree rect
        RectHV lbRect = splitRect(h, compareX, false);
        RectHV rtRect = splitRect(h, compareX, true);

        if (rect.intersects(lbRect)) {
            range(h.lb, rect, points, !compareX);
        }
        if (rect.intersects(rtRect)) {
            range(h.rt, rect, points, !compareX);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        return nearest(root, p, root.p, true);
    }

    private Point2D nearest(Node h, Point2D p, Point2D champ, boolean compareX) {
        if (h == null) return champ;

        // update champ if a closer point is found
        if (h.p.distanceSquaredTo(p) < champ.distanceSquaredTo(p)) champ = h.p;

        double cmp = (compareX) ? Double.compare(h.p.x(), p.x()) : Double.compare(h.p.y(), p.y());
        if (cmp > 0) {
            champ = nearest(h.lb, p, champ, !compareX);
            if (Double.compare(getOriD(h, p, compareX, true), champ.distanceSquaredTo(p)) < 0)
                champ = nearest(h.rt, p, champ, !compareX);
        }
        else if (cmp < 0) {
            champ = nearest(h.rt, p, champ, !compareX);
            if (Double.compare(getOriD(h, p, compareX, false), champ.distanceSquaredTo(p)) < 0)
                champ = nearest(h.lb, p, champ, !compareX);
        }
        else {
            champ = nearest(h.lb, p, champ, !compareX);
            champ = nearest(h.rt, p, champ, !compareX);
        }
        return champ;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
