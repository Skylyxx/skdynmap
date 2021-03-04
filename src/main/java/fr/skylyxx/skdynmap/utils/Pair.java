package fr.skylyxx.skdynmap.utils;

import java.util.Objects;

public final class Pair<T, K> {

    private T firstValue;
    private K secondValue;

    public Pair() {
    }

    public Pair(final T firstValue, final K secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T getFirst() {
        return this.firstValue;
    }

    public K getSecond() {
        return this.secondValue;
    }

    public static <T, K> Pair<T, K> from(final T firstValue, final K secondValue) {
        return new Pair<>(firstValue, secondValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        return Objects.equals(firstValue, that.firstValue) && Objects.equals(secondValue, that.secondValue);
    }

    public boolean equals(final T firstValue, final K secondValue) {
        return this.firstValue == firstValue && this.secondValue == secondValue;
    }
}