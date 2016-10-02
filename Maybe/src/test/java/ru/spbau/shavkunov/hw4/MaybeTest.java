package ru.spbau.shavkunov.hw4;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeTest {
    @Test
    public void isPresent() throws Exception {
        Maybe<Integer> x = Maybe.just(10);

        assertTrue(x.isPresent());
        Integer res = x.get();
        assertEquals(10, res.intValue());
    }

    @Test
    public void mapTest() throws Exception {
        Maybe<Integer> a = Maybe.just(5);
        Function<Integer, Double> f = x -> (x * 1.0) / 3.0;
        Double result = 5.0/3.0;
        assertEquals(result, a.map(f).get());
    }

    @Test
    public void readNumberTest() throws Exception {
        String s = "1234";
        int res = Maybe.readNumber(s).get();
        assertEquals(1234, res);

        String letters = "abacaba";
        assertFalse(Maybe.readNumber(letters).isPresent());
    }

    @Test
    public void readFileTest() throws Exception {
        PrintWriter out = new PrintWriter("test.txt");

        out.println("12341");
        out.println("not a number");
        out.println("3341number");
        out.println("0");
        out.println("-128");
        out.println("       234  ");
        out.println("5123Maybe");
        out.close();

        InputStream in = new FileInputStream("test.txt");
        ArrayList<Maybe<Integer>> res = Maybe.readFile(in);

        assertEquals(res.get(0).get().intValue(), 12341);
        assertFalse(res.get(1).isPresent());
        assertFalse(res.get(2).isPresent());
        assertEquals(res.get(3).get().intValue(), 0);
        assertEquals(res.get(4).get().intValue(), -128);
        assertEquals(res.get(5).get().intValue(), 234);
        assertFalse(res.get(6).isPresent());

        File f = new File("test.txt");
        f.delete();
    }

    @Test
    public void writeToFileTest() throws Exception {
        File fin = new File("input.txt");
        File fout = new File("output.txt");
        PrintWriter out = new PrintWriter(fin);

        out.println("12341");
        out.println("not a number");
        out.println("3341number");
        out.println("0");
        out.println("-128");
        out.println("       234");
        out.println("5123Maybe");
        out.close();

        InputStream is = new FileInputStream(fin);
        OutputStream os = new FileOutputStream(fout);
        ArrayList<Maybe<Integer>> res = Maybe.readFile(is);
        Maybe.writeToFile(os, res);

        File inputTest = new File("input.txt");
        File outputTest = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputTest);
        InputStream outputStream = new FileInputStream(outputTest);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputStream));

        String line1 = inputReader.readLine();
        String line2 = outputReader.readLine();

        while (true) {
            if (line1 == null) {
                assertNull(line2);
                break;
            }
            assertNotNull(line2);

            Maybe<Integer> fromInput = Maybe.readNumber(line1);
            Maybe<Integer> fromOutput = Maybe.readNumber(line2);

            if (fromInput.isPresent() && fromOutput.isPresent()) {
                Integer first = fromInput.get() * fromInput.get();
                Integer second = fromOutput.get();
                assertEquals(first.intValue(), second.intValue());
            } else {
                assertEquals(fromInput.isPresent(), fromOutput.isPresent());
            }

            line1 = inputReader.readLine();
            line2 = outputReader.readLine();
        }

        inputTest.delete();
        outputTest.delete();
    }
}