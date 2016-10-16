package ru.spbau.shavkunov.hw5;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PredicateTest {
    private Predicate<Integer> even, odd, mod6;

    @Before
    public void setUp() throws Exception {
        even = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };

        odd = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 2 == 1;
            }
        };

        mod6 = new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer x) {
                return x % 6 == 0;
            }
        };
    }

    @Test
    public void or() throws Exception {
        assertTrue(odd.apply(5));
        assertFalse(odd.apply(4));
        assertTrue(even.apply(4));
        assertFalse(even.apply(5));

        assertTrue(even.or(odd).apply(1));
        assertTrue(even.or(odd).apply(2));
    }

    @Test
    public void and() throws Exception {
        assertFalse(even.and(odd).apply(1));
        assertFalse(even.and(odd).apply(2));

        assertTrue(mod6.and(even).apply(12));
        assertFalse(mod6.and(even).apply(14));
    }

    @Test
    public void not() throws Exception {
        assertTrue(mod6.not().apply(4));
        assertFalse(mod6.not().apply(6));
    }

    @Test
    public void alwaysTrue() throws Exception {
        assertTrue(mod6.alwaysTrue().apply(4));
    }

    @Test
    public void alwaysFalse() throws Exception {
        assertFalse(mod6.alwaysFalse().apply(5));
    }
}