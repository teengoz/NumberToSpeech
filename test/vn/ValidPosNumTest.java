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
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Tee
 */
@RunWith(Parameterized.class)
public class ValidPosNumTest{
    private String inpVal;
    private Boolean expVal;
    
    public ValidPosNumTest(String inpVal, String expVal) {
        this.inpVal = inpVal;
        this.expVal = Boolean.valueOf(expVal);
    }
    
    @Parameters(name = "Case#{index}: {0}")
    public static Collection data() throws IOException {
        URL url = ValidPosNumTest.class.getResource("/testdata/ValidPosNum.tc");
        CSVReader csvReader = new CSVReader(url.getPath());
        return csvReader.getTestData();
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("- Chuc nang kiem tra chuoi so hop le");
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
    
    @Test
    public void testValidPosNum() {
        String source = this.inpVal;
        Boolean expResult = this.expVal;
        Boolean result = NumToSpeech.validPosNum(source);
        assertEquals(expResult, result);
    }
    
}
