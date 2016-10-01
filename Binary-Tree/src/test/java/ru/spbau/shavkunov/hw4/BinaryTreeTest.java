package ru.spbau.shavkunov.hw4;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryTreeTest {
    @Test
    public void addTest() throws Exception {
        BinaryTree<Integer> t = new BinaryTree<Integer>();
        t.add(5);
        t.add(3);
        t.add(2);
    }

    @Test
    public void containsTest() throws Exception {
        BinaryTree<Integer> t = new BinaryTree<Integer>();
        t.add(5);
        t.add(3);
        t.add(2);

        assertTrue(t.contains(5));
        assertTrue(t.contains(3));
        assertTrue(t.contains(2));
        assertEquals(3, t.size());
        assertFalse(t.contains(1));
    }

    @Test
    public void sizeTest() throws Exception {
        BinaryTree<Integer> t = new BinaryTree<Integer>();

        t.add(1);
        assertEquals(1, t.size());
        t.add(2);
        assertEquals(2, t.size());
        t.add(3);
        assertEquals(3, t.size());
    }
}