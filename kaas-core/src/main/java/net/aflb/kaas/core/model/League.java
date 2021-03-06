package net.aflb.kaas.core.model;

import java.util.Locale;

public record League(
        long id,
        String name
) implements Comparable<League> {
    @Override
    public int compareTo(League o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
