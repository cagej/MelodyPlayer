package DataStructures;

import Exceptions.EmptyCollectionException;
import ADTs.StackADT;

public class ArrayStack<E> implements StackADT<E> {
    private final static int DEFAULT_CAPACITY = 10;  
    private E[] data;
    private int top;

    @SuppressWarnings("unchecked")
    public ArrayStack() {
        data = (E[]) new Object[DEFAULT_CAPACITY];

    }

    @Override
    public void push(E target) {
        if (isFull()) {
            expandCapacity();
        }
        data[top] = target;
        top++;
        
    }

    @SuppressWarnings("unchecked")
    protected void expandCapacity() {
        E[] newData = (E[])(new Object[data.length * 2]); // Warning
        for (int i = 0; i < data.length; i++)
            newData[i] = data[i];

        data = newData;
    }
    
    @Override
    public E pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        top--;
        E result = data[top];
        data[top] = null;
        return result;
    }
    
    @Override
    public E peek() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException();
        return data[top -1];

    }

    @Override
    public boolean isEmpty() {
        return top == 0;    
    }

    protected boolean isFull() {
        return top == data.length;        
    }
    
    @Override
    public int size() {
        return top;
    }
}

