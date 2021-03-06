package net.aflb.kaas.core.model;

import java.util.Locale;

public record Team(
        long id,
        String name
) implements Comparable<Team> {
    @Override
    public int compareTo(Team o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
