package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;

import java.util.List;
import java.util.Map;

public interface MatchGenerator {
    List<Match> generate(final Map<Division, List<Team>> competingTeams);
}
