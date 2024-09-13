package DataStructures;

import Exceptions.EmptyCollectionException;
import ADTs.QueueADT;

/**
 * Linked list implementation of queues
 */
public class LinkedQueue<T> implements QueueADT<T> {
    /* front = beginning of the queue */
    private SinglyLinkedNode<T> front;
    
    /* rear = end of the queue */
    private SinglyLinkedNode<T> rear;
    
    /* size = number of elements in the queue */
    private int size;
    
    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;    
    }
    
    /**
     * Insert an element in the end of the queue
     * @param target input element
     */
    @Override
    public void enqueue(T target) {
        SinglyLinkedNode<T> newNode = new SinglyLinkedNode<>(target);
        
        if(isEmpty()) {
            front = newNode;
        } else {
            rear.setNext(newNode);
        }
        rear = newNode;
        size++;
    }
    
    /**
     * Remove from the beginning of the queue
     * @return the removed element
     * @throws EmptyCollectionException 
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException(
                    "Remove item from empty queue");
        }
        
        T result = front.getElement();
        front = front.getNext();
        size--;
        
        if(isEmpty()) {
            rear = null;
        }
        return result;
    }
    
    /**
     * Check whether queue is empty
     * @return true if the queue is empty
     *         false if the queue is not empty
     */
    @Override
    public boolean isEmpty() {
        if(size == 0)
            return true;
        else
            return false;
    }
    
    /**
     * Retrieve the front
     * @return the element in the beginning of the queue
     * @throws EmptyCollectionException 
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException(
                    "Remove item from empty queue");
        }
        return front.getElement();
    }
    
    /**
     * Retrieve the size
     * @return number of elements in the queue
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(T target) {
        this.enqueue(target);
    }

    @Override
    public T poll() {
        if (isEmpty()) return null;     
        SinglyLinkedNode<T> frontNode = front;
        front = front.getNext();     
        size--;
        
        if (front == null) {
            rear = front;
        }  
        return frontNode.getElement();
    }
}

