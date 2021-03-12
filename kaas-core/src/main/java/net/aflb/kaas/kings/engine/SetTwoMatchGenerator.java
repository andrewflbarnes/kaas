package net.aflb.kaas.kings.engine;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration.InvalidNumberOfTeamsException;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetTwo;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchGenerator;
import net.aflb.kaas.core.spi.MatchResultProcessor;
import net.aflb.kaas.engine.BasicMatchResultProcessor;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
// TODO Rename this class, it is actually a configurer for any set which is not the first.
// TODO Fix copypasta javadoc for the class

/**
 * <p>
 * Class for creating the initial configuration and Matchs based on the settings
 * for each {@link Club} as set in the database. This is a somewhat intensive
 * task and as runs in its own thread.
 * </p>
 *
 * <p>
 * When executed this task retrieves all clubs under the current set league from
 * the database. For each division it then retrieves all competing teams
 * (creating those which don't already exist), ordering by highest seeded first
 * then alphabetically for unseeded.
 * </p>
 *
 * <p>
 * For each set of {@link Team}s a {@link DivisionConfigurationSetTwo} object is
 * used to create the corresponding {@link RaceGroup}s. The total overall
 * ordered list of {@link Match}s for the round is then determined by iterating
 * over each of the sections in each of the groups in each division and pulling
 * out the corresponding Matchs. Finally, this list of Matchs is saved in order to
 * the database.
 * </p>
 *
 * @author Barnesly
 */
// FIXME
@Slf4j
public class SetTwoMatchGenerator implements MatchGenerator {
    private static final int THIS_ROUND_NO = 2;

    private static final int MINIMUM_SET_NO = 2;
    private static final int MIN_SET_TWO_TEAMS = 7;

    /**
     * Parameterised constructor for instantiation.
     *
     * @param matchSetNo  the number of the set to generate Matchs for
     */
    public SetTwoMatchGenerator(int matchSetNo) {
        if (matchSetNo < MINIMUM_SET_NO) {
            throw new SetNumberTooLowException("Param matchSetNo (" + matchSetNo + ") must be " +
                    MINIMUM_SET_NO + " or higher");
        }
    }


