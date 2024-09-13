package Exceptions;

public class EmptyCollectionException extends Exception {

    public EmptyCollectionException() {
        System.out.println("Structure is empty.");
    }  
    
    public EmptyCollectionException(String textMsg) {
        System.out.println(textMsg);
    } 
}
