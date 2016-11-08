package ru.spbau.shavkunov.hw6;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyTreeSetImplTest {
    private MyTreeSet<Integer> t;

    @Before
    public void setup() throws Exception {
        t = new MyTreeSetImpl<Integer>();
    }

    @Test
    public void addTest() throws Exception {
        t.add(100);
        t.add(10);
        t.add(20);

        assertEquals(3, t.size());
    }
}