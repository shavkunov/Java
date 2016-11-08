package ru.spbau.shavkunov.hw6;

import java.util.Iterator;
import java.util.Set;

public interface MyTreeSet<E> extends Set<E> {

    Iterator<E> descendingIterator();

    MyTreeSet<E> descendingSet();
    
    E first();

    E last();

    E lower(E e);

    E floor(E e);

    E ceiling(E e);

    E higher(E e);
}
