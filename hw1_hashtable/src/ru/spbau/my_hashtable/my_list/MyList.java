package ru.spbau.my_hashtable.my_list;

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

    public boolean contains(String key) {
        Node curNode = head;

        while(curNode != null && !curNode.key.equals(key)) {
            curNode = curNode.next;
        }

        return curNode != null;
    }

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

    public String get(String key) {
        Node curNode = getNode(key);

        if (curNode == null) {
            return null;
        }

        return curNode.value;
    }

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

    private void add(String key, String value) {
        if (head == null) {
            head = new Node(key, value);
            tail = head;
        } else {
            tail.next = new Node(key, value);
            tail = tail.next;
        }
    }

    public String delete(String key) {
        if (head.key.equals(key)) {
            String result = head.value;
            head = head.next;
            return result;
        }

        Node curNode = head;
        while(!curNode.next.key.equals(key))
            curNode = curNode.next;

        if (curNode.next.key.equals(key)) {
            String result = curNode.next.value;
            curNode.next = curNode.next.next;
            return result;
        } else {
            return null;
        }
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
