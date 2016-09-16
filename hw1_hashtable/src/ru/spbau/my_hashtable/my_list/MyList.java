package ru.spbau.my_hashtable.my_list;

/**
 * Реализация списка для HashTable.
 *
 * @author mikhail shavkunov
 */

public class MyList {
    private Node head, tail;

    private class Node {
        private String key, value;
        private Node next;
        private Node(String inputKey, String inputValue) {
            key = inputKey;
            value = inputValue;
            next = null;
        }
    }

    public MyList() {
        head = tail = null;
    }

    /**
     * Есть ли в списке элемент с заданным ключом. O(length), length - длина списка.
     * @param key ключ проверяемого элемента.
     * @return true если элемент есть, false иначе.
     */

    public boolean contains(String key) {
        Node curNode = head;

        while(curNode != null && !curNode.key.equals(key)) {
            curNode = curNode.next;
        }

        return curNode != null;
    }

    /**
     * Получение узла списка с нужным ключом. O(length), length - длина списка.
     * @param key ключ по которому нужно получить узел.
     * @return null, если такого нет, иначе сам элемент.
     */

    private Node getNode(String key) {
        if (!contains(key)) {
            return null;
        }

        Node curNode = head;

        while(curNode != null && !curNode.key.equals(key)) {
            curNode = curNode.next;
        }

        return curNode;
    }

    /**
     * Получение значения элемента списка по ключу. O(length), length - длина списка.
     * @param key ключ элемента, который хотим получить.
     * @return null, если элемента с таким ключом нет, иначе значение.
     */

    public String get(String key) {
        Node curNode = getNode(key);

        if (curNode == null) {
            return null;
        }

        return curNode.value;
    }

    /**
     * Добавление элемента в список. O(length), length - длина списка.
     * @param key ключ добавляемого элемента.
     * @param value значение добавляемого элемента.
     * @return null, если не было элемента с таким ключом, иначе значение элемента, которое было по ключу key.
     */


    public String put(String key, String value) {

        Node curNode = getNode(key);

        if (curNode == null) {
            add(key, value);
            return null;
        }

        String result = curNode.value;
        curNode.value = value;

        return result;
    }

    /**
     * Добавление элемента в список. O(length), length - длина списка.
     * @param key ключ добавляемого элемента.
     * @param value значение добавляемого элемента.
     */

    private void add(String key, String value) {
        if (head == null) {
            head = new Node(key, value);
            tail = head;
        } else {
            tail.next = new Node(key, value);
            tail = tail.next;
        }
    }

    /**
     * Удаление элемента из списка по ключу.
     * @param key ключ удаляемого элемента.
     * @return null если удаляемого элемента нет, иначе значение удаляемого элемента.
     */

    public String delete(String key) {
        if (!contains(key)) {
            return null;
        }

        if (head.key.equals(key)) {
            String result = head.value;
            head = head.next;
            return result;
        }

        Node curNode = head;
        while(!curNode.next.key.equals(key))
            curNode = curNode.next;

        String result = curNode.next.value;
        curNode.next = curNode.next.next;
        return result;
    }

    public void printList() {
        Node curNode = head;

        while(curNode != null) {
            System.out.println(curNode.key + " " + curNode.value);
            curNode = curNode.next;
        }

        System.out.println();
    }
}
