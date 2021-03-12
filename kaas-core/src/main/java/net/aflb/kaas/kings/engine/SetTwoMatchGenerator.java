/**
 * Kings Ski Club Match Organiser
 */
package net.aflb.kaas.kings.engine;

//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.support.v4.util.ArrayMap;
//import android.util.Log;
//import android.widget.Toast;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.legacy.export.MatchSerializer;
import net.aflb.kaas.core.legacy.races.RaceConfigurer;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration.InvalidNumberOfTeamsException;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationKnockout;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetTwo;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;
import net.aflb.kaas.core.legacy.races.group.RaceGroup.MarkBoothException;
import net.aflb.kaas.core.legacy.races.group.RaceGroup.RacesUnfinishedException;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchGenerator;
import net.aflb.kaas.core.spi.MatchResultProcessor;
import net.aflb.kaas.engine.BasicMatchResultProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
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
 * Note that this class is package-private and should be utilised through the
 * {@link RaceConfigurer#generateRaces(Round, int, boolean)} method.
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
    private boolean isKnockouts;

    private static final String UNKNOWN_MARK_BOOTH = "Too many teams drawn: MASSAGE";
    private static final int MINIMUM_SET_NO = 2;
    private static final int MIN_SET_TWO_TEAMS = 7;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK);

    /**
     * Parameterised constructor for instantiation.
     *
     * @param matchSetNo  the number of the set to generate Matchs for
     * @param isKnockouts true if this round is the final knockout set for determining team position
     */
    public SetTwoMatchGenerator(int matchSetNo, boolean isKnockouts) {
        if (matchSetNo < MINIMUM_SET_NO) {
            throw new SetNumberTooLowException("Param matchSetNo (" + matchSetNo + ") must be " +
                    MINIMUM_SET_NO + " or higher");
        }
        this.isKnockouts = isKnockouts;
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
            // FIXME safer way of getting division - maybe from the round with an Optional?
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
                // -1 is not a real section as this value is currently ignored
                final var groupName = g.getGroupName();
                final var groupTeams = Collections.singletonMap(division, g.getTeams());
                final var setDivisionMinileague = Round.of(true, groupName, groupTeams, round.league());
                final var partitions = g.getMatches();
                for (int i = 0; i < partitions.size(); i++) {
                    // TODO set partition teams properlyand only include the teams we are using (we could reduce over matches but this seems ugly...)
                    final var partitionTeams = groupTeams;
                    final var setDivisionMinileaguePartition = Round.of(
                            false, "%s partition %d".formatted(groupName, i + 1), partitionTeams, round.league());
                    setDivisionMinileaguePartition.matches().addAll(partitions.get(i));

                    setDivisionMinileague.subRounds().add(setDivisionMinileaguePartition);
                }
                setDivision.subRounds().add(setDivisionMinileague);
            });
            set2.subRounds().add(setDivision);
        }

        List<Map<String, RaceGroup>> allRaceGroups = new ArrayList<>(3);
//		try {
//			allRaceGroups.add(generateRaceGroupMap(Division.MIXED));
//			allRaceGroups.add(generateRaceGroupMap(Division.LADIES));
//			allRaceGroups.add(generateRaceGroupMap(Division.BOARD));
//		} catch (MatchGenerationFailException e) {
//			publishProgress(e.getMessag e());
//			return Boolean.FALSE;
//		}


        // TODO Put a better check in - we need to understand if there are no Matchs
        // run for this league yet or if we simply have very low numbers of
        // teams competing
        int racesToRun = 0;
        for (int i = 0, n = allRaceGroups.size(); i < n; i++) {
            racesToRun += allRaceGroups.get(i).size();
        }
        if (racesToRun < 1) {
            // FIXME - holdover from android, propagate an exception
//			publishProgress("No Matchs created!");
            return null;
        }

        // Create a single list of Matchs in the order they will be run
        List<Match<?>> allMatchs = new ArrayList<>();
        Collection<RaceGroup> groups;
        List<Match<?>> theseMatchs;
        for (int i = 0; i < 3; i++) {
            for (int j = 0, n = allRaceGroups.size(); j < n; j++) {
                groups = allRaceGroups.get(j).values();
                // Easier to use implicit iterators for the Map
                for (RaceGroup group : groups) {
                    theseMatchs = group.getRaces(i);
                    for (int k = 0, m = theseMatchs.size(); k < m; k++) {
                        allMatchs.add(theseMatchs.get(k));
                    }
                }
            }
        }

        // Begin the transaction to the database
