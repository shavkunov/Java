package ru.spbau.shavkunov.hw4;

public class BinaryTree<T extends Comparable<? super T>> {
    /**
     * Корень дерева.
     */
    private Node root;

    /**
     * Размер дерева.
     */
    private int size;

    /**
     * Дерево состоит из узлов.
     */
    private class Node {
        /**
         * Информация в узле.
         */
        private T data;

        /**
         * Левое поддерево узла.
         */
        private Node left;

        /**
         * Правое поддерево узла.
         */
        private Node right;

        public Node(T data) {
            this.data = data;
            left = null;
            right = null;
        }

        /**
         * Устанавливает в левое поддерево вершину с информацией data
         */
        public void setLeft(T data) {
            left = new Node(data);
        }

        /**
         * Устанавливает в правое поддерево вершину с информацией data
         */
        public void setRight(T data) {
            right = new Node(data);
        }

        public T getData() {
            return data;
        }

        /**
         * @return true если левое поддерево не пустое.
         */
        public boolean isPresentLeft() {
            return left != null;
        }

        /**
         * @return true если правое поддерево не пустое.
         */
        public boolean isPresentRight () {
            return right != null;
        }

        /**
         * @return Получение левого поддерева.
         */
        public Node getLeft() {
            return left;
        }

        /**
         * @return Получение правого поддерева.
         */
        public Node getRight() {
            return right;
        }
    }

    public BinaryTree() {
        root = null;
        size = 0;
    }

    /**
     * Максимальный спуск вниз по дерево.
     * @param root Старт происходит из корня.
     * @param data Информация по которой ищется подходящее место.
     * @return Возвращает последнюю вершину, на которую можно спуститься вниз в зависимости от data.
     */
    private Node walkDown(Node root, T data) {
        if (root.getData().compareTo(data) <= 0) {
            if (root.isPresentLeft()) {
                return walkDown(root.getLeft(), data);
            } else {
                return root;
            }
        } else {
            if (root.isPresentRight()) {
                return walkDown(root.getRight(), data);
            } else {
                return root;
            }
        }
    }

    /**
     * Добавление узла с ключом data.
     */
    public void add(T data) {
        if (root == null) {
            root = new Node(data);
        }
        Node lastNode = walkDown(root, data);
        if (lastNode.getData().compareTo(data) <= 0) {
            lastNode.setLeft(data);
        } else {
            lastNode.setRight(data);
        }
        size++;
    }

    /**
     * @return Размер дерева.
     */
    public int size() {
        return this.size;
    }

    /**
     * @return true если содержиться узел с ключом data, false иначе.
     */
    public boolean contains(T data) {
        Node lastNode = walkDown(root, data);
        if (lastNode.getData().compareTo(data) != 0) {
            return false;
        } else {
            return true;
        }
    }
}