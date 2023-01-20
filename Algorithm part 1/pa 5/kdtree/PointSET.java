/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> points;
    private int n;

    public PointSET() {
        points = new SET<Point2D>();
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!points.contains(p)) {
            n++;
            points.add(p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // no need to implement draw method
    public void draw() {
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> pointsList = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) pointsList.add(point);
        }
        return pointsList;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (points.isEmpty()) return null;
        Point2D champ = points.max();
        for (Point2D q : points) {
            if (q.distanceSquaredTo(p) < champ.distanceSquaredTo(p)) champ = q;
        }
        return champ;
    }

    // main method for unit testing
    public static void main(String[] args) {

    }
}
