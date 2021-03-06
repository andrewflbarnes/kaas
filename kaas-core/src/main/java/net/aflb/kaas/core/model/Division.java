package net.aflb.kaas.core.model;

import java.util.Locale;

public record Division(
        long id,
        String name
) implements Comparable<Division> {
    @Override
    public int compareTo(Division o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
