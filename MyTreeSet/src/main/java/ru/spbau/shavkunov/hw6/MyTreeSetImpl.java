package ru.spbau.shavkunov.hw6;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
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
    public MyTreeSetImpl(Comparator<E> cmp) {
        this();
        this.cmp = cmp;
    }

    /**
     * Рекурсивное добавление, вспомогательная функция для add.
     * @param root корень дерева, куда хотим добавить элемент data.
     * @return false, если элемент в дереве уже был, иначе true.
     */
    private boolean addRecursive(Node root, E data) {
        if (cmp.compare(root.data, data) == 0) {
            return false;
        }

        if (cmp.compare(root.data, data) < 0) {
            if (root.left != null) {
                return addRecursive(root.left, data);
            } else {
                root.setLeft(data, root);
                size++;
                return true;
            }
        } else {
            if (root.right != null) {
                return addRecursive(root.right, data);
            } else {
                root.setRight(data, root);
                size++;
                return true;
            }
        }
    }

    /**
     * Добавление элемента data в дерево.
     * @return false, если элемент в дереве уже был, иначе true.
     */
    public boolean add(E data) {
        if (cmp == null) {
            cmp = (a, b) -> ((Comparable) a).compareTo(b);
        }

        if (root == null) {
            root = new Node(data);
            size++;
            return true;
        }

        return addRecursive(root, data);
    }

    /**
     * Поиск элемента data в дереве.
     * @return возвращается Node с этим элементом, если он был в дереве, иначе возвратится Node с элементом lower(data).
     */
    private Node findElement(E data) {
        Node res = root;

        int compareRes = cmp.compare(root.data, data);
        while (compareRes != 0 && res != null) {
            if (compareRes < 0) {
                res = res.left;
            } else {
                res = res.right;
            }

            compareRes = cmp.compare(root.data, data);
        }

        return res;
    }

    /**
     * Удаление элемента obj из дерева.
     * @return false, если удаление не производилось(элемента нет в дереве или obj не типа E), иначе true.
     */
    public boolean remove(Object obj) {
        E data = (E) obj;

        if (data == null) {
            return false;
        }

        Node dataStorage = findElement(data);

        if (dataStorage == null || cmp.compare(dataStorage.data, data) != 0) {
            return false;
        }

        if (dataStorage.left == null || dataStorage.right == null) {
            if (dataStorage.left == null) {
                if (dataStorage.parent.left == dataStorage) {
                    dataStorage.parent.left = dataStorage.right;
                } else {
                    dataStorage.parent.right = dataStorage.right;
                }
                dataStorage.right = null;
            } else {
                if (dataStorage.parent.left == dataStorage) {
                    dataStorage.parent.left = dataStorage.left;
                } else {
                    dataStorage.parent.right = dataStorage.left;
                }
                dataStorage.left = null;
            }
        } else  {
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

        dataStorage.parent = null;
        size--;
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

            @Override
            public boolean hasNext() {
                return root != null;
            }

            @Override
            public E next() {
                E res = root.data;
                root = root.next();
                return res;
            }
        };
    }

    /**
     * @return возвращает итератор обхода множества в обратном порядке.
     */
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node root = MyTreeSetImpl.this.root.mostRight();

            @Override
            public boolean hasNext() {
                return root != null;
            }

            @Override
            public E next() {
                E res = root.data;
                root = root.previous();
                return res;
            }
        };
    }

    /**
     * @return возвращает структуру с обратными операциями. Т.е. прямой обход становится обратным, а lower становится higher и тд.
     */
    public MyTreeSet<E> descendingSet() {
        return new MyDescendingSet();
    }

    /**
     * @return возвращает наименьший элемент множества.
     */
    public E first() {
        return root.mostLeft().data;
    }

    /**
     * @return возвращает наибольший элемент множества.
     */
    public E last() {
        return root.mostRight().data;
    }

    /**
     * Вспомогательная функция, реализующая поиск через сравнение < и <=.
     * @param less компаратор, сравнивающий либо на строгое меньше либо на нестрогое.
     * @param data элемент, относительно которого ищем не превосходящий его.
     * @return возвращает искомый элемент.
     */
    private E getLeastElement(BiFunction<E, E, Boolean> less, E data) {
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
    private E getMostElement(BiFunction<E, E, Boolean> more, E data) {
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

    /**
     * Поиск максимального элемента, который строго меньше чем е.
     * @return возвращает искомый элемент.
     */
    public E lower(E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) < 0;
        return getLeastElement(less, e);
    }

    /**
     * Поиск максимального элемента, который нестрого меньше чем е.
     * @return возвращает искомый элемент.
     */
    public E floor(E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) <= 0;
        return getLeastElement(less, e);
    }

    /**
     * Поиск минимального элемента, который строго больше чем е.
     * @return возвращает искомый элемент.
     */
    public E ceiling(E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) > 0;
        return getMostElement(more, e);
    }

    /**
     * Поиск минимального элемента, который нестрого больше чем е.
     * @return возвращает искомый элемент.
     */
    public E higher(E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) >= 0;
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
        public E first() {
            return MyTreeSetImpl.this.last();
        }

        @Override
        public E last() {
            return MyTreeSetImpl.this.first();
        }

        @Override
        public E lower(E e) {
            return MyTreeSetImpl.this.higher(e);
        }

        @Override
        public E floor(E e) {
            return MyTreeSetImpl.this.ceiling(e);
        }

        @Override
        public E ceiling(E e) {
            return MyTreeSetImpl.this.floor(e);
        }

        @Override
        public E higher(E e) {
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
        public Node(E data) {
            left = null;
            right = null;
            parent = null;
            this.data = data;
        }

        /**
         * Конструктор с указанием предка вершины.
         */
        public Node(E data, Node parent) {
            this(data);
            this.parent = parent;
        }

        /**
         * Поиск наиболее левой вершины, относительно текущей.
         * @return возвращает искомую вершину.
         */
        public Node mostLeft() {
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
        public Node mostRight() {
            Node res = this;
            while (res.right != null) {
                res = res.right;
            }

            return res;
        }

        /**
         * @return возвращает вершину, следующую в порядке обхода дерева алгоритмом dfs.
         */
        public Node next() {
            if (right != null) {
                return right.mostLeft();
            }

            Node res = this;

            while (res.parent.right == res) {
                res = res.parent;
            }

            return res.parent;
        }

        /**
         * @return аналогично next, только вершина будет предыдущей.
         */
        public Node previous() {
            if (left != null) {
                return left.mostRight();
            }

            Node res = this;

            while (res.parent.left == res) {
                res = res.parent;
            }

            return res.parent;
        }

        /**
         * Установить левым сыном конкретную вершину.
         */
        public void setLeft(E data, Node parent) {
            left = new Node(data, parent);
        }

        /**
         * Установить правым сыном конкретную вершину.
         */
        public void setRight(E data, Node parent) {
            right = new Node(data, parent);
        }
    }
}