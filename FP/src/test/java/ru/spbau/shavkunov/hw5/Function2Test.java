package ru.spbau.shavkunov.hw5;

import org.junit.Before;
import org.junit.Test;

import static java.lang.StrictMath.sqrt;
import static org.junit.Assert.*;

public class Function2Test {
    private Function2<Double, Double, Double> f;
    private Function2<Integer, Integer, Integer> h;

    @Before
    public void setUp() throws Exception {
        f = new Function2<Double, Double, Double>() {
            @Override
            public Double apply(Double x, Double y) {
                return x * x + y * y;
            }
        };

        h = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer x, Integer y) {
                return x - y;
            }
        };
    }

    @Test
    public void apply() throws Exception {
        assertEquals((Double) 25.0, f.apply(3.0, 4.0));
    }

    @Test
    public void compose() throws Exception {
        Function1<Double, Double> g = new Function1<Double, Double>() {
            @Override
            public Double apply(Double x) {
                return sqrt(x);
            }
        };

        assertEquals((Double) 5.0, f.compose(g).apply(3.0, 4.0));
    }

    @Test
    public void bind1() throws Exception {
        assertEquals((Integer) 10, h.bind1(13).apply(3));
    }

    @Test
    public void bind2() throws Exception {
        assertEquals((Integer) 5, h.bind2(10).apply(15));
    }

    @Test
    public void curry() throws Exception {
        assertEquals((Double) 25.0, f.curry().apply(3.0).apply(4.0));
        assertEquals((Integer) 10, h.curry().apply(17).apply(7));
    }
}