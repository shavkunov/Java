package ru.spbau.hw3.shavkunov;

import java.io.*;
import java.util.AbstractCollection;

/**
 * Реализация бора. Для простоты реализации бор работает только на латинском алфавите с маленькими буквами.
 */
public class Trie implements StreamSerializable {
    /**
     * Корень бора.
     */
    private Node root;

    private class Node implements Serializable {
        public static final int ALPHABET_SIZE = 26;
        public static final int CODE_SYMBOL_A = 97;

        /**
         * Из вершины в вершину осуществляется переход по строчному символу латинского алфавита.
         */
        private Node[] next;

        /**
         * Отец вершины.
         */
        private Node parent;

        /**
         * Размер поддерева. Т.е. количество терминальных вершин в текущем поддереве.
         */
        private int size;

        /**
         * False если вершина не терминальна, иначе true.
         */
        public boolean isTerminal;

        public Node(boolean isEnd, Node parent) {
            next = new Node[ALPHABET_SIZE];
            this.parent = parent;
            isTerminal = isEnd;
            if (isEnd) {
                size = 1;
            } else {
                size = 0;
            }
        }

        /**
         * Символу сопоставляется индекс в массиве переходов.
         */
        private int getIndex(char symbol) {
            return symbol - CODE_SYMBOL_A;
        }

        /**
         * Добавление вершине сына.
         * @param symbol В сына осуществляется переход по этому символу.
         * @param isEnd false, если добавляемая вершина не терминальна, иначе true.
         */
        public void setNext(char symbol, boolean isEnd) {
            int index = getIndex(symbol);
            next[index] = new Node(isEnd, this);
        }

        /**
         * Возвращает корень подддерева, в который можно перейти по заданному символу.
         * @return null, если такого перехода не существует, иначе корень поддерева.
         */
        public Node getNext(char symbol) {
            int index = getIndex(symbol);
            return next[index];
        }

        /**
         * Удаление всех поддеревьев имеющих нулевой размер, для удаления строки из бора.
         */
        public void deleteSons() {
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                if (next[i] != null && next[i].size == 0) {
                    next[i] = null;
                }
            }
        }

        /**
         * Задание вершине ее терминальности. Необходимо только для того, чтобы снять метку при удалении.
         * @param flag true, чтобы вершина стала терминальной, иначе false.
         */
        private void setTerminal(boolean flag) {
            isTerminal = flag;
        }

        private void writeObject(java.io.ObjectOutputStream stream) throws IOException, ClassNotFoundException {
            stream.writeInt(size);
            //stream.writeObject(parent);
            stream.writeBoolean(isTerminal);
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                stream.writeObject(next[i]);
            }
        }

        private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
            size = stream.readInt();
            isTerminal = stream.readBoolean();
            //parent = (Node)stream.readObject();
            next = new Node[ALPHABET_SIZE];
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                next[i] = (Node) stream.readObject();
            }
        }
    }

    public Trie() {
        root = new Node(false, null);
    }

    /**
     * Добавление строки в бор. O(length).
     * @return true, если строки такой еще не было в боре, иначе false.
     */
    public boolean add(String s) throws IllegalArgumentException {
        for (int i = 0; i < s.length(); i++) {
            if (!(s.charAt(i) >= 'a' && s.charAt(i) <= 'z')) {
                throw new IllegalArgumentException("Invalid string!");
            }
        }

        if (contains(s)) {
            return false;
        }
        Node curNode = root;
        for (int i = 0; i < s.length() - 1; i++) {
            char curSymbol = s.charAt(i);
            curNode.size++;
            if (curNode.getNext(curSymbol) == null) {
                curNode.setNext(curSymbol, false);
            }
            curNode = curNode.getNext(curSymbol);
        }

        char lastSymbol = s.charAt(s.length() - 1);
        if (curNode.getNext(lastSymbol) == null) {
            curNode.setNext(lastSymbol, true);
        } else {
            curNode = curNode.getNext(lastSymbol);
            curNode.setTerminal(true);
        }
        curNode.size++;
        return true;
    }

    /**
     * Максимальный спуск вниз по строке s.
     * @param s Строка, по которой будет произведен спуск вниз.
     * @param sizeShift По пути можно менять размер вершин бора. С помощью этого происходит удаление строки из бора.
     *                  Если передать -1, будет уменьшение размера всех вершин на пути на 1.
     * @return Возвращается вершина, которая соответствует максимальному присутствующему в боре префиксу строки s.
     * Null, если строки нет в боре.
     */
    private Node walkDown(String s, int sizeShift) {
        Node curNode = root;

        for (int i = 0; i < s.length(); i++) {
            char curSymbol = s.charAt(i);
            curNode.size += sizeShift;
            curNode = curNode.getNext(curSymbol);

            if (curNode == null) {
                return null;
            }
        }
        return curNode;
    }

    /**
     * Проверка наличия строки s в боре.
     * @return true, если строка в боре присутствует, false иначе.
     */
    public boolean contains(String s) {
        Node lastNode = walkDown(s, 0);
        return lastNode != null && lastNode.isTerminal;
    }

    /**
     * Удаление строки s из бора.
     * @return true, если строка была в боре, иначе false.
     */
    public boolean remove(String s) {
        if (!contains(s)){
            return false;
        }
        Node lastNode = walkDown(s, -1);
        lastNode.setTerminal(false);

        while (lastNode.size == 0 & lastNode.parent != null) {
            lastNode = lastNode.parent;
        }
        lastNode.deleteSons();
        return true;
    }

    /**
     * Возвращает количество строк в боре.
     */
    public int size() {
        return root.size;
    }

    /**
     * Возвращает количество строк, начинающихся с заданного префикса.
     */
    public int howManyStartsWithPrefix(String prefix) {
        Node lastNode = walkDown(prefix, 0);
        if (lastNode == null) {
            return 0;
        } else {
            return lastNode.size;
        }
    }

    /**
     * Сериализация бора.
     * @param out Поток вывода.
     */
    public void serialize(OutputStream out) throws IOException {
        try (ObjectOutputStream myOut = new ObjectOutputStream(out)) {
            myOut.writeObject(root);
            myOut.flush();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Десериализация бора.
     * @param in Входной поток.
     */
    public void deserialize(InputStream in) throws IOException {
        try (ObjectInputStream myIn = new ObjectInputStream(in)) {
            Node tmpNode =  (Node) myIn.readObject();
            this.root = tmpNode;
        } catch (ClassNotFoundException c){
            System.out.print("Class Trie not found");
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * Печать всех слов на экран.
     */
    public void print() {
        recursivePrint(root, "");
    }

    private void recursivePrint(Node curNode, String s) {
        if (curNode.isTerminal) {
            System.out.println(s);
        }
        for (int i = 0; i < Node.ALPHABET_SIZE; i++) {
            if (curNode.next[i] != null) {
                recursivePrint(curNode.next[i], s + (char) (Node.CODE_SYMBOL_A + i));
            }
        }
    }
}