package ru.spbau.shavkunov.hw6;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * Реализация интерфейса MyTreeSet для типа данных E.
 */
public class MyTreeSetImpl<E> extends AbstractSet<E> implements MyTreeSet<E> {
    /**
     * Корень бинарного дерева.
     */
    private Node root;

    /**
     * Размер непустых узлов дерева.
     */
    private int size;

    /**
     * Компаратор, умеющий сравнивать два элемента типа E.
     */
    private Comparator<E> cmp;

    /**
     * Состояние дерева.
     */
    private long state = 0;

    /**
     * Конструктор, подразумевающий, что тип Е, можно сравнивать.
     */
    public MyTreeSetImpl() {
        size = 0;
        root = null;
        cmp = null;
    }

    /**
     * Конструтор, который явно принимает компаратор, по которому сравнивать элементы.
     */
    public MyTreeSetImpl(@NotNull Comparator<E> cmp) {
        this();
        this.cmp = cmp;
    }

    /**
     * Генерация следующего состояния.
     */
    private long generateState() {
        Random rand = new Random();
        return rand.nextLong();
    }

    /**
     * Рекурсивное добавление, вспомогательная функция для add.
     * @param root корень дерева, куда хотим добавить элемент data.
     * @return false, если элемент в дереве уже был, иначе true.
     */
    private boolean addRecursive(@NotNull Node root,@NotNull E data) {
        if (cmp.compare(root.data, data) == 0) {
            return false;
        }

        if (cmp.compare(root.data, data) >= 0) {
            if (root.left != null) {
                return addRecursive(root.left, data);
            } else {
                root.setLeft(data, root);
                size++;
                state = generateState();
                return true;
            }
        } else {
            if (root.right != null) {
                return addRecursive(root.right, data);
            } else {
                root.setRight(data, root);
                size++;
                state = generateState();
                return true;
            }
        }
    }

    /**
     * Добавление элемента data в дерево.
     * @return false, если элемент в дереве уже был, иначе true.
     */
    public boolean add(@NotNull E data) {
        if (cmp == null) {
            cmp = (a, b) -> ((Comparable) a).compareTo(b);
        }

        if (root == null) {
            root = new Node(data);
            size++;
            state = generateState();
            return true;
        }

        return addRecursive(root, data);
    }

    /**
     * Поиск элемента data в дереве.
     * @return возвращается Node с этим элементом, если он был в дереве, иначе возвратится Node с элементом lower(data).
     */
    private @Nullable Node findElement(@NotNull E data) {
        Node res = root;

        if (root == null) {
            return null;
        }

        int compareRes = cmp.compare(res.data, data);
        while (compareRes != 0) {
            if (compareRes < 0) {
                res = res.right;
            } else {
                res = res.left;
            }

            if (res == null) {
                break;
            }
            compareRes = cmp.compare(res.data, data);
        }

        return res;
    }

    /**
     * Содержится ли obj в дереве.
     * @return true, если obj присутствует в дереве, иначе false.
     */
    @Override
    public boolean contains(@NotNull Object obj) {
        E data = (E) obj;

        Node dataStorage = findElement(data);

        if (dataStorage == null || cmp.compare(dataStorage.data, data) != 0) {
            return false;
        }

        return true;
    }

