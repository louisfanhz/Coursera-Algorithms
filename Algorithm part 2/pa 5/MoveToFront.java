/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        final int R = 256;
        int[] ascii = new int[R];
        for (int i = 0; i < R; i++) {
            ascii[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i < R; i++) {
                if (ascii[i] == c) {
                    BinaryStdOut.write(i, 8);
                    for (int j = i; j > 0; j--) {
                        int b = ascii[j-1];
                        ascii[j-1] = ascii[j];
                        ascii[j] = b;
                    }
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        final int R = 256;
        int[] ascii = new int[R];
        for (int i = 0; i < R; i++) {
            ascii[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar();
            BinaryStdOut.write(ascii[c], 8);
            for (int i = c; i > 0; i--) {
                int b = ascii[i-1];
                ascii[i-1] = ascii[i];
                ascii[i] = b;
            }
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
