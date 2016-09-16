package ru.spbau.my_hashtable.my_list;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class MyListTest {
    private MyList listTest;

    @Before
    public void setUp() throws Exception {
        listTest = new MyList();

        listTest.put("test1", "1");
        listTest.put("test2", "2");
        listTest.put("test3", "3");
    }

    @Test
    public void contains() throws Exception {
        assertTrue(listTest.contains("test1"));
        assertTrue(listTest.contains("test2"));
        assertFalse(listTest.contains("Test"));
    }

    @Test
    public void get() throws Exception {
        assertEquals("1", listTest.get("test1"));
        assertEquals("2", listTest.get("test2"));
        assertEquals("3", listTest.get("test3"));
        assertNull(listTest.get("TEST"));
    }

    @Test
    public void put() throws Exception {
        assertEquals("1", listTest.put("test1", "11"));
        assertEquals("2", listTest.put("test2", "22"));
        assertNull(listTest.put("test4", "4"));
    }

    @Test
    public void delete() throws Exception {
        assertEquals("1", listTest.delete("test1"));
        assertEquals("2", listTest.delete("test2"));
        assertNull(listTest.delete("test1"));
    }

}