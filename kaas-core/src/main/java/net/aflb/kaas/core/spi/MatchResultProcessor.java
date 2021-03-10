package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;

import java.util.List;

public interface MatchResultProcessor {
    List<Team> getResults(List<Match<?>> matches) throws ManualInterventionException, RacesUnfinishedException;
}
