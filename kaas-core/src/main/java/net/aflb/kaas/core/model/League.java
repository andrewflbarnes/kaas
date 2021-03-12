package net.aflb.kaas.core.model;

import java.util.Locale;

public record League(
        long id,
        String name
) implements Comparable<League> {
    public static League of(final String name) {
        // FIXME
        return new League(System.currentTimeMillis(), name);
    }

    @Override
    public int compareTo(League o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
