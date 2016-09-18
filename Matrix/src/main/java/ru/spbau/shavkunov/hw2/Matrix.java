package ru.spbau.shavkunov.hw2;

import java.util.Arrays;
import java.util.Vector;

/**
 * Реализация класса ru.spbau.shavkunov.hw2.Matrix.
 *
 * @author mikhail shavkunov
 */
public class Matrix {
    /**
     * Матрица хранится как список столбцов.
     */
    private Column[] data;

    /**
     * Переменная хранящая обход матрица по спирали.
     */
    private Vector<Integer> tour;

    /**
     * Размер квадратной матрицы.
     */
    private int size;

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
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
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
        return this.size;
    }

    private void init() {
        data = new Column[size];
        for (int i = 0; i < size; i++) {
            data[i] = new Column(size);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[j].set(i, j * size + i);
            }
        }
    }

    /**
     * Конструктор по умолчанию создаст матрицу 3*3 с некоторым ее заполнением.
     */
    public Matrix() {
        size = 3;
        init();
    }

    /**
     * Аналогично конструктору по умолчанию.
     * @param inputSize размер создаваемой матрицы.
     */
    public Matrix(int inputSize) {
        size = inputSize;
        init();
    }

    /**
     * Создание матрицы по двумерному массиву.
     * @param inputData основа матрицы.
     */
    public Matrix(int[][] inputData) {
        size = inputData.length;
        init();
        this.set(inputData);
    }

    /**
     * Вывод всех элементов матрицы в естесственном порядке.
     */
    public void print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(data[j].get(i) + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param x вывод столбца x.
     */
    public void printColumn(int x) {
        for (int i = 0; i < size; i++) {
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
    private boolean straightWalk(Index ind, int dx, int dy, int length) {
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
        Index ind = new Index(size / 2, size / 2);
        tour = new Vector<Integer>();

        tour.add(ind.getElement());
        for (int length = 1; length <= size; length++) {
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

            if (straightWalk(ind, 0, dy, length)) {
                break;
            }

            straightWalk(ind, dx, 0, length);
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

        if (size != input.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.get(i, j) != input.get(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }
}
