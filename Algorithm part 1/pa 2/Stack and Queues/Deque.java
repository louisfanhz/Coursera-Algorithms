import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;       // pointer to the first node
    private Node<Item> last;        // pointer to the last node
    private int n;                  // total number of elements in deque

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (n == 0);
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null item");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        n++;
        if (oldfirst == null) {
            last = first;
        }
        else {
            oldfirst.previous = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("cannot add null item");
        if (isEmpty()) {
            addFirst(item);
        }
        else {
            Node<Item> oldlast = last;
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            last.previous = oldlast;
            oldlast.next = last;
            n++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque underflow");
        Item item = first.item;
        first = first.next;
        if (first != null) first.previous = null;
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque underflow");
        Item item = last.item;
        last = last.previous;
        if (last != null) last.next = null;
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    // an iterator which doesn't implement remove()
    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        StdOut.println(
                "enter > to add at first, < to add at last, [ to remove at first, ] to remove at last, and . to stop adding/removing");
        while (true) {
            String item = StdIn.readString();
            if (item.equals(".")) {
                StdOut.println("current deque:");
                for (String s : deque) {
                    StdOut.print(" " + s + " ");
                }
                break;
            }
            if (item.equals("[")) {
                String removed = deque.removeFirst();
                StdOut.println("removing first element: " + removed);
            }
            else if (item.equals("]")) {
                String removed = deque.removeLast();
                StdOut.println("removing last element: " + removed);
            }
            else if (item.equals(">")) {
                StdOut.println("adding at first");
                while (true) {
                    item = StdIn.readString();
                    if (item.equals(".")) {
                        StdOut.println("current deque:");
                        for (String s : deque) {
                            StdOut.print(" " + s + " ");
                        }
                        break;
                    }
                    deque.addFirst(item);
                }
            }
            else if (item.equals("<")) {
                StdOut.println("adding at last");
                while (true) {
                    item = StdIn.readString();
                    if (item.equals(".")) {
                        StdOut.println("current deque:");
                        for (String s : deque) {
                            StdOut.print(" " + s + " ");
                        }
                        break;
                    }
                    deque.addLast(item);
                }
            }
        }
        if (!deque.isEmpty()) {
            StdOut.println("(" + deque.size() + " elements left on deque)");
        }
        else {
            StdOut.println("deque is emptied");
        }
    }
}
