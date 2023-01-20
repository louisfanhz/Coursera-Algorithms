/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: 2020/07/12
 *  Description: Exhaustively examines 4 points at a time and checks whether
 *  they all lie on the same line segment, returning all such line segments.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private int numSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        return numSegments;
    }

    public LineSegment[] segments() {
        Point[] collinearPoints = new Point[4];
        LineSegment[] lines = new LineSegment[0];
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double ilSlope = points[i].slopeTo(points[l]);
                        if (ilSlope == points[i].slopeTo(points[k])) {
                            if (ilSlope == points[i].slopeTo(points[j])) {
                                collinearPoints[0] = points[i];
                                collinearPoints[1] = points[j];
                                collinearPoints[2] = points[k];
                                collinearPoints[3] = points[l];

                                lines = addLine(lines, collinearPoints);
                                numSegments++;
                            }
                        }
                    }
                }
            }
        }
        return lines;
    }

    private LineSegment[] addLine(LineSegment[] lines, Point[] collinearPoints) {
        Point origin = collinearPoints[0];
        Arrays.sort(collinearPoints);


        if (!(collinearPoints[0].equals(origin))) {
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
