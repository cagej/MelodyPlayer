package ADTs;

import Exceptions.EmptyCollectionException;

/** FIFO queue */
public interface QueueADT<E> {

    /** Add target to the back of queue */
    public void enqueue(E target);
    
    public void add(E target);
    
    /** Return true if queue is empty */
    public boolean isEmpty();

    /**
     * Remove and return the front item from the queue
     * @throws EmptyStructureException if the queue is empty
     */
    public E dequeue() throws EmptyCollectionException;
    
    public E poll();
    /**
    * Returns (without removing) the element that is in the head of the queue
    * @return the element in the head of the queue
    * @throws EmptyCollectionException 
    */
    public E first() throws EmptyCollectionException;

    /** Number of elements in stack */
    public int size();
}
