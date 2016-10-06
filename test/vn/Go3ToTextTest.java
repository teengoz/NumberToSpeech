/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Tee
 */
@RunWith(Parameterized.class)
public class Go3ToTextTest {
    private String inpVal;
    private String expVal;
    
    public Go3ToTextTest(String inpVal, String expVal) {
        this.inpVal = inpVal;
        this.expVal = expVal;
    }
    
    @Parameterized.Parameters(name = "Case#{index}: {0} - {1}")
    public static Collection data() throws IOException {
        URL url = UnitsToStringTest.class.getResource("/testdata/Go3ToString.tc");
        TCReader tcReader = new TCReader(url.getPath());
        return tcReader.getTestData();
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("- Chuc nang chuyen so cho Nhom 3 chu so");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getText method, of class NumToSpeech.
     */
    @Test
    public void testUnitsToString() {
        String source = this.inpVal;
        String expResult = this.expVal;
        String result = NumToSpeech.go3ToText(source);
        assertEquals(expResult, result);
    }
    
}
