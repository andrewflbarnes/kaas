package net.aflb.kaas.core.spi;

import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.WinDsq;

import java.util.List;

public interface MatchResultProcessor {
    List<Team> getResults(List<Match<?>> matches) throws ManualInterventionException, RacesUnfinishedException;
    WinDsq whoWon(final List<Match<?>> matches, final WinDsq wdOne, final WinDsq wdTwo) throws RaceNotRunException;
}
