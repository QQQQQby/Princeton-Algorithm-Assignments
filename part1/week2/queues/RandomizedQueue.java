import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int n;
    private Object[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        validateItem(item);
        if (n == items.length)
            changeCapacity(2 * n);
        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        checkEmpty();
        int idx = randomIndex();
        Item item = (Item) items[idx];
        items[idx] = items[--n];
        items[n] = null;
        if (items.length >= 4 && n == items.length / 4)
            changeCapacity(n * 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkEmpty();
        return (Item) items[randomIndex()];
    }


    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        int[] indexes;
        int pointer;

        RandomizedQueueIterator() {
            indexes = new int[n];
            for (int i = 0; i < n; ++i)
                indexes[i] = i;
            // Shuffle
            for (int i = 0; i < n; ++i) {
                int j = StdRandom.uniform(i, n);
                int temp = indexes[i];
                indexes[i] = indexes[j];
                indexes[j] = temp;
            }
            pointer = 0;
        }

        @Override
        public boolean hasNext() {
            return pointer < n;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (Item) items[indexes[pointer++]];
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

    private int randomIndex() {
        return StdRandom.uniform(0, n);
    }

    private void changeCapacity(int capacity) {
        Object[] newItems = new Object[capacity];
        for (int i = 0; i < n; ++i)
            newItems[i] = items[i];
        items = newItems;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 5; ++i) {
            queue.enqueue(i);
        }
        StdOut.println(queue.dequeue());
        for (int num : queue) {
            StdOut.println(num);
        }
    }
}
