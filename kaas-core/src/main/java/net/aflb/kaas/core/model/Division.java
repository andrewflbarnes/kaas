package net.aflb.kaas.core.model;

import java.util.Comparator;
import java.util.Locale;

public record Division(
        long id,
        String name,
        int rank
) implements Comparable<Division> {
    public static final Comparator<Division> BY_RANK = Comparator.comparingInt(Division::rank);

    public static Division of(final String name, final int rank) {
        // FIXME
        return new Division(System.currentTimeMillis(), name, rank);
    }

    @Override
    public int compareTo(Division o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
