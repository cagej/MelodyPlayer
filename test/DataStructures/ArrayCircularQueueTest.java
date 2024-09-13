package DataStructures;

import Exceptions.EmptyCollectionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayCircularQueueTest {
    
    public ArrayCircularQueueTest() {
    }

    /**
     * Test of enqueue method
     */
    @Test
    public void testEnqueue() {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.enqueue(4);
        assertEquals(queue.size(), 1);
        try{
            assertEquals(queue.first().intValue(), 4);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        queue.enqueue(6);
        assertEquals(queue.size(), 2);
        try{
            assertEquals(queue.first().intValue(), 4);
            queue.dequeue();
            assertEquals(queue.first().intValue(), 6);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Test of isEmpty method
     */
    @Test
    public void testIsEmpty() {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        assertTrue(queue.isEmpty());
        queue.enqueue(6);
        assertFalse(queue.isEmpty());
    }
    
    /**
     * Test of dequeue method
     */
    @Test(expected=EmptyCollectionException.class)
    public void testDequeue1() throws Exception {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.dequeue();
    }
    /**
     * Test of dequeue method
     */
    @Test
    public void testDequeue2() throws Exception {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.enqueue(4);
        queue.enqueue(6);
        assertEquals(queue.size(), 2);
        try{
            assertEquals(queue.dequeue().intValue(), 4);
            assertEquals(queue.first().intValue(), 6);
            assertEquals(queue.dequeue().intValue(), 6);
            assertEquals(queue.size(), 0);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Test of first method
     */
    @Test(expected=EmptyCollectionException.class)
    public void testFirst1() throws Exception {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.first();
    }
        
    /**
     * Test of first method
     */
    @Test
    public void testFirst2() throws Exception {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.enqueue(4);
        assertEquals(queue.first().intValue(), 4);
        queue.enqueue(6);
        assertEquals(queue.first().intValue(), 4);
        queue.dequeue();
        assertEquals(queue.first().intValue(), 6);
    }

    /**
     * Test of size method
     */
    @Test
    public void testSize() {
        ArrayCircularQueue<Integer> queue = new ArrayCircularQueue<Integer>();
        queue.enqueue(4);
        assertEquals(queue.size(), 1);
        try{
            queue.dequeue();
            assertEquals(queue.size(), 0);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}