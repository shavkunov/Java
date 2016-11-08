package ru.spbau.shavkunov.hw6;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyTreeSetImplTest {
    private MyTreeSet<Integer> t;

    @Before
    public void setUp() throws Exception {
        t = new MyTreeSetImpl<Integer>();
    }

    @Test
    public void addTest() throws Exception {
        t.add(100);
        t.add(10);
        t.add(20);

        assertEquals(3, t.size());
    }

    @Test
    public void containsTest() throws Exception {
        t.add(5);
        assertTrue(t.contains(5));
        t.add(10);
        assertTrue(t.contains(10));
        t.add(0);

        assertTrue(t.contains(5));
        assertTrue(t.contains(10));
        assertTrue(t.contains(0));

        assertFalse(t.contains(20));
    }

    @Test
    public void removeTest() throws Exception {
        t.add(5);
        t.add(10);
        t.add(20);

        assertTrue(t.contains(10));
        t.remove(10);
        assertFalse(t.contains(10));
        t.remove(5);
        t.remove(20);

        assertFalse(t.contains(5));
        assertEquals(0, t.size());
    }

    @Test
    public void methodLastTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(20, (int) t.last());
        t.add(100);
        assertEquals(100, (int) t.last());
    }

    @Test
    public void methodFirstTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(5, (int) t.first());
        t.add(0);
        assertEquals(0, (int) t.first());
    }

    @Test
    public void lowerTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(10, (int) t.lower(20));
        assertEquals(20, (int) t.lower(21));
    }

    @Test
    public void floorTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(20, (int) t.floor(20));
        assertEquals(5, (int) t.floor(9));
    }

    @Test
    public void ceilingTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(20, (int) t.ceiling(20));
        assertNull(t.ceiling(21));
    }

    @Test
    public void higherTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        assertEquals(20, (int) t.higher(10));
        assertEquals(10, (int) t.higher(9));
    }

    @Test
    public void itetatorTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        Iterator<Integer> it = t.iterator();
        assertEquals(5, it.next().intValue());
        assertEquals(10, it.next().intValue());
        assertEquals(20, it.next().intValue());
    }

    @Test
    public void descendingItetatorTest() throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        Iterator<Integer> it = t.descendingIterator();
        assertEquals(20, it.next().intValue());
        assertEquals(10, it.next().intValue());
        assertEquals(5, it.next().intValue());
    }

    @Test
    public void descendingSetTest()  throws Exception {
        t.add(5);
        t.add(20);
        t.add(10);

        MyTreeSet<Integer> dt = t.descendingSet();
        assertEquals(5, dt.last().intValue());
        assertEquals(20, dt.first().intValue());
        assertEquals(5, dt.higher(10).intValue());
        assertEquals(20, dt.lower(10).intValue());
        assertEquals(10, dt.ceiling(11).intValue());
        assertEquals(10, dt.floor(9).intValue());
    }
}