    @Override
    public List<Match<?>> generate(Round round) {
        if (!round.virtual()) {
            throw new IllegalStateException("Round for generation must be virtual");
        }

        if (round.subRounds().isEmpty()) {
            throw new IllegalStateException("Round must have a previous set (subround)");
        }

        final Round lastSet = round.subRounds().get(round.subRounds().size() - 1);

        if (!lastSet.isComplete()) {
            throw new IllegalStateException("Previous set (subround) in round is not complete");
        }

        final Map<Round, Map<String, List<Team>>> divisionResults = new HashMap<>();
        final MatchResultProcessor processor = new BasicMatchResultProcessor();

        for (final Round lastSetDivision : lastSet.subRounds()) {
            final Map<String, List<Team>> minileagueResults = new HashMap<>();
            divisionResults.put(lastSetDivision, minileagueResults);
            for (Round minileague : lastSetDivision.subRounds()) {
                // FIXME remove this exceptions catching/rethrow
                try {
                    minileagueResults.put(minileague.name(), processor.getResults(minileague.matches()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // TODO mass of duplicate code in SetOneMatchGenerator
        final var set2 = Round.of(true, "set 2", round.seeds(), round.league());
        round.subRounds().add(set2);

        for (Map.Entry<Round, Map<String, List<Team>>> divisionResult : divisionResults.entrySet()) {
            final Map<String, RaceGroup> raceGroupMap;
            final var lastSetDivision = divisionResult.getKey();
            final var division = lastSetDivision.division()
                    .orElseThrow(() -> new IllegalStateException("division sub round should be for one division exactly"));
            final var setDivision = Round.of(true, division.name(), lastSetDivision.seeds(), round.league());
            // FIXME remove this exceptions catching/rethrow
            try {
                raceGroupMap = generateRaceGroupMap(divisionResult.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            raceGroupMap.values().forEach(g -> {
                final var groupName = g.getGroupName();
                final var groupTeams = Collections.singletonMap(division, g.getTeams());
                final var setDivisionMinileague = Round.of(true, groupName, groupTeams, round.league());
                final var partitions = g.getMatches();
                for (int i = 0; i < partitions.size(); i++) {
                    // TODO nicer way to do this?
                    final var partitionTeams = Collections.singletonMap(division, g.getMatches()
                            .stream()
                            .flatMap(Collection::stream)
                            .flatMap(m -> Stream.of(m.getTeamOne(), m.getTeamTwo()))
                            .distinct()
                            .collect(Collectors.toList()));
                    final var setDivisionMinileaguePartition = Round.of(
                            false, "%s partition %d".formatted(groupName, i + 1), partitionTeams, round.league());
                    setDivisionMinileaguePartition.matches().addAll(partitions.get(i));

                    setDivisionMinileague.subRounds().add(setDivisionMinileaguePartition);
                }
                setDivision.subRounds().add(setDivisionMinileague);
            });
            set2.subRounds().add(setDivision);
        }
        return null;
    }

    // TODO got to be a better way of doing this

    /**
     * Creates a map of {@link RaceGroup}s for the required league, division and
     * {@link Club}s. Each group will have it's Matchs created as well.
     *
     * @return A {@link Map} of {@link RaceGroup}s using the group name as the
     * key.
     * @throws InvalidNumberOfTeamsException If there is any error generating the Match groups or Matchs
     */
    private Map<String, RaceGroup> generateRaceGroupMap(final Map<String, List<Team>> divisionResults) throws
            InvalidNumberOfTeamsException {

        // TODO condensed this down so much might not be worth having in a separate method?
        final Map<String, Team> teamOrder = new HashMap<>();
        divisionResults.forEach((minileague, miniLeagueResults) -> {
            for (int i = 0; i < miniLeagueResults.size(); i++) {
                teamOrder.put((i + 1) + minileague, miniLeagueResults.get(i));
            }
        });

        return createRaceGroups(teamOrder);
    }

    /**
     * <p>
     * The method which determines the Matchs required for the provided Map of
     * {@link Team}s.
     * </p>
     *
     * <p>
     * For each set of positions in the {@code setTwoTransformation} the
     * corresponding teams are retrieved from the map of teams and added to a
     * {@link RaceGroup}. A map of these groups is then returned.
     * </p>
     *
     * @param teamsMap The {@link Map} of {@link Team}s which Match groups are needed
     *                 for
     * @return A {@link Map} of {@link RaceGroup}s based on the previous rounds
     * results
     * @throws InvalidNumberOfTeamsException The {@link RuntimeException} thrown when {@link RaceGroup}s
     *                                       cannot be created for the required number of teams
     */
    public Map<String, RaceGroup> createRaceGroups(Map<String, Team> teamsMap) throws InvalidNumberOfTeamsException {
        // TODO return empty map if the teamMap size is 0

        // Generate the configuration required for this set of matches
        // FIXME?
        final DivisionConfiguration config = new DivisionConfigurationSetTwo(teamsMap.size());
//        switch (this.MatchSetNo) {
//            case 2:
//                config = new DivisionConfigurationSetTwo(teamsMap.size());
//                break;
//            case 3:
//                config = new DivisionConfigurationKnockout(teamsMap.size());
//                break;
//            default:
//                throw new DivisionConfiguration.InvalidSetupException("No division configuration " +
//                        "exists for the required set (" + this.MatchSetNo + ")");
//        }

        final String[][] transformationMapping = config.getTransformationMapping();
        final String[] groupNames = config.getGroupNames();
        final GroupConfiguration[] groupGrid = config.getGroupGrid();

        // Initialise the map we are returning
        // FIXME was ArrayMap
        final Map<String, RaceGroup> raceGroups = new HashMap<>(groupNames.length);

        // If there are no matches to run (zero length groupNames array or a zero length groupGrid
        // array or zero length transformationMapping array) return the empty map
        if (groupNames.length == 0) {
            return raceGroups;
        }

        // Create the Match groups
        int numTeams;
        for (int i = 0, n = transformationMapping.length; i < n; i++) {
            numTeams = transformationMapping[i].length;
            ArrayList<Team> teams = new ArrayList<>(numTeams);

            for (int j = 0; j < numTeams; j++) {
                teams.add(teamsMap.get(transformationMapping[i][j]));
            }

            // Create a new RageGroup and add the teams who are competing to it
            RaceGroup group = new RaceGroup(groupNames[i],
                    teams, groupGrid[i], 999 /* controlId */, THIS_ROUND_NO);

            // Add the group to the map
            raceGroups.put(groupNames[i], group);
        }

        // Create the matches for each group
        List<Team> theseTeams;
        for (int i = 0; i < groupNames.length; i++) {
            theseTeams = raceGroups.get(groupNames[i]).getTeams();
            for (int j = 0, m = theseTeams.size(); j < m; j++) {
                log.debug(theseTeams.get(j).name() + " in Match group " + groupNames[i]);
            }

            // Call the method to generate the Matchs inside the RaceGroup
            raceGroups.get(groupNames[i]).initRaces();

            log.info("Match group " + groupNames[i] + " created, initialised and added");
        }

        // return the map of raceGroups
        return raceGroups;
    }

    /**
     * Exception thrown when an attempt is made to instantiate the class with a set < 2
     *
     * @author Barnesly
     */
    private static class SetNumberTooLowException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Constructor
         *
         * @param reason The reason the exception was raised
         */
        public SetNumberTooLowException(String reason) {
            super(reason);
        }
    }

}
