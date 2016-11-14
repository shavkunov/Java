package ru.spbau.shavkunov.hw5;

import org.junit.Test;

import static java.lang.StrictMath.sqrt;
import static org.junit.Assert.*;

public class Function1Test {
    @Test
    public void applyTest() throws Exception {
        Function1<Integer, Integer> f = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 5;
            }
        };

        int myResult = f.apply(6);
        assertEquals(11, myResult);
    }

    @Test
    public void composeTest() throws Exception {
        Function1<Integer, Double> f = new Function1<Integer, Double>() {
            @Override
            public Double apply(Integer x) {
                return x + 5.0;
            }
        };

        Function1<Double, Double> g = new Function1<Double, Double>() {
            @Override
            public Double apply(Double x) {
                return sqrt(x);
            }
        };

        Double myResult1 = f.compose(g).apply(4);
        assertEquals((Double) 3.0, myResult1);

        Double myResult2 = f.compose(g).apply(6);
        assertEquals((Double) sqrt(11), myResult2);
    }
}