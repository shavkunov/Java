package ru.spbau.shavkunov.hw5;

public interface Predicate<T> extends Function1<T, Boolean> {
    default Function1<T, Boolean> or(Function1<? super T, Boolean> f) {
        return t -> apply(t) || f.apply(t);
    }

    default Function1<T, Boolean> and(Function1<? super T, Boolean> f) {
        return t -> apply(t) && f.apply(t);
    }

    default Function1<T, Boolean> not() {
        return t -> !apply(t);
    }

    default Function1<T, Boolean> alwaysTrue() {
        return t -> true;
    }

    default Function1<T, Boolean> alwaysFalse() {
        return t -> false;
    }
}
