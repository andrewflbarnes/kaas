package net.aflb.kaas.core.model;

import java.util.Locale;

public record Club(
        long id,
        String name
) implements Comparable<Club> {

    public static Club of(final String name) {
        // FIXME
        return new Club(System.currentTimeMillis(), name);
    }
    @Override
    public int compareTo(Club o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
