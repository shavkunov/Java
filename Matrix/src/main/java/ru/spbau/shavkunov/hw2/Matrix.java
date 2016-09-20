package ru.spbau.shavkunov.hw2;

import java.util.Arrays;
import java.util.Vector;

/**
 * Реализация класса ru.spbau.shavkunov.hw2.Matrix.
 * Класс умеет сортировать столбцы по возрастанию первых элементов и делать обход по спирали, начиная из центра.
 *
 * @author mikhail shavkunov
 */
public class Matrix {
    /**
     * Матрица хранится как список столбцов.
     */
    private Column[] data;

    /**
     * Индекс элемента в матрице.
     */
    private class Index {
        /**
         * Строчка матрицы, где расположен элемент.
         */
        private int x;

        /**
         * Столбец матрицы, где расположен элемент.
         */
        private int y;

        public Index(int row, int column) {
            x = row;
            y = column;
        }

        public void setRow(int newValue) {
            x = newValue;
        }

        public int getRow() {
            return x;
        }

        public int getElement() {
            return data[x].get(y);
        }

        public int getColumn() {
            return y;
        }

        public void setColumn(int newValue) {
            y = newValue;
        }
    }

    private class Column implements Comparable<Column> {
        /**
         * Столбец матрицы
         */
        private int[] data;

        public Column(int size) {
            data = new int[size];
        }

        public int get(int index) {
            return data[index];
        }

        public void set(int index, int value) {
            data[index] = value;
        }

        public int compareTo(Column col) {
            if (data[0] <= col.get(0)) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    private void set(int[][] inputData) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[j].set(i, inputData[i][j]);
            }
        }
    }

    /**
     * Извлечение элемента матрицы.
     * @param x координата строки.
     * @param y координата столбца.
     * @return возвращаемый элемент.
     */
    public int get(int x, int y) {
        return data[y].get(x);
    }

    /**
     * Сортировка столбцов матрицы по возрастанию первых элементов.
     */
    public void sortColumns() {
        Arrays.sort(data);
    }

    /**
     *
     * @return размер квадратной матрицы.
     */
    public int size() {
        return data.length;
    }

    private void init(int size) {
        data = new Column[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Column(data.length);
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[j].set(i, j * data.length + i);
            }
        }
    }

    /**
     * Конструктор по умолчанию создаст матрицу 3*3 с некоторым ее заполнением.
     */
    public Matrix() {
        init(3);
    }

    /**
     * Аналогично конструктору по умолчанию.
     * @param inputSize размер создаваемой матрицы.
     */
    public Matrix(int inputSize) {
        init(inputSize);
    }

    /**
     * Создание матрицы по двумерному массиву.
     * @param inputData основа матрицы.
     */
    public Matrix(int[][] inputData) {
        init(inputData.length);
        this.set(inputData);
    }

    /**
     * Вывод всех элементов матрицы в естесственном порядке.
     */
    public void print() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                System.out.print(data[j].get(i) + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param x вывод столбца x.
     */
    public void printColumn(int x) {
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[x].get(i));
        }
    }

    /**
     * Вспомогательный метод для реализации обхода по спирали spiralPrint.
     * Вывод заданного количества элементов в направлении, задаваемом смещением (dx, dy).
     * @param dx смещение по координате строки
     * @param dy смещение по координате столбца
     * @param length сколько раз смещаться
     * @return true если дошли до левого верхнего угла матрицы, false иначе
     */
    private boolean straightWalk(Vector<Integer> tour, Index ind, int dx, int dy, int length) {
        for (int i = 0; i < length; i++) {
            if (ind.getRow() == ind.getColumn() && ind.getRow() == 0) {
                return true;
            }

            ind.setRow(ind.getRow() + dx);
            ind.setColumn(ind.getColumn() + dy);
            tour.add(ind.getElement());
        }

        return false;
    }

    /**
     * Обход матрицы по спирали начиная с центра.
     * @return возвращает набор элементов матрицы в порядке обхода.
     */
    public Vector<Integer> spiralTour() {
        Index ind = new Index(data.length / 2, data.length / 2);
        Vector<Integer> tour = new Vector<Integer>();

        tour.add(ind.getElement());
        for (int length = 1; length <= data.length; length++) {
            /**
             * Смещение по координате столбца.
             */
            int dy;

            /**
             * Смещение по координате строки.
             */
            int dx;

            if (length % 2 == 1) {
                dy = -1;
                dx = 1;
            } else {
                dy = 1;
                dx = -1;
            }

            if (straightWalk(tour, ind, 0, dy, length)) {
                break;
            }

            straightWalk(tour, ind, dx, 0, length);
        }

        return tour;
    }

    /**
     * Перегразка метода equals, сравнение матриц.
     * @param in на вход метод принимает матрицу.
     * @return true если матрицы равны поэлементно, false иначе.
     */
    public boolean equals(Object in) {
        Matrix input = (Matrix) in;

        if (data.length != input.size()) {
            return false;
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (this.get(i, j) != input.get(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }
}
