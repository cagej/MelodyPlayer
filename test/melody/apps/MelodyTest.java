package melody.apps;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import melody.audio.Note;
import melody.apps.Melody;
import ADTs.*;
import DataStructures.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MelodyTest {
    Melody instance1; //instance 1 has no repeats
    double duration1;
    Melody instance2; //instance 2 has repeats
    double duration2;
    String currentDir;
    
    Random rand;
    public MelodyTest() {
        try{
            currentDir = System.getProperty("user.dir");
            System.out.println("current dir = " + currentDir);
            System.out.println("current class = " + this.getClass().toString());
            Path inputFPath = Paths.get("birthday.txt");
            System.out.println("[Test File Path] : " + inputFPath.toAbsolutePath());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    @Before
    public void setUp() {
        rand = new Random();
        String filename1 = currentDir + "/birthday.txt";
        File file1 = new File(filename1);
        String filename2 = currentDir + "/levels.txt";
        File file2 = new File(filename2);
        try{
            Scanner input1 = new Scanner(file1);
            QueueADT<Note> song1 = MelodyMain.read(input1);
            instance1 = new Melody(song1, "", "", song1.size());
            duration1 = instance1.getTotalDuration();
            
            Scanner input2 = new Scanner(file2);
            QueueADT<Note> song2 = MelodyMain.read(input2);
            instance2 = new Melody(song2, "", "", song2.size());
            duration2 = instance2.getTotalDuration();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Test of changeTempo method
     */
    @Test
    public void testChangeTempo() {
        System.out.println("changeTempo");
        double duration = instance1.getTotalDuration();
        double ratio = rand.nextDouble();
        instance1.changeTempo(ratio);
        assertTrue(duration * ratio - instance1.getTotalDuration() < 0.00001);
    }

    /**
     * Test of getTotalDuration method
     */
    @Test
    public void testGetTotalDuration() {
        System.out.println("getTotalDuration");
        double expResult = duration1;
        double result = instance1.getTotalDuration();
        assertTrue(expResult-result == 0);
        
        expResult = duration2;
        result = instance2.getTotalDuration();
        System.out.println("[testGetTotalDuration] " + expResult + " " + result);
        assertTrue(expResult-result == 0);        
    }

    /**
     * Test of octaveDown method
     */
    @Test
    public void testOctaveDown() {
        System.out.println("octaveDown");
        boolean expResult = true;
        boolean result = instance1.octaveDown();
        assertEquals(expResult, result);
        
        expResult = false;
        result = instance1.octaveDown();
        result = instance1.octaveDown();
        result = instance1.octaveDown();
        result = instance1.octaveDown();
        assertEquals(expResult, result);
    }

    /**
     * Test of octaveUp method
     */
    @Test
    public void testOctaveUp() {
        System.out.println("octaveUp");
        boolean expResult = true;
        boolean result = instance1.octaveUp();
        assertEquals(expResult, result);
        
        expResult = false;
        result = instance1.octaveUp();
        result = instance1.octaveUp();
        result = instance1.octaveUp();
        result = instance1.octaveUp();
        result = instance1.octaveUp();
        assertEquals(expResult, result);
    }

    /**
     * Test of play method
     */
    @Test
    public void testPlay() {
        System.out.println("play");
        double expResult = duration1;
        double result = instance1.getTotalDuration();
        assertTrue(expResult-result == 0);
    }

    /**
     * Test of append method
     */
    @Test
    public void testAppend() {
        System.out.println("append");
        double expResult = instance1.getTotalDuration();
        expResult += instance2.getTotalDuration();
        
        instance1.append(instance2);
        double result = instance1.getTotalDuration();
        assertTrue(expResult-result == 0);
    }

    /**
     * Test of reverse method
     */
    @Test
    public void testReverse() {
        System.out.println("reverse");
        double expResult = instance1.getTotalDuration();
        instance1.reverse();
        double result = instance1.getTotalDuration();
        assertTrue(expResult-result == 0);
    }
}
