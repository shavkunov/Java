package ru.spbau.shavkunov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static ru.spbau.shavkunov.Database.*;

public class DatabaseTest {
    @Before
    public void setUp() throws Exception {
        initDatabase();
    }

    private void fillDatabase() throws SQLException {
        addEntry("name1", 1);
        addEntry("name1", 11);
        addEntry("name1", 111);
        addEntry("name2", 2);
        addEntry("name2", 3);
        addEntry("name3", 3);
        addEntry("name4", 4);
    }

    @Test
    public void addTest() throws Exception {
        fillDatabase();
    }

    @Test
    public void searchByNameTest() throws Exception {
        fillDatabase();

        ResultSet results = getNumbers("name1");
        results.next();
        assertEquals(1, results.getInt("number"));
        results.next();
        assertEquals(11, results.getInt("number"));
        results.next();
        assertEquals(111, results.getInt("number"));

        results = getNumbers("name2");
        results.next();
        assertEquals(2, results.getInt("number"));

        results = getNumbers("name3");
        results.next();
        assertEquals(3, results.getInt("number"));
    }

    @Test
    public void searchByNumberTest() throws Exception {
        fillDatabase();

        ResultSet results = getNames(3);
        results.next();
        assertEquals("name2", results.getString("name"));
        results.next();
        assertEquals("name3", results.getString("name"));
    }

    @Test
    public void deleteEntryTest() throws Exception {
        fillDatabase();

        deleteEntry("name3", 3);
        ResultSet rs = getNumbers("name3");
        assertNull(rs);
    }

    @Test
    public void changeNameTest() throws Exception {
        fillDatabase();

        changeName("name4", "name5", 4);
        ResultSet rs = getNames(4);
        rs.next();
        assertEquals("name5", rs.getString("name"));
    }

    @Test
    public void changeNumberTest() throws Exception {
        fillDatabase();

        changeNumber("name4", 4, 5);
        ResultSet rs = getNames(4);
        assertNull(rs);
        rs = getNames(5);
        assertEquals("name4", rs.getString("name"));
    }

    @Test
    public void allEntriesTest() throws Exception {
        fillDatabase();

        ResultSet rs = getAllEntries();
        rs.next();
        assertEquals("name1#1", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name1#11", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name1#111", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name2#2", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name2#3", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name3#3", rs.getString("name") + "#" + rs.getLong("number"));
        rs.next();
        assertEquals("name4#4", rs.getString("name") + "#" + rs.getLong("number"));
    }

    @After
    public void tearDown() throws Exception {
        closeDatabase();

        File db = new File("content.db");
        if (db.exists()) {
            db.delete();
        }
    }
}