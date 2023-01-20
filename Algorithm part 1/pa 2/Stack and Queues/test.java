public class test {
    private int n = 10;

    private class inner {
        private int copyN = n;

        private void decrement() {
            copyN--;
            System.out.println(n + " " + copyN);
        }
    }

    public void call() {
        inner in = new inner();
        in.decrement();
    }

    public static void main(String[] args) {
        test T = new test();
        T.call();
    }
}
