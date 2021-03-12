package net.aflb.kaas.core.model.competing;

import lombok.Data;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.League;

/**
 * Meant for display purposes providing richer data
 */
@Data
public class MetaMatch {
    private final League league;
    private final Division division;
    private final String minileague;
    private final Match<?> match;

    public MetaMatch(final League league, final Division division, final String minileague, final Match<?> match) {
        this.league = league;
        this.division = division;
        this.minileague = minileague;
        this.match = match;
    }
}
