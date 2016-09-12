package ru.spbau.my_hashtable;

import ru.spbau.my_hashtable.my_list.MyList;

public class HashTable {
    private MyList[] table;
    private int capacity, size;

    private int hasher(String s) {
        return s.hashCode() % capacity;
    }

    public int size() {
        return size;
    }

    public HashTable() {
        capacity = 8;
        table = new MyList[capacity];
        for (int i = 0; i < capacity; i++)
            table[i] = new MyList();
        size = 0;
    }

    public HashTable(int inputCapacity) {
        table = new MyList[inputCapacity];
        capacity = inputCapacity;
        for (int i = 0; i < capacity; i++)
            table[i] = new MyList();
        size = 0;
    }

    public boolean contains(String key) {
        int index = hasher(key);

        return table[index].contains(key);
    }

    public String get(String key) {
        int index = hasher(key);

        return table[index].get(key);
    }

    public String put(String key, String value) {
        int index = hasher(key);

        String result = table[index].put(key, value);

        if (result == null) {
            size++;
        }

        return result;
    }

    public String remove(String key) {
        int index = hasher(key);

        size--;

        return table[index].delete(key);
    }

    public void clear() {
        capacity = 8;
        table = new MyList[capacity];
        for (int i = 0; i < capacity; i++)
            table[i] = new MyList();
        size = 0;
    }
}
