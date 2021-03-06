package net.aflb.kaas.core.model;

import java.util.Locale;

public record Club(
        long id,
        String name
) implements Comparable<Club> {
    @Override
    public int compareTo(Club o) {
        return name.toLowerCase(Locale.ROOT).compareTo(o.name.toLowerCase(Locale.ROOT));
    }
}
