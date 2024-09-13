package DataStructures;

import Exceptions.EmptyCollectionException;
import ADTs.QueueADT;

/* Array-based queue */
public class ArrayCircularQueue<E> implements QueueADT<E> {
    private final static int DEFAULT_CAPACITY = 10;
    private E[] data;

    /* Int variables representing front and back-most element in queue */
    private int front, rear;

    /* Number of items currently in the queue */
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayCircularQueue() {
        rear = front = 0;
        size = 0;
        data = (E[]) (new Object[13]);
    }

    @Override
    public void enqueue(E target) {
        /** If queue is full, expand capacity */
        if (isFull()) {
            expandCapacity();
        }
        
        /* Queue input target */
        data[rear] = target;
        rear = (rear + 1)% data.length;
        size++;
    }

    /**
     * Remove and return element from the beginning of the queue
     * @throws EmptyCollectionException 
     */
    @Override
    public E dequeue() throws EmptyCollectionException {
        /** If queue is empty, throw an exception **/
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        
        /**
        * Removes data item from the queue, which 
        * corresponds to save element at the front index 
        * to a variable, named result
        **/
        E result = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return result;
    }

    /** Double the length of data */
    @SuppressWarnings("unchecked")
    protected void expandCapacity() {
        E[] newData = (E[])(new Object[data.length * 2]); // Warning
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
    }

    /**
     * Examine whether the queue is empty
     * @return true if the queue is empty
     *         false if the queue is not empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;

    }

    /**
    * Examine whether the queue array is full
    * @return Return true if data is full, 
    *         or else false
    */
    protected boolean isFull() {
        return size == data.length;

    }
    
    /**
     * Retrieve the first
     * @return the element in the beginning of the queue
     * @throws EmptyCollectionException 
     */
    @Override
    public E first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException(
                    "Remove item from empty queue");
        }
        return data[front];
    }

    /* Return size of array */
    @Override
    public int size() {
        // Return the size of the queue
        return size;
    }

    @Override
    public void add(E target) {
        this.enqueue(target);
    }

    @Override
    public E poll() {
        if (isEmpty()) return null;
        E result = data[front];
        front = (front + 1) % data.length;
        size--;
        return result;    }
}