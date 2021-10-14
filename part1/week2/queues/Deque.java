import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private final Node front, rear;
    private int n;

    private class Node {
        Item item;
        Node next, pre;

        Node() {
            this(null, null, null);
        }

        Node(Item item, Node pre, Node next) {
            this.item = item;
            this.pre = pre;
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        front = new Node();
        rear = new Node(null, null, front);
        front.pre = rear;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);
        Node p = front.pre, newNode = new Node(item, p, front);
        front.pre = newNode;
        p.next = newNode;
        ++n;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateItem(item);
        Node q = rear.next, newNode = new Node(item, rear, q);
        rear.next = newNode;
        q.pre = newNode;
        ++n;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmpty();
        Node p = front.pre;
        p.pre.next = front;
        front.pre = p.pre;
        --n;
        return p.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkEmpty();
        Node q = rear.next;
        q.next.pre = rear;
        rear.next = q.next;
        --n;
        return q.item;
    }


    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node p;

        DequeIterator() {
            p = front.pre;
        }

        @Override
        public boolean hasNext() {
            return p != rear;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = p.item;
            p = p.pre;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void validateItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
    }

    private void checkEmpty() {
        if (isEmpty())
            throw new NoSuchElementException();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 5; ++i) {
            deque.addFirst(i);
        }
        deque.removeFirst();
        deque.addLast(-5);
        for (int num : deque) {
            StdOut.println(num);
        }
    }

}