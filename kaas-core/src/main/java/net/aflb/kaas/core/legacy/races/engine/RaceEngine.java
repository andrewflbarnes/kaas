package net.aflb.kaas.core.legacy.races.engine;

import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.model.Team;

import java.util.List;
import java.util.Map;

public interface RaceEngine {

    /**
     * Generates a List of {@link Match}es based on the provided competing teams
     *
     * @param control        the {@link Round} for these races
     * @param competingTeams a map from divisions to a list of teams competing in the races
     * @return a list of {@link Match}es to be run
     * @throws RaceGenerationFailException if it is not possible to generate the races
     */
    List<Match<?>> generateRacesFromSeeding(Round control, Map<String, List<Team>> competingTeams)
            throws RaceGenerationFailException;

    /**
     * Generate a list of {@link Match}es based on results from a previous set of races
     *
     * @param control          the {@link Round} for these races
     * @param previousSetRaces a map from divisions to a list of the previously run {@link Match}es
     * @param teams            a map from divisions to a list of {@link Team}s competing in the races
     * @param raceSetNo        the number of the set races are being generated for
     * @param isKnockouts      true if this is a knockout round
     * @return a list of {@link Match}es to be run
     * @throws RaceGenerationFailException if it is not possible to generate the races
     */
    List<Match<?>> generateRacesFromResults(Round control, Map<String, List<Match<?>>> previousSetRaces,
                                            Map<String, List<Team>> teams, int raceSetNo, boolean isKnockouts)
            throws RaceGenerationFailException;
}
