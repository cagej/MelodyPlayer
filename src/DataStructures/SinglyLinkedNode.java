package DataStructures;

/**
 * Represents a node in a linked list.
 */
public class SinglyLinkedNode<T> {
    private SinglyLinkedNode<T> next;
    private T element;
 
    public SinglyLinkedNode() {
        next = null;
        element = null;
    }
 
    /**
     * Creates a node storing specified element
     * @param elem element to be stored
     */
    public SinglyLinkedNode(T elem) {
        next = null;
        element = elem;
        
    }
 
    /**
     * Returns the node that follows this one
     * @return reference to next node
     */
    public SinglyLinkedNode<T> getNext() {
        return next;
    }
 
    /**
     * Sets the node that follows this one
     * @param node node to follow this one
     */
    public void setNext(SinglyLinkedNode<T> node) {
        next = node;

    }
 
    /**
     * Returns the element stored in this node
     * @return element stored at the node
     */
    public T getElement() {
        return element;
    }
 
    /**
     * Sets the element stored in this node
     * @param elem element to be stored at this node
     */
    public void setElement(T elem) {
        //TODO Reset the data/information member variable 
        //to the given object
        element = elem;

    }
    
    /* Print node data */
    public void printNodeData() {
        System.out.println(this.element.toString());
    }
}
