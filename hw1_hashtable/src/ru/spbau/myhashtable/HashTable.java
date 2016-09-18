package ru.spbau.myhashtable;

import ru.spbau.myhashtable.mylist.MyList;

/**
 * Реализация HashTable на списках, выполняющая каждую операцию в среднем за константу.
 *
 * @author mikhail shavkunov
 */
public class HashTable {
    private MyList[] table;

    /**
     * capacity -- количество списков в HashTable.
     */
    private int capacity;

    /**
     * size -- количество элементов в HashTable.
     */
    private int size;

    private int hasher(String s) {
        return ((s.hashCode() % capacity) + capacity) % capacity;
    }

    public int size() {
        return size;
    }

    public HashTable() {
        init(8);
    }

    /**
     * Инициализация HashTable.
     * @param inputCapacity количество ячеек(списков) в HashTable после инициализации.
     */
    private void init(int inputCapacity) {
        table = new MyList[inputCapacity];
        capacity = inputCapacity;
        for (int i = 0; i < capacity; i++) {
            table[i] = new MyList();
        }
        size = 0;
    }

    public HashTable(int inputCapacity) {
        init(inputCapacity);
    }

    /**
     * Содержится ли элемент в таблице по ключу. O(1) в среднем.
     * @param key ключ проверяемого элемента
     * @return true если элемент содержится, false иначе.
     */
    public boolean contains(String key) {
        int index = hasher(key);
        return table[index].contains(key);
    }

    /**
     * Получить элемент по ключу. O(1) в среднем.
     * @param key ключ элемента, который хотим получить.
     * @return null если такого объекта нет, иначе значение по ключу.
     */
    public String get(String key) {
        int index = hasher(key);
        return table[index].get(key);
    }

    /**
     * Положить элемент в HashTable. O(1) в среднем.
     * @param key ключ нового элемента.
     * @param value значение нового элемента
     * @return null, если по такому ключу не было элемента, иначе предыдущий элемент.
     */
    public String put(String key, String value) {
        int index = hasher(key);
        String result = table[index].put(key, value);

        if (result == null) {
            size++;
        }

        return result;
    }

    /**
     * Удаление элемента по ключу. О(1) в среднем.
     * @param key ключ удаляемого элемента
     * @return null, если удаляемого элемента в таблице нет, иначе удалённое значение.
     */
    public String remove(String key) {
        int index = hasher(key);
        size--;
        return table[index].delete(key);
    }

    public void clear() {
        init(8);
    }
}
