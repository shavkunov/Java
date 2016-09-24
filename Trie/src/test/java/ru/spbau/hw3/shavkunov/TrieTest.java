package ru.spbau.hw3.shavkunov;

import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

public class TrieTest {
    private Trie t;

    @Before
    public void setUp() throws Exception {
        t = new Trie();
    }

    @Test
    public void addTest() throws Exception {
        t.add("test");
        t.add("abacaba");
        assertEquals(t.size(), 2);
        assertTrue(t.contains("test"));
        assertFalse(t.contains("testhw"));
    }

    @Test
    public void removeTest() throws Exception {
        t.add("test");
        assertTrue(t.contains("test"));
        assertEquals(1, t.size());
        t.remove("test");
        assertFalse(t.contains("test"));
        assertEquals(0, t.size());
    }

    @Test
    public void howManyStartsWithPrefixTest() throws Exception {
        t.add("abcd");
        t.add("abcdeqg");
        t.add("corruption");
        t.add("correct");
        t.add("correspondent");
        t.add("corrupt");
        t.add("corra");

        assertEquals(5, t.howManyStartsWithPrefix("corr"));
        assertEquals(2, t.howManyStartsWithPrefix("abcd"));
    }

    @Test
    public void serializeTest() throws Exception {
        t.add("abcd");
        t.add("abcdeqg");
        t.add("corrupt");
        t.add("corruption");
        t.add("correct");
        t.add("correspondent");
        t.add("corra");

        try (FileOutputStream out = new FileOutputStream("file.txt")) {
            //DataOutputStream dataOut = new DataOutputStream(out);
            //t.serialize(dataOut);
            t.serialize(out);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        Trie deserializedTrie = new Trie();
        try (FileInputStream in = new FileInputStream("file.txt")) {
            //DataInputStream dataIn = new DataInputStream(in);
            //deserializedTrie.deserialize(dataIn);

            deserializedTrie.deserialize(in);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        assertEquals(7, deserializedTrie.size());
        assertTrue(deserializedTrie.contains("abcd"));
        assertTrue(deserializedTrie.contains("abcdeqg"));
        assertTrue(deserializedTrie.contains("correct"));
        assertTrue(deserializedTrie.contains("correspondent"));
        assertTrue(deserializedTrie.contains("corrupt"));
        assertTrue(deserializedTrie.contains("corra"));
        assertTrue(deserializedTrie.contains("corruption"));
    }

    @Test
    public void containsTest() throws Exception {
        t.add("corrupt");
        t.add("corruption");
        assertEquals(2, t.size());
        assertTrue(t.contains("corrupt"));
        assertTrue(t.contains("corruption"));

        Trie anotherTrie = new Trie();
        anotherTrie.add("corruption");
        anotherTrie.add("corrupt");

        assertEquals(2, anotherTrie.size());
        assertTrue(anotherTrie.contains("corrupt"));
        assertTrue(anotherTrie.contains("corruption"));
    }
}