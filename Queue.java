import java.util.LinkedList;
import java.util.List;

/**
 * A custom generic Queue implementation.
 * @param <E> the type of elements held in this Queue
 */
public class Queue<E> {
    private LinkedList<E> list;

    /**
     * Constructor to initialize the queue.
     */
    public Queue() {
        list = new LinkedList<>();
    }

    /**
     * Adds an element to the end of the queue.
     * @param element the element to be added
     */
    public void enqueue(E element) {
        list.add(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     * @return the element at the front of the queue, or null if the queue is empty
     */
    public E dequeue() {
        return list.isEmpty() ? null : list.removeFirst();
    }

    /**
     * Returns the element at the front of the queue without removing it.
     * @return the element at the front of the queue, or null if the queue is empty
     */
    public E peek() {
        return list.isEmpty() ? null : list.getFirst();
    }

    /**
     * Returns the number of elements in the queue.
     * @return the size of the queue
     */
    public int size() {
        return list.size();
    }

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Adds all elements from a given list to the queue.
     * @param elements the list of elements to be added
     */
    public void addAll(List<E> elements) {
        list.addAll(elements);
    }

    /**
     * Returns a list containing all elements of the queue.
     * @return a List containing all elements in the queue
     */
    public List<E> toList() {
        return new LinkedList<>(list);
    }
}
