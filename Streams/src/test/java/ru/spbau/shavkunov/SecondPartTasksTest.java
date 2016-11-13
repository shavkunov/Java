package ru.spbau.shavkunov;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static ru.spbau.shavkunov.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        assertEquals(Arrays.asList("asdbfuonivmabanucoij;lmk;j", "aba", "Hello aba!"),
                findQuotes(Arrays.asList("quotesTest/a.txt", "quotesTest/b.txt", "quotesTest/file.txt"), "aba"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(Math.abs(piDividedBy4() - Math.PI / 4.0) < 0.001);
    }

    @Test
    public void testFindPrinter() {
        HashMap<String, List<String>> m = new HashMap<>();

        m.put("Alex", Arrays.asList("abc", "abc", "abc"));
        m.put("Bob", Arrays.asList("aa", "asdf", "ab"));
        m.put("Tom", Arrays.asList("aaaaaaa", "abbbbc", "bbbb"));

        assertEquals("Tom", findPrinter(m));
    }

    @Test
    public void testCalculateGlobalOrder() {
        HashMap<String, Integer> m1 = new HashMap<>();
        m1.put("a", 1);
        m1.put("b", 2);
        m1.put("c", 3);

        HashMap<String, Integer> m2 = new HashMap<>();
        m2.put("d", 4);
        m2.put("a", 9);

        HashMap<String, Integer> m3 = new HashMap<>();
        m3.put("b", 3);
        m3.put("d", 16);

        HashMap<String, Integer> correct = new HashMap<>();
        correct.put("a", 10);
        correct.put("b", 5);
        correct.put("c", 3);
        correct.put("d", 20);

        assertEquals(correct, calculateGlobalOrder(Arrays.asList(m1, m2, m3)));
    }
}