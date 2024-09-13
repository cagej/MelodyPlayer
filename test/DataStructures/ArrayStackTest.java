package DataStructures;

import Exceptions.EmptyCollectionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayStackTest {
    
    public ArrayStackTest() {
    }

    /**
     * Test of isEmpty method
     */
    @Test
    public void testIsEmpty() {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        assertTrue(stack.isEmpty());
        stack.push(6);
        assertFalse(stack.isEmpty());
    }

    /**
     * Test of peek method
     */
    @Test(expected=EmptyCollectionException.class)
    public void testPeek1() throws Exception {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.peek();
    }

    /**
     * Test of peek method
     */
    @Test
    public void testPeek2() throws Exception {
        //TODO peek on the top of an non-empty stack
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.push(4);
        assertEquals(stack.peek().intValue(), 4);
        stack.push(6);
        assertEquals(stack.peek().intValue(), 6);
        stack.pop();
        assertEquals(stack.peek().intValue(), 4);
    }
    /**
     * Test of pop method
     */
    @Test(expected=EmptyCollectionException.class)
    public void testPop1() throws Exception {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.pop();
    }
    /**
     * Test of pop method
     */
    @Test
    public void testDequeue2() throws Exception {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.push(4);
        stack.push(6);
        assertEquals(stack.size(), 2);
        try{
            assertEquals(stack.pop().intValue(), 6);
            assertEquals(stack.peek().intValue(), 4);
            assertEquals(stack.pop().intValue(), 4);
            assertEquals(stack.size(), 0);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Test of push method
     */
    @Test
    public void testPush() {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.push(4);
        assertEquals(stack.size(), 1);
        try{
            assertEquals(stack.peek().intValue(), 4);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        stack.push(6);
        assertEquals(stack.size(), 2);
        try{
            assertEquals(stack.peek().intValue(), 6);
            stack.pop();
            assertEquals(stack.peek().intValue(), 4);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Test of size method
     */
    @Test
    public void testSize() {
        ArrayStack<Integer> stack = new ArrayStack<Integer>();
        stack.push(4);
        assertEquals(stack.size(), 1);
        try{
            stack.pop();
            assertEquals(stack.size(), 0);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    } 
}