    /**
     * Удаление элемента obj из дерева.
     * @return false, если удаление не производилось(элемента нет в дереве или obj не типа E), иначе true.
     */
    @Override
    public boolean remove(@NotNull Object obj) {
        if (!contains(obj)) {
            return false;
        }

        E data = (E) obj;
        Node dataStorage = findElement(data);

        if (dataStorage == root && root.left == root.right && root.left == null) {
            root = null;
            size = 0;
            state = generateState();
            return true;
        }

        if (dataStorage.left == null || dataStorage.right == null) {
            if (dataStorage.left == null) {
                if (dataStorage == root) {
                    root = dataStorage.right;
                    root.parent = null;
                    size--;
                    state = generateState();
                    return true;
                }

                if (dataStorage.parent.left == dataStorage) {
                    dataStorage.parent.left = dataStorage.right;
                } else {
                    dataStorage.parent.right = dataStorage.right;
                }
                dataStorage.right = null;
            } else {
                if (dataStorage == root) {
                    root = dataStorage.left;
                    root.parent = null;
                    size--;
                    state = generateState();
                    return true;
                }

                if (dataStorage.parent.left == dataStorage) {
                    dataStorage.parent.left = dataStorage.left;
                } else {
                    dataStorage.parent.right = dataStorage.left;
                }
                dataStorage.left = null;
            }
        } else  {
            if (dataStorage == root) {
                root = dataStorage.left;
                root.parent = null;
                size--;
                state = generateState();
                return true;
            }

            if (dataStorage.parent.right == dataStorage) {
                dataStorage.parent.right = dataStorage.right;
            } else {
                dataStorage.parent.left = dataStorage.right;
            }

            Node leftParent = dataStorage.right.mostLeft();
            leftParent.left = dataStorage.left;
            dataStorage.right = null;
            dataStorage.left = null;
        }

        if (dataStorage != root) {
            dataStorage.parent = null;
        }
        size--;
        state = generateState();
        return true;
    }

    /**
     * @return возвращает количество элементов в множестве.
     */
    public int size() {
        return size;
    }

    /**
     * @return возвращает итератор обхода множества в прямом порядке.
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node root = MyTreeSetImpl.this.root.mostLeft();
            private long state = MyTreeSetImpl.this.state;

            @Override
            public boolean hasNext() {
                if (state != MyTreeSetImpl.this.state) {
                    throw new RuntimeException();
                }

                return root != null;
            }

            @Override
            public E next() {
                if (state != MyTreeSetImpl.this.state) {
                    throw new RuntimeException();
                }

                E res = root.data;
                root = root.next();
                return res;
            }
        };
    }

    /** {@link MyTreeSet#descendingIterator()} **/
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node root = MyTreeSetImpl.this.root.mostRight();
            private long state = MyTreeSetImpl.this.state;

            @Override
            public boolean hasNext() {
                if (state != MyTreeSetImpl.this.state) {
                    throw new RuntimeException();
                }

                return root != null;
            }

