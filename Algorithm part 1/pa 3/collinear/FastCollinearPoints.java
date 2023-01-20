/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: July 12 2020
 *  Description: Fast approach to find collinear points and return lines
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private Point[] pointsCopy;
    private double[] slopes;
    private int numOfSegments = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null points array");
        else {
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null)
                    throw new IllegalArgumentException("null point detected");
            }
            this.points = shallowCopy(points);
            Arrays.sort(this.points);
        }

        for (int i = 1; i < this.points.length; i++) {
            if (this.points[i - 1].compareTo(this.points[i]) == 0)
                throw new IllegalArgumentException("repeated points detected");
        }

        slopes = new double[points.length];
        pointsCopy = new Point[points.length];
    }

    private Point[] shallowCopy(Point[] pts) {
        Point[] copy = new Point[pts.length];
        for (int i = 0; i < pts.length; i++) {
            copy[i] = pts[i];
        }
        return copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[0];
        for (int i = 0; i < points.length; i++) {
            int count = 1;
            originalize(points[i]);
            for (int j = slopes.length - 2; j >= 0; j--) {
                if (Double.compare(slopes[j], slopes[j + 1]) == 0) {
                    count++;
                }
                else if (count < 3) count = 1;
                else {
                    Point[] collinearPoints = new Point[count + 1];
                    while (count > 0) {
                        collinearPoints[count] = pointsCopy[j + count];
                        count--;
                    }
                    collinearPoints[0] = pointsCopy[0];
                    lines = addLine(lines, collinearPoints);
                    count = 1;
                    numOfSegments++;
                }
            }
        }
        return lines;
    }

    private void originalize(Point p) {
        for (int i = 0; i < points.length; i++) {
            slopes[i] = p.slopeTo(points[i]);
            pointsCopy[i] = points[i];
        }
        Arrays.sort(slopes);
        Arrays.sort(pointsCopy, p.slopeOrder());
    }

    private LineSegment[] addLine(LineSegment[] lines, Point[] collinearPoints) {
        Point origin = collinearPoints[0];
        Arrays.sort(collinearPoints);

        if (!(collinearPoints[collinearPoints.length - 1].equals(origin))) {
            return lines;
        }

        LineSegment[] newLines = new LineSegment[lines.length + 1];
        LineSegment newLine = new LineSegment(collinearPoints[0],
                                              collinearPoints[collinearPoints.length - 1]);

        for (int i = 0; i < lines.length; i++) {
            newLines[i] = lines[i];
        }
        newLines[lines.length] = newLine;
        return newLines;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