//		this.raceDatasource.beginTransactionNonExclusive();

        // Delete Matchs for this round if they already exist
//		this.raceDatasource.deleteMatchs(allMatchs.get(0).getControlId(), this.MatchSetNo);

        if (this.isKnockouts) {
            Collections.sort(allMatchs, new Comparator<Match<?>>() {
                @Override
                public int compare(Match<?> matchOne, Match<?> matchTwo) {
//                    int check = matchTwo.getDivision().toUpperCase().toCharArray()[0] -
//                            matchOne.getDivision().toUpperCase().toCharArray()[0];
//                    String divisionLetter;
//
//                    if (check == 0) {
//                        return matchTwo.getGroup().toUpperCase().toCharArray()[0] -
//                                matchOne.getGroup().toUpperCase().toCharArray()[0];
//                    } else {
//                        divisionLetter = matchOne.getDivision().substring(0, 1);
//                        if (divisionLetter.equalsIgnoreCase("L")) {
//                            // We want ladies first
//                            return -1;
//                        } else if (divisionLetter.equalsIgnoreCase("M")) {
//                            // We want mixed last
//                            return 1;
//                        } else if (matchTwo.getDivision().substring(0, 1).equalsIgnoreCase("M")) {
//                            // Again we want mixed last (after boarders)
//                            return -1;
//                        } else {
//                            // We want ladies first (before boarders)
//                            return 1;
//                        }
//                    }
                    return 1;
                }
            });
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
     * @throws MatchGenerationFailException If there is any error generating the Match groups or Matchs
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

//	/**
//	 * This method is overriden specifically to display any error message as a
//	 * {@link Toast} prior to the execute method failing as a result of a
//	 * handling a caught exception or other error.
//	 *
//	 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
//	 */
//	@Override
//	protected void onProgressUpdate(String... messages) {
//		Toast.makeText(getContext(), messages[0], Toast.LENGTH_LONG).show();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//	 */
//	@Override
//	protected void onPostExecute(Boolean success) {
//		if (success) {
//			Toast.makeText(getContext(), "Match configuration completed",
//					Toast.LENGTH_LONG).show();
//		} else {
//			Toast.makeText(getContext(), "Match configuration failed!",
//					Toast.LENGTH_LONG).show();
//		}
//	}

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

        // Generate the configuration required for this set of Matchs
        // TODO gross - pass in a generic implementation of DivisionConfiguration which is
        // instantiated here
        /*
         * public class Blah<T extends DivisionConfiguration>...
         *    private Clazz<T> clazz;
         * public Blah(params..., Class<T> clazz)...
         *    this.clazz = clazz;...
         * T config = clazz.newInstance();
         * config.setupTeams(teamsMap.size());
         *
         * etc.
         */

        // FIXME?
        final DivisionConfiguration config = new DivisionConfigurationSetTwo(teamsMap.size());
//        if (this.isKnockouts) {
//            config = new DivisionConfigurationKnockout(teamsMap.size());
//        } else {
//            switch (this.MatchSetNo) {
//                case 2:
//                    config = new DivisionConfigurationSetTwo(teamsMap.size());
//                    break;
//                case 3:
//                    config = new DivisionConfigurationKnockout(teamsMap.size());
//                    break;
//                default:
//                    throw new DivisionConfiguration.InvalidSetupException("No division configuration " +
//                            "exists for the required set (" + this.MatchSetNo + ")");
//            }
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
     * Exception thrown when an error occurs trying to generate this set of Matchs
     *
     * @author Barnesly
     */
    private class MatchGenerationFailException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Constructor
         *
         * @param reason The reason the exception was raised
         */
        public MatchGenerationFailException(String reason) {
            super(reason);
        }
    }

    /**
     * Exception thrown when an attempt is made to instantiate the class with a set < 2
     *
     * @author Barnesly
     */
    private class SetNumberTooLowException extends RuntimeException {
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