            @Override
            public E next() {
                if (state != MyTreeSetImpl.this.state) {
                    throw new RuntimeException();
                }

                E res = root.data;
                root = root.previous();
                return res;
            }
        };
    }

    /** {@link MyTreeSet#descendingSet()} **/
    public MyTreeSet<E> descendingSet() {
        return new MyDescendingSet();
    }

    /** {@link MyTreeSet#first()} **/
    public @Nullable E first() {
        if (root == null) {
            return null;
        }

        return root.mostLeft().data;
    }

    /** {@link MyTreeSet#last()} **/
    public @Nullable E last() {
        if (root == null) {
            return null;
        }

        return root.mostRight().data;
    }

    /**
     * Вспомогательная функция, реализующая поиск через сравнение < и <=.
     * @param less компаратор, сравнивающий либо на строгое меньше либо на нестрогое.
     * @param data элемент, относительно которого ищем не превосходящий его.
     * @return возвращает искомый элемент.
     */
    private @Nullable E getLeastElement(@NotNull BiFunction<E, E, Boolean> less, @NotNull E data) {
        Node cur = root;
        E res = null;

        while (cur != null) {
            if (less.apply(cur.data, data)) {
                res = cur.data;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }

        return res;
    }

    /**
     * Аналогично с getLeastElement, только сравнения < и <= заменяются на > и >= соответственно.
     */
    private @Nullable E getMostElement(@NotNull BiFunction<E, E, Boolean> more, @NotNull E data) {
        Node cur = root;
        E res = null;

        while (cur != null) {
            if (more.apply(cur.data, data)) {
                res = cur.data;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        return res;
    }

    /** {@link MyTreeSet#lower(Object)} **/
    public @Nullable E lower(@NotNull E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) < 0;
        return getLeastElement(less, e);
    }

    /** {@link MyTreeSet#floor(Object)} **/
    public @Nullable E floor(@NotNull E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) <= 0;
        return getLeastElement(less, e);
    }

    /** {@link MyTreeSet#ceiling(Object)} **/
    public @Nullable E ceiling(@NotNull E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) >= 0;
        return getMostElement(more, e);
    }

    /** {@link MyTreeSet#higher(Object)} **/
    public @Nullable E higher(@NotNull E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) > 0;
        return getMostElement(more, e);
    }

    /**
     * Класс, реализующий логику DescendingSet, использующий только реализацию методов MyTreeSetImpl.
     */
    public class MyDescendingSet extends AbstractSet<E> implements MyTreeSet<E> {
        MyDescendingSet() {}

        @Override
        public Iterator descendingIterator() {
            return MyTreeSetImpl.this.iterator();
        }

        @Override
        public MyTreeSet descendingSet() {
            return MyTreeSetImpl.this;
        }

        @Override
        public @Nullable E first() {
            return MyTreeSetImpl.this.last();
        }

        @Override
        public @Nullable E last() {
            return MyTreeSetImpl.this.first();
        }

        @Override
        public @Nullable E lower(@NotNull E e) {
            return MyTreeSetImpl.this.higher(e);
        }

        @Override
        public @Nullable E floor(@NotNull E e) {
            return MyTreeSetImpl.this.ceiling(e);
        }

        @Override
        public @Nullable E ceiling(@NotNull E e) {
            return MyTreeSetImpl.this.floor(e);
        }

        @Override
        public @Nullable E higher(@NotNull E e) {
            return MyTreeSetImpl.this.lower(e);
        }

        @Override
        public Iterator iterator() {
            return MyTreeSetImpl.this.descendingIterator();
        }

        @Override
        public int size() {
            return MyTreeSetImpl.this.size();
        }
    }

    /**
     * Вершина дерева.
     */
    private class Node {
        /**
         * Левый сын.
         */
        public Node left;

        /**
         * Правый сын.
         */
        public Node right;

        /**
         * Предок вершины.
         */
        public Node parent;

        /**
         * Значение, хранящееся в вершине.
         */
        E data;

        /**
         * Конструктор без предка.
         */
        public Node(@NotNull E data) {
            left = null;
            right = null;
            parent = null;
            this.data = data;
        }

        /**
         * Конструктор с указанием предка вершины.
         */
        public Node(@NotNull E data, @NotNull Node parent) {
            this(data);
            this.parent = parent;
        }

        /**
         * Поиск наиболее левой вершины, относительно текущей.
         * @return возвращает искомую вершину.
         */
        public @Nullable Node mostLeft() {
            Node res = this;
            while (res.left != null) {
                res = res.left;
            }

            return res;
        }

        /**
         * Поиск наиболее правой вершины, относительно текущей.
         * @return возвращает искомую вершину.
         */
        public @Nullable Node mostRight() {
            Node res = this;
            while (res.right != null) {
                res = res.right;
            }

            return res;
        }

        /**
         * @return возвращает вершину, следующую в порядке обхода дерева алгоритмом dfs.
         */
        public @Nullable Node next() {
            if (right != null) {
                return right.mostLeft();
            }

            Node res = this;

            while (res.parent.right == res) {
                res = res.parent;

                if (res.parent == null) {
                    return null;
                }
            }

            return res.parent;
        }

        /**
         * @return аналогично next, только вершина будет предыдущей.
         */
        public @Nullable Node previous() {
            if (left != null) {
                return left.mostRight();
            }

            Node res = this;

            if (res.parent == null) {
                return null;
            }

            while (res.parent.left == res) {
                res = res.parent;

                if (res.parent == null) {
                    return null;
                }
            }

            return res.parent;
        }

        /**
         * Установить левым сыном конкретную вершину.
         */
        public void setLeft(@NotNull E data, @NotNull Node parent) {
            left = new Node(data, parent);
        }

        /**
         * Установить правым сыном конкретную вершину.
         */
        public void setRight(@NotNull E data, @NotNull Node parent) {
            right = new Node(data, parent);
        }
    }
}