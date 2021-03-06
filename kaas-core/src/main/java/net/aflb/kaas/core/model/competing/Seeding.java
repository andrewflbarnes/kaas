package net.aflb.kaas.core.model.competing;

import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.Team;

public record Seeding (
        Team team,
        Club club,
        Division division,
        long rank
) implements Comparable<Seeding> {
    @Override
    public int compareTo(Seeding o) {
        return (int)(rank - o.rank);
    }
}
