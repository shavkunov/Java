package ru.spbau.shavkunov.hw6;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;

public class MyTreeSetImpl<E> extends AbstractSet<E> implements MyTreeSet<E> {
    private Node root;
    private int size;
    private Comparator<E> cmp;

    public MyTreeSetImpl() {
        size = 0;
        root = null;
        cmp = null;
    }

    public MyTreeSetImpl(Comparator<E> cmp) {
        this();
        this.cmp = cmp;
    }

    private class Node {
        public Node left;
        public Node right;
        public Node parent;
        E data;

        public Node(E data) {
            left = null;
            right = null;
            parent = null;
            this.data = data;
        }

        public Node(E data, Node parent) {
            this(data);
            this.parent = parent;
        }

        public Node mostLeft() {
            Node res = this;
            while (res.left != null) {
                res = res.left;
            }

            return res;
        }

        public Node mostRight() {
            Node res = this;
            while (res.right != null) {
                res = res.right;
            }

            return res;
        }

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

        public void setLeft(E data, Node parent) {
            left = new Node(data, parent);
        }

        public void setRight(E data, Node parent) {
            right = new Node(data, parent);
        }
    }

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

    public boolean contains(Object obj) {
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
            } else {
                if (dataStorage.parent.left == dataStorage) {
                    dataStorage.parent.left = dataStorage.left;
                } else {
                    dataStorage.parent.right = dataStorage.left;
                }
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

    public int size() {
        return size;
    }

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
            return MyTreeSetImpl.this.ceiling(e);
        }

        @Override
        public E floor(E e) {
            return MyTreeSetImpl.this.higher(e);
        }

        @Override
        public E ceiling(E e) {
            return MyTreeSetImpl.this.lower(e);
        }

        @Override
        public E higher(E e) {
            return MyTreeSetImpl.this.floor(e);
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

    public MyTreeSet<E> descendingSet() {
        return new MyDescendingSet();
    }

    public E first() {
        return root.mostLeft().data;
    }

    public E last() {
        return root.mostRight().data;
    }

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

    public E lower(E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) < 0;
        return getLeastElement(less, e);
    }

    public E floor(E e) {
        BiFunction<E, E, Boolean> less = (e1, e2) -> cmp.compare(e1, e2) <= 0;
        return getLeastElement(less, e);
    }

    public E ceiling(E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) > 0;
        return getMostElement(more, e);
    }

    public E higher(E e) {
        BiFunction<E, E, Boolean> more = (e1, e2) -> cmp.compare(e1, e2) >= 0;
        return getMostElement(more, e);
    }
}