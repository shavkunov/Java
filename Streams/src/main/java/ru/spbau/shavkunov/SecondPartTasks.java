package ru.spbau.shavkunov;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(p -> {
            try {
                return Files.lines(Paths.get(p));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Stream.of();
        }).filter(s -> s.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random rand = new Random();
        return Stream.generate(() -> new Point2D.Double(rand.nextDouble(), rand.nextDouble()))
                     .limit(1_000_000)
                     .filter(p -> p.distance(0.5, 0.5) <= 0.5)
                     .count() / (double) 1_000_000;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet()
                           .stream()
                           .max(Comparator
                                   .comparingInt(entry -> entry.getValue()
                                                               .stream()
                                                               .mapToInt(String::length)
                                                               .sum())).map(Map.Entry::getKey).orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().reduce(new HashMap<>(),
                                     (hm, m) -> {
                                      m.entrySet().forEach(entry -> hm.merge(entry.getKey(),
                                                                             entry.getValue(),
                                                                             (a, b) -> a + b));
                                      return hm;
                                     });
    }
}