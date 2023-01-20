/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: Nov 19
 *  Description: content-aware image resizing
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private static final int E_BORDER = 1000;
    private int[][] picture;
    private int height;
    private int width;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        height = picture.height();
        width = picture.width();
        this.picture = new int[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                this.picture[y][x] = picture.getRGB(x, y);
    }

    // current picture
    public Picture picture() {
        Picture carvedPicture = new Picture(width, height);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                carvedPicture.setRGB(x, y, picture[y][x]);
        return carvedPicture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) return E_BORDER;

        int x1 = picture[y][x + 1];
        int x2 = picture[y][x - 1];
        int y1 = picture[y + 1][x];
        int y2 = picture[y - 1][x];
        int rdx = ((x1 >> 16) & 0xFF) - ((x2 >> 16) & 0xFF);
        int gdx = ((x1 >> 8) & 0xFF) - ((x2 >> 8) & 0xFF);
        int bdx = (x1 & 0xFF) - (x2 & 0xFF);
        int rdy = ((y1 >> 16) & 0xFF) - ((y2 >> 16) & 0xFF);
        int gdy = ((y1 >> 8) & 0xFF) - ((y2 >> 8) & 0xFF);
        int bdy = (y1 & 0xFF) - (y2 & 0xFF);

        int deltaXSquared = rdx * rdx + gdx * gdx + bdx * bdx;
        int deltaYSquared = rdy * rdy + gdy * gdy + bdy * bdy;

        return Math.sqrt(deltaXSquared + deltaYSquared);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (width <= 1) throw new IllegalArgumentException(
                "one or more picture dimension is <= 1");

        final double[][] e;     // energy of vertices
        double[][] distTo;      // distance of shortest path from source
        int[][] vertexTo;       // last vertex on shortest path

        e = new double[height][width];
        distTo = new double[height][width];
        vertexTo = new int[height][width];

        // NOTE: the coordinate system used for picture is the transpose of how
        // array is stored in Java
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                e[y][x] = energy(x, y);
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        // initializing array entries to appropriate value
        for (int x = 0; x < width; x++) {
            distTo[0][x] = E_BORDER;
            vertexTo[0][x] = x;
        }

        return findSP(e, distTo, vertexTo);
    }

    private int[] findSP(double[][] e, double[][] distTo, int[][] vertexTo) {
        // compute shortest path from all vertices in the first row
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                for (int vx : reachableX(x)) {
                    // relax neighboring vertices in y+1 row\
                    if (distTo[y + 1][vx] > distTo[y][x] + e[y + 1][vx]) {
                        distTo[y + 1][vx] = distTo[y][x] + e[y + 1][vx];
                        vertexTo[y + 1][vx] = x;
                    }
                }
            }
        }

        // get the index of minimum distTo in the last row of distTo[][]
        int minDistIndex = getMinIndex(distTo[height - 1]);
        Stack<Integer> sp = new Stack<Integer>();
        int x, y;
        sp.push(minDistIndex);
        for (x = minDistIndex, y = height - 1; y > 0; y--) {
            sp.push(vertexTo[y][x]);
            x = vertexTo[y][x];
        }

        int[] path = new int[sp.size()];
        for (int i = 0; i < path.length; i++)
            path[i] = sp.pop();
        return path;
    }

    // returns the index of the array entry where distTo is the smallest
    private int getMinIndex(double[] arr) {
        int minIndex = 0;
        for (int i = 0; i < arr.length; i++)
            if (arr[i] < arr[minIndex]) minIndex = i;
        return minIndex;
    }

    // return reachable x coordinates of pixels in y+1 row from pixel (x, y)
    private int[] reachableX(int x) {
        int[] xs;
        if (x == 0) {
            xs = new int[2];
            xs[0] = x + 1;
        }
        else if (x == width - 1) {
            xs = new int[2];
            xs[0] = x - 1;
            xs[1] = x;
        }
        else {
            xs = new int[3];
            xs[0] = x - 1;
            xs[1] = x;
            xs[2] = x + 1;
        }
        return xs;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
        int[] sp = findVerticalSeam();
        transposePicture();
        return sp;
    }

    // transpose image p
    private void transposePicture() {
        int[][] pT = new int[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                pT[x][y] = picture[y][x];

        height = pT.length;
        width = pT[0].length;
        picture = pT;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width <= 1) throw new IllegalArgumentException(
                "cannot remove vertical seam when picture width <= 1");
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height)
            throw new IllegalArgumentException("seam length does not match picture dimension");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width)
                throw new IllegalArgumentException("seam entry is outside its prescribed range");
            if (seam[i] - seam[i - 1] > 1)
                throw new IllegalArgumentException("two entries in seam differ by more than 1");
        }

        for (int y = 0; y < height; y++) {
            if (seam[y] != width - 1) {
                for (int x = seam[y]; x < width - 1; x++)
                    picture[y][x] = picture[y][x + 1];
            }
            picture[y][width - 1] = Integer.MIN_VALUE;
        }
        width--;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height <= 1) throw new IllegalArgumentException(
                "cannot remove horizontal seam when picture width <= 1");
        if (seam == null) throw new IllegalArgumentException();

        transposePicture();
        int[] sp = findVerticalSeam();
        removeVerticalSeam(sp);
        transposePicture();
    }

    public static void main(String[] args) {
        Picture p = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(p);

        int[] seam = { 3, 3, 3, 4, 3, 4 };
        sc.removeHorizontalSeam(seam);

        for (int y = 0; y < sc.height; y++)
            for (int x = 0; x < sc.width; x++)
                StdOut.println();
    }
}
