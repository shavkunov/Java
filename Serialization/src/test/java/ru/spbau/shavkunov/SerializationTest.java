package ru.spbau.shavkunov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.assertTrue;

class TestClass {
    public int intField;
    public short shortField;
    protected byte byteField;
    private String stringField;
    public boolean booleanField;
    public float floatField;
    public double doubleField;
    public char charField;
    public long longField;

    public TestClass() {
        intField = 0;
        shortField = 0;
        byteField = 0;
        stringField = "";
        booleanField = false;
        floatField = 0;
        doubleField = 0;
        charField = 0;
        longField = 0;
    }

    public TestClass(int arg1, short arg2, byte arg3, String arg4,
                     boolean arg5, float arg6, double arg7, char arg8, long arg9) {
        intField = arg1;
        shortField = arg2;
        byteField = arg3;
        stringField = arg4;
        booleanField = arg5;
        floatField = arg6;
        doubleField = arg7;
        charField = arg8;
        longField = arg9;
    }

    public boolean equals(TestClass tc) {
        return intField == tc.intField &&
               shortField == tc.shortField &&
               byteField == tc.byteField &&
               stringField.equals(tc.stringField) &&
               booleanField == tc.booleanField &&
               floatField == tc.floatField &&
               doubleField == tc.doubleField &&
               charField == tc.charField &&
               longField == tc.longField;
    }
}

public class SerializationTest {
    private TestClass correct;

    @Before
    public void setUp() throws Exception {
        correct = new TestClass(Integer.MAX_VALUE, Short.MAX_VALUE, Byte.MAX_VALUE, "Test Class name",
                                true, Float.MAX_VALUE, Double.MAX_VALUE, Character.MIN_VALUE, Long.MAX_VALUE);
    }

    @Test
    public void serializeTest() throws Exception {
        File f = new File("output");
        FileOutputStream fos = new FileOutputStream(f);
        Serialization.serialize(correct, fos);
    }

    @Test
    public void deserializeTest() throws Exception {
        File f = new File("output");
        FileOutputStream fos = new FileOutputStream(f);
        Serialization.serialize(correct, fos);

        FileInputStream fis = new FileInputStream(f);
        TestClass actual = Serialization.deserialize(fis, TestClass.class);

        assertTrue(correct.equals(actual));
    }

    @After
    public void tearDown() throws Exception {
        File f = new File("output");
        f.delete();
    }
}