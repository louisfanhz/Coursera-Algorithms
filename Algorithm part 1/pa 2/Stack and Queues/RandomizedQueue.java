import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int n;
    private int first;
    private int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[(first + i) % q.length];
        }
        q = copy;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot enqueue null");
        if (n == q.length) resize(2 * q.length);
        q[last++] = item;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randNum = StdRandom.uniform(n);

        Item item = q[randNum];
        q[randNum] = q[--last];
        q[last] = null;
        n--;
        if (first == q.length) first = 0;
        if (n > 0 && n == q.length / 4) resize(q.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randNum = StdRandom.uniform(n);
        Item item = q[randNum];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int last = n;
        private int copyN = n;
        private Item[] copy;

        public RandomizedQueueIterator() {
            copy = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                copy[i] = q[(first + i) % q.length];
            }
        }

        public boolean hasNext() {
            return copyN != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int randNum = StdRandom.uniform(copyN);

            Item item = copy[randNum];
            copy[randNum] = copy[--last];
            copy[last] = null;
            copyN--;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (true) {
            String item = StdIn.readString();
            if (item.equals(".")) break;
            queue.enqueue(item);
        }
        StdOut.println("the following items are enqueued: ");
        for (String s : queue) {
            StdOut.print(s + " ");
        }
        StdOut.println("\nhow many items do you want to deque?");
        int n = Integer.parseInt(StdIn.readString());

        StdOut.println("elements removed: ");
        for (int i = 0; i < n; i++) {
            StdOut.print(queue.dequeue() + " ");
        }
        if (!queue.isEmpty()) {
            StdOut.println("(" + queue.size() + " left on queue)");
            StdOut.println("sampling the elements left on queue");
            for (int i = 0; i < queue.size(); i++) {
                StdOut.print(queue.sample() + " ");
            }
            StdOut.print("\n");
        }
        else {
            StdOut.println("(queue is emptied)");
        }
    }
}
