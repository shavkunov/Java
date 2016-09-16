package ru.spbau.my_hashtable;

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
    public void size() throws Exception {
        assertEquals(3, t.size());

        t.put("Test", "0");
        assertEquals(4, t.size());
    }

    @Test
    public void contains() throws Exception {
        assertTrue(t.contains("Test1"));
        assertTrue(t.contains("Test2"));
        assertFalse(t.contains("Test"));
    }

    @Test
    public void get() throws Exception {
        assertEquals("1", t.get("Test1"));
        assertEquals("2", t.get("Test2"));
        assertNull(t.get("Test"));
    }

    @Test
    public void put() throws Exception {
        assertEquals("1", t.put("Test1", "11"));
        assertEquals("2", t.put("Test2", "22"));
        assertNull(t.put("Test", "0"));
    }

    @Test
    public void remove() throws Exception {
        assertNull(t.remove("TEST"));
        assertEquals("1", t.remove("Test1"));
        assertEquals("2", t.remove("Test2"));
    }

}