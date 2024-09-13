package ADTs;

import Exceptions.EmptyCollectionException;

/** LIFO stack */
public interface StackADT<E> {

    /** Return true if this stack is empty */
    public boolean isEmpty();

    /**
     * Return the top item on stack
     * @throws EmptyCollectionException if stack is empty
     */
    public E peek() throws EmptyCollectionException;

    /**
     * Remove and return the top item on this StackADT
     * @throws EmptyStructureException if the stack is empty
     */
    public E pop() throws EmptyCollectionException;

    /** Adds target to the top of the stack */
    public void push(E target);

    /** Number of elements of this StackADT. */
    public int size();
}
