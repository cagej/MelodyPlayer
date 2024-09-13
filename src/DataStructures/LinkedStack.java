package DataStructures;

import Exceptions.EmptyCollectionException;
import ADTs.StackADT;

public class LinkedStack<T> implements StackADT<T> {
    private SinglyLinkedNode<T> top;
    private int size;
    
    public LinkedStack() {
        size = 0;
        top = null;      
    }
    
    @Override
    public void push(T target) {
        SinglyLinkedNode<T> temp = new SinglyLinkedNode<T>(target);
        temp.setNext(top);
        top = temp;
        size++;
    }

    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException(
                    "Pop off empty stack");
        }
        T result = top.getElement();
        top = top.getNext();
        size--;
   
        return result;   
    }
    
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException(
                    "Peep on the top of empty stack");
        }
        return top.getElement();    
    }
    
    @Override
    public boolean isEmpty() {
        if(size == 0)
            return true;
         else 
            return false;        
    }
    
    @Override
    public int size() { 
        return size;
    }
}

