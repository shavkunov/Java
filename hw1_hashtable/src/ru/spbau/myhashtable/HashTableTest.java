package ru.spbau.myhashtable;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HashTableTest {
    private HashTable t;

    @Before
    public void setUp() throws Exception {
        t = new HashTable();

        t.put("Test1", "1");
        t.put("Test2", "2");
        t.put("Test3", "3");
    }

    @Test
    public void sizeTest() throws Exception {
        assertEquals(3, t.size());

        t.put("Test", "0");
        assertEquals(4, t.size());
    }

    @Test
    public void containsTest() throws Exception {
        assertTrue(t.contains("Test1"));
        assertTrue(t.contains("Test2"));
        assertFalse(t.contains("Test"));
    }

    @Test
    public void getTest() throws Exception {
        assertEquals("1", t.get("Test1"));
        assertEquals("2", t.get("Test2"));
        assertNull(t.get("Test"));
    }

    @Test
    public void putTest() throws Exception {
        assertEquals("1", t.put("Test1", "11"));
        assertEquals("2", t.put("Test2", "22"));
        assertNull(t.put("Test", "0"));
    }

    @Test
    public void removeTest() throws Exception {
        assertNull(t.remove("TEST"));
        assertEquals("1", t.remove("Test1"));
        assertEquals("2", t.remove("Test2"));
    }

    @Test
    public void clearTest() throws Exception {
        t.clear();

        assertNull(t.get("Test1"));
        assertNull(t.get("Test2"));
        assertEquals(0, t.size());
        assertFalse(t.contains("Test3"));
        assertNull(t.put("x", "y"));
    }

}