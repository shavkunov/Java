package ru.spbau.shavkunov.hw4;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Реализация объекта Maybe, который может хранить, а может не хранить внутри себя объект типа Т.
 */
public class Maybe<T> {
    private T data;

    private Maybe() {
        data = null;
    }

    private Maybe(T t) {
        data = t;
    }

    /**
     * Создаёт новый объект типа Maybe, хранящий в себе значение t
     * @param t значение, которое Maybe будет хранить
     * @param <T> тип значения t
     * @return Возвращает объект Maybe со значением t
     */
    public static <T> Maybe<T> just(T t) {
        return new Maybe(t);
    }

    /**
     * Создает новый объект Maybe, не хранящий в себе данные.
     * @param <T> какого типа данные, могли бы быть в Maybe
     * @return Возвращет объект Maybe.
     */
    public static <T> Maybe<T> nothing() {
        return new Maybe();
    }

    /**
     * Возвращает данные, который хранит объект Maybe.
     * @throws NotPresentException Исключение пробрасывается в случае отсутствия данных.
     */
    public T get() {
        if (!isPresent()) {
            throw new NotPresentException();
        }

        return data;
    }

    /**
     * Возвращает, есть ли в Maybe данные, которые Maybe может хранить.
     */
    public boolean isPresent() {
        return data != null;
    }

    /**
     * Возвращает новый объект Maybe с типом, соответствующим возвращаемому значению функции mapper.
     * @param mapper функция на значении Maybe.
     */
    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (isPresent()) {
            return just(mapper.apply(data));
        }

        return Maybe.<U>nothing();
    }

    /**
     * Чтение числа из строки.
     * @param s Откуда нужно прочитать число.
     * @return Возвращает объект Maybe с числом, если в строке записано число, иначе внутри Maybe будет храниться null.
     */
    public static Maybe<Integer> readNumber(String s) {
        s = s.trim();
        try {
            Integer res = Integer.parseInt(s);
            return Maybe.just(res);
        } catch (Exception e) {
            return Maybe.<Integer>nothing();
        }
    }

    /**
     * Построчное считывание чисел из файла.
     * @return Возвращает ArrayList с соотвествующим наполнением.
     * Если в i-ой строке файла было записано не число, то в i-ой ячейке ArrayList будет Maybe хранящий null, иначе
     * Maybe будет хранить то число, которое было на i-ой строке.
     */
    public static ArrayList<Maybe<Integer>> readFile(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<Maybe<Integer>> list = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            Maybe<Integer> res = Maybe.readNumber(line);
            list.add(res);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * Вывод в файл квадратов чисел. Если в i-ой ячейке ArrayList Maybe хранит null, то в файле на этой же строке будет null.
     * Иначе в i-ой строке файла будет записано квадрат числа, хранящегося в Maybe.
     * @param out Куда будет выводиться квадраты чисел.
     * @param list Формат представления чисел.
     */
    public static void writeToFile(OutputStream out, ArrayList<Maybe<Integer>> list) {
        PrintWriter outFile = new PrintWriter(out);
        for (int i = 0; i < list.size(); i++) {
            Maybe<Integer> current = list.get(i);
            if (current.isPresent()) {
                outFile.println(current.get() * current.get());
            } else {
                outFile.println("null");
            }
        }
        outFile.close();
    }
}