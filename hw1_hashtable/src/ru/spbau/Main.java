package ru.spbau;

import ru.spbau.my_hashtable.HashTable;
import ru.spbau.my_hashtable.my_list.MyList;

public class Main {

    public static void testList() {
        MyList listExample = new MyList();

        System.out.println(listExample.put("test1", "1") + "");
        System.out.println(listExample.put("test2", "2") + "");
        System.out.println(listExample.put("test3", "3") + "");
        listExample.printList();

        System.out.println(listExample.delete("test2") + "");
        listExample.printList();

        System.out.println(listExample.put("test1", "1011") + "");
        listExample.printList();
    }

    public static void main(String[] args) {
        //testList();

        HashTable h = new HashTable();

        System.out.println(h.put("Test1", "1") + "");
        System.out.println(h.get("Test1") + "");
        System.out.println(h.put("Test2", "2") + "");
        System.out.println(h.get("Test2") + "");
        System.out.println(h.put("Test1", "11") + "");
        System.out.println(h.size() + "\n");
        System.out.println(h.remove("Test2") + "");
        System.out.println(h.get("Test1") + "");
        System.out.println(h.contains("Test1"));
        System.out.println(h.contains("Test2"));
        
        h.clear();

    }
}
