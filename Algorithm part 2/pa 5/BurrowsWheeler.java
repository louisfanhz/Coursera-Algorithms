/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder s = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readChar());
        }

        CircularSuffixArray csa = new CircularSuffixArray(s.toString());

        int length = csa.length();
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt((length-1 + csa.index(i)) % length);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        StringBuilder s = new StringBuilder();
        int first = BinaryStdIn.readInt();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readChar());
        }

        final int R = 256;
        final int N = s.length();
        int[] count = new int[R+1];
        int[] next = new int[N];

        // use index-counting to allocate next[]
        for (int i = 0; i < N; i++) {
            count[s.charAt(i)+1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r+1] += count[r];
        }
        for (int i = 0; i < N; i++) {
            next[count[s.charAt(i)]++] = i;
        }

        char[] sT = new char[N];
        sT[N-1] = s.charAt(first);
        for (int i = 0; i < N - 1; i++) {
            first = next[first];
            sT[i] = s.charAt(first);
        }
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(sT[i]);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
