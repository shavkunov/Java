package ru.spbau.shavkunov.hw5;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class CollectionsTest {
    private Function1<Integer, Integer> f;
    private Function2<Integer, Integer, Integer> sum, sub;
    private Predicate<Integer> even, odd;
    private ArrayList<Integer> al;
    private LinkedList<Integer> ll;

    @Before
    public void setUp() throws Exception {
        f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + x;
            }
        };

        sum = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        };

        sub = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x - y;
            }
        };

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

        al = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            al.add(i);
        }

        ll = new LinkedList<Integer>();
        ll.add(2);
        ll.add(4);
        ll.add(6);
        ll.add(9);
        ll.add(10);
    }

    @Test
    public void map() throws Exception {
        ArrayList myResult1 = Collections.map(al, f);
        ArrayList correct1 = new ArrayList(Arrays.asList(0, 2, 4, 6, 8));
        assertEquals(correct1, myResult1);

        ArrayList myResult2 = Collections.map(ll, f);
        ArrayList correct2 = new ArrayList(Arrays.asList(4, 8, 12, 18, 20));
        assertEquals(correct2, myResult2);
    }

    @Test
    public void filter() throws Exception {
        ArrayList myResult1 = Collections.filter(al, even);
        ArrayList correct1 = new ArrayList(Arrays.asList(0, 2, 4));
        assertEquals(correct1, myResult1);

        ArrayList myResult2 = Collections.filter(ll, even);
        ArrayList correct2 = new ArrayList(Arrays.asList(2, 4, 6, 10));
        assertEquals(correct2, myResult2);
    }

    @Test
    public void takeWhile() throws Exception {
        ArrayList myResult1 = Collections.takeWhile(al, even);
        ArrayList correct1 = new ArrayList(Arrays.asList(0));
        assertEquals(correct1, myResult1);

        ArrayList myResult2 = Collections.takeWhile(ll, even);
        ArrayList correct2 = new ArrayList(Arrays.asList(2, 4, 6));
        assertEquals(correct2, myResult2);
    }

    @Test
    public void takeUnless() throws Exception {
        ArrayList myResult1 = Collections.takeUnless(al, odd);
        ArrayList correct1 = new ArrayList(Arrays.asList(0));
        assertEquals(correct1, myResult1);

        ArrayList myResult2 = Collections.takeUnless(ll, odd);
        ArrayList correct2 = new ArrayList(Arrays.asList(2, 4, 6));
        assertEquals(correct2, myResult2);
    }

    @Test
    public void foldl() throws Exception {
        assertEquals((Integer) 10, Collections.foldl(al, sum, 0));
        assertEquals((Integer) 31, Collections.foldl(ll, sum, 0));
    }

    @Test
    public void foldr() throws Exception {
        assertEquals((Integer) 2, Collections.foldr(al, sub, 0));
        assertEquals((Integer) 5, Collections.foldr(ll, sub, 0));
    }
}