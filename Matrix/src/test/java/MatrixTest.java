import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

public class MatrixTest {
    private Matrix m;

    @Test
    public void spiralPrint() throws Exception {
        m = new Matrix();
        Integer[] answer1 = {4, 3, 6, 7, 8, 5, 2, 1, 0};
        Vector answerVect = new Vector(Arrays.asList(answer1));
        Vector myAnswer = m.spiralPrint();
        assertTrue(answerVect.equals(myAnswer));

        m = new Matrix(1);
        myAnswer = m.spiralPrint();
        assertTrue(myAnswer.toString().equals("[0]"));

        m = new Matrix(5);
        Integer[] answer2 = {12, 11, 16, 17, 18, 13, 8, 7, 6, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0};
        answerVect = new Vector(Arrays.asList(answer2));
        myAnswer = m.spiralPrint();
        assertTrue(answerVect.equals(myAnswer));
    }

    @Test
    public void sortColumns() throws Exception {
        int[][] mydata = { {2, 1, 0}, {5, 4, 3}, {8, 7, 6} };
        m = new Matrix(mydata);
        m.sortColumns();

        int[][] answer = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8} };
        Matrix correct = new Matrix(answer);

        assertTrue(m.equals(correct));
    }

    @Test
    public void equals() throws Exception {
        int[][] data1 = { {0, 0, 0}, {2, 2, 2}, {3, 3, 3} };
        int[][] data2 = { {3, 3, 3}, {0, 0, 0}, {1, 1, 1} };

        Matrix a = new Matrix(data1);
        Matrix b = new Matrix(data2);

        assertFalse(a.equals(b));

        int[][] data3 = { {0, 2, 3}, {0, 2, 3}, {0, 2, 3} };
        int[][] data4 = { {3, 0, 2}, {3, 0, 2}, {3, 0, 2} };

        a = new Matrix(data3);
        b = new Matrix(data4);
        b.sortColumns();

        assertTrue(a.equals(b));
    }


}