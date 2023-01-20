/* *****************************************************************************
 *  Name: Haozhi Fan
 *  Date: 01/09/2021
 *  Description: Create and sort circular suffix array for string s
 **************************************************************************** */

public class CircularSuffixArray {
    private String s;
    private int[] index;

    private class CircularSuffix {
        private String str;
        private int firstChar;
        private int length;

        public CircularSuffix(String str, int firstChar) {
            this.str = str;
            this.firstChar = firstChar;
            length = str.length();
        }

        public char charAt(int i) { return str.charAt((i + firstChar) % length); }

        public int getFirstChar() { return firstChar; }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Argument to constructor is null");

        this.s = s;
        index = new int[s.length()];
        CircularSuffix[] csArr = new CircularSuffix[s.length()];

        for (int i = 0; i < s.length(); i++) {
            csArr[i] = new CircularSuffix(s, i);
        }

        sort(csArr, 0, csArr.length - 1, 0);

        for (int i = 0; i < csArr.length; i++) {
            index[i] = csArr[i].getFirstChar();
        }

        /*
        // FIXME: for debugging
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                StdOut.print(csArr[i].charAt(j));
            }
            StdOut.print("  " + index[i]);
            StdOut.println();
        }

         */
    }

    private static void sort(CircularSuffix[] a, int lo, int hi, int d) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        int v = a[lo].charAt(d);
        int i = lo + 1;
        while (i <= gt) {
            int t = a[i].charAt(d);
            if      (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else            i++;
        }
        sort(a, lo, lt-1, d);
        sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    private static void exch(CircularSuffix[] a, int p, int q) {
        CircularSuffix k = a[p];
        a[p] = a[q];
        a[q] = k;
    }

    // length of s
    public int length() { return s.length(); }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length)
            throw new IllegalArgumentException("argument to index is out of range");
        return index[i];
    }

    // unit testing
    public static void main(String[] args) {

    }
}
