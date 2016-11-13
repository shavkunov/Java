package ru.spbau.shavkunov.hw6;

import java.util.Iterator;
import java.util.Set;

public interface MyTreeSet<E> extends Set<E> {

    /**
     * @return возвращает итератор обхода множества в обратном порядке.
     */
    Iterator<E> descendingIterator();

    /**
     * @return возвращает структуру с обратными операциями. Т.е. прямой обход становится обратным, а lower становится higher и тд.
     */
    MyTreeSet<E> descendingSet();

    /**
     * @return возвращает наименьший элемент множества.
     */
    E first();

    /**
     * @return возвращает наибольший элемент множества.
     */
    E last();

    /**
     * Поиск максимального элемента, который строго меньше чем е.
     * @return возвращает искомый элемент.
     */
    E lower(E e);

    /**
     * Поиск максимального элемента, который нестрого меньше чем е.
     * @return возвращает искомый элемент.
     */
    E floor(E e);

    /**
     * Поиск минимального элемента, который строго больше чем е.
     * @return возвращает искомый элемент.
     */
    E ceiling(E e);

    /**
     * Поиск минимального элемента, который нестрого больше чем е.
     * @return возвращает искомый элемент.
     */
    E higher(E e);
}