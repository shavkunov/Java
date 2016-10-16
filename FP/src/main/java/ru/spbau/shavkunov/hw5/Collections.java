package ru.spbau.shavkunov.hw5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Реализация некоторых стандартных методов для работы с лямбда функциями.
 */
public final class Collections {
    /**
     * Принимает коллекцию a и функцию f.
     * @return Возращает список в порядке обхода итератора,
     * где каждый элемент это результат применения функции f к элементу коллекции.
     */
    public static <A, B> ArrayList<B> map(Iterable<A> a, Function1<A, B> f) {
        Iterator it = a.iterator();
        ArrayList<B> resList = new ArrayList<B>();

        while (it.hasNext()) {
            Object element = it.next();
            resList.add(f.apply((A) element));
        }

        return resList;
    }

    /**
     * Принимает коллекцию a и предикат f.
     * @return Возвращает список элементов коллекции, которые удолетворяют предикату f.
     */
    public static <A> ArrayList<A> filter(Iterable<A> a, Predicate<A> f) {
        Iterator it = a.iterator();
        ArrayList<A> resList = new ArrayList<A>();

        while (it.hasNext()) {
            Object element = it.next();
            if (f.apply((A) element)) {
                resList.add((A) element);
            }
        }

        return resList;
    }

    /**
     * Принимает коллекцию a и предикат f.
     * @return В порядке обхода итератора возвращает список элементов до первого элемента, который не удолетворяет условию предиката.
     */
    public static <A> ArrayList<A> takeWhile(Iterable<A> a, Predicate<A> f) {
        Iterator it = a.iterator();
        ArrayList<A> resList = new ArrayList<A>();

        while (it.hasNext()) {
            Object element = it.next();
            if (f.apply((A) element)) {
                resList.add((A) element);
            } else {
                break;
            }
        }

        return resList;
    }

    /**
     * Аналогично takeWhile, только список будет до первого элемента, который предикату удолетворяет.
     */
    public static <A> ArrayList<A> takeUnless(Iterable<A> a, Predicate<A> f) {
        Iterator it = a.iterator();
        ArrayList<A> resList = new ArrayList<A>();

        while (it.hasNext()) {
            Object element = it.next();
            if (!f.apply((A) element)) {
                resList.add((A) element);
            } else {
                break;
            }
        }

        return resList;
    }

    /**
     * Левоассоциативная свертка коллекции. Порядок обхода задается итератором.
     * @param a Коллекция
     * @param f Комбинирующая функция
     * @param start Начальное значение.
     * @return Возвращает результат свертки.
     */
    public static <A, B> B foldl(Iterable<A> a, Function2<A, B, B> f, B start) {
        Iterator it = a.iterator();
        B res = start;

        while (it.hasNext()) {
            Object element = it.next();
            res = f.apply((A) element, res);
        }

        return res;
    }

    /**
     * Правоассоциативная свертка коллекции. Порядок обхода задается итератором.
     * @param a Коллекция
     * @param f Комбинирующая функция
     * @param start Начальное значение.
     * @return Возвращает результат свертки.
     */
    public static <A, B> B foldr(Iterable<A> a, Function2<A, B, B> f, B start) {
        Iterator it = a.iterator();
        ArrayList<A> list = new ArrayList<A>();
        B res = start;

        while (it.hasNext()) {
            Object element = it.next();
            list.add((A) element);
        }

        ListIterator lit = list.listIterator();
        while (lit.hasNext()) {
            lit.next();
        }

        while (lit.hasPrevious()) {
            Object element = lit.previous();
            res = f.apply((A) element, res);
        }

        return res;
    }
}
