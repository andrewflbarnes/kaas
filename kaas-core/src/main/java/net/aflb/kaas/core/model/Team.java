package net.aflb.kaas.core.model;

import java.util.Locale;

public record Team(
        long id,
        String name
) implements Comparable<Team> {
    public static Team of(final String name) {
        // FIXME
        return new Team(System.currentTimeMillis(), name);
    }

    @Override
    public int compareTo(Team o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
