package ru.spbau.shavkunov.hw5;

/**
 * Интерфейс функции от двух переменных.
 * @param <T> тип первого аргумента
 * @param <U> тип второго аргумента
 * @param <R> тип возвращаемого значения
 */
public interface Function2<T, U, R> {
    /**
     * Применение функции от аргументов x, y.
     */
    R apply(T x, U y);

    /**
     * Композиция функции от двух переменных с функцией от одной переменной.
     */
    default <E> Function2<T, U, E> compose(Function1<? super R, E> g) {
        return (x, y) -> g.apply(apply(x, y));
    }

    /**
     * Bind первого аргумента.
     * @return возвращает функцию вида: f(_, secondArg)
     */
    default Function1<U, R> bind1(T firstArg) {
        return (secondArg) -> apply(firstArg, secondArg);
    }

    /**
     * Bind второго аргумента
     * @return возвращает функцию вида: f(firstArg, _)
     */
    default Function1<T, R> bind2(U secondArg) {
        return (firstArg) -> apply(firstArg, secondArg);
    }

    /**
     * Каррирование.
     * @return Возвращает функцию от одной переменной, которая возвращает функцию от одной переменной.
     */
    default Function1<T, Function1<U, R>> curry() {
        return (T x) -> (U y) -> apply(x, y);
    }
}
