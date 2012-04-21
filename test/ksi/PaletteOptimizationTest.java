/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksi;

import java.awt.Image;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Syndarin
 */
public class PaletteOptimizationTest {
    
    public PaletteOptimizationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of SetFixedPalette method, of class PaletteOptimization.
     */
    @Test
    public void testSetFixedPalette() {
        System.out.println("SetFixedPalette");
        Image source = null;
        Image expResult = null;
        Image result = PaletteOptimization.SetFixedPalette(source);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SetOptimizedPalette method, of class PaletteOptimization.
     */
    @Test
    public void testSetOptimizedPalette() {
        System.out.println("SetOptimizedPalette");
        Image source = null;
        Image expResult = null;
        Image result = PaletteOptimization.SetOptimizedPalette(source);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of MakeDithering method, of class PaletteOptimization.
     */
    @Test
    public void testMakeDithering() {
        System.out.println("MakeDithering");
        Image original = null;
        Image optimized = null;
        Image expResult = null;
        Image result = PaletteOptimization.MakeDithering(original, optimized);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
