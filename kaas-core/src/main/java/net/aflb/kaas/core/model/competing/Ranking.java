package net.aflb.kaas.core.model.competing;

import java.util.Comparator;

public record Ranking<T>(
        T entity,
        long rank
) implements Comparable<Ranking<T>> {
    public static final Comparator<Ranking<?>> BY_WEIGHT = (r1, r2) -> (int)(r2.rank - r1.rank);
    public static final Comparator<Ranking<?>> BY_POSITION = (r1, r2) -> (int)(r1.rank - r2.rank);

    @Override
    public int compareTo(Ranking<T> o) {
        return (int)(rank - o.rank);
    }
}
