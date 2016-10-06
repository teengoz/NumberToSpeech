/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Tee
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ValidPosNumTest.class,
    UnitsToStringTest.class,
    TensToStringTest.class,
    HundredsToStringTest.class,
    Go3ToTextTest.class,
    GetTextTest.class
})
public class NumToText_AllTests{
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
//    public static TestSuite suite() {
//        TestSuite suite = new TestSuite("Test for Money");
//        suite.addTest(new TestSuite(ValidPosNumTest.class));
//        suite.addTest(new TestSuite(UnitsToStringTest.class));
//        suite.addTest(new TestSuite(TensToStringTest.class));
//        suite.addTest(new TestSuite(HundredsToStringTest.class));
//        suite.addTest(new TestSuite(Go3ToTextTest.class));
//        suite.addTest(new TestSuite(Go9ToTextTest.class));
//        return suite;
//    }
}
