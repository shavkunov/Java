package ru.spbau.shavkunov.hw5;

/**
 * Интерфейс функции от одной переменной.
 * @param <T> тип аргумента функции.
 * @param <R> тип возвращаемого значения функции.
 */
public interface Function1<T, R> {
    /**
     * Применение функции к аргументу x.
     */
    R apply(T x);

    /**
     * Композиция двух функций от одной переменной.
     */
    default <W> Function1<T, W> compose(Function1<? super R, W> f) {
        return t -> f.apply(apply(t));
    }
}
