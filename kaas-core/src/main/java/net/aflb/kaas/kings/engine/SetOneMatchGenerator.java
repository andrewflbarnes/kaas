package net.aflb.kaas.kings.engine;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.legacy.races.RaceConfigurer;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration.InvalidNumberOfTeamsException;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetOne;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Division;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.spi.MatchGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Class for creating the initial configuration and races based on the settings
 * for each {@link Club} as set in the database. This is a somewhat intensive
 * task and as runs in its own thread.
 * </p>
 *
 * <p>
 * Note that this class is package-private and should be utilised through the
 * {@link RaceConfigurer#generateRaces} method.
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
 * For each set of {@link Team}s a {@link DivisionConfigurationSetOne} object is used
 * to create the corresponding {@link RaceGroup}s. The total overall ordered
 * list of {@link Match}es for the round is then determined by iterating over each
 * of the sections in each of the groups in each division and pulling out the
 * corresponding races. Finally, this list of races is saved in order to the
 * database.
 * </p>
 *
 * @author Barnesly
 */
@Slf4j
public class SetOneMatchGenerator implements MatchGenerator {
    private static final int THIS_ROUND_NO = 1;

    /**
     * Doesn't actually return anything - side effects on the {@code round}
     * @param round
     * @return
     */
    @Override
    public List<Match<?>> generate(final Round round) {

        final var competingTeams = round.seeds();
        if (!round.virtual()) {
            throw new IllegalStateException("Round for generation must be virtual");
        }
        final var set1 = Round.of(true, "set 1", round.seeds(), round.league());
        round.subRounds().add(set1);

        // Create the race groups and races for each division
        for (Map.Entry<Division, List<Team>> kv : competingTeams.entrySet()) {
            final var division = kv.getKey();
            final var teams = kv.getValue();
            Collections.sort(teams);
            log.debug("{} competing seeds", division.name());
            teams.forEach(team -> log.debug("{}", team.name()));
            final var setDivision = Round.of(true, division.name(), Collections.singletonMap(division, teams), round.league());
            // FIXME - holdover from android, we should propagate the exception
            final Map<String, RaceGroup> raceGroupMap;
            try {
                raceGroupMap = generateRaceGroupMap(teams);
            } catch (InvalidNumberOfTeamsException e) {
                log.error("Unable to generate races", e);
                return null;
            }
            final var groups = raceGroupMap.values();
            groups.forEach(g -> {
                // -1 is not a real section as this value is currently ignored
                final var groupName = g.getGroupName();
                final var groupTeams = Collections.singletonMap(division, g.getTeams());
                final var setDivisionMinileague = Round.of(true, groupName, groupTeams, round.league());
                final var partitions = g.getMatches();
                for (int i = 0; i < partitions.size(); i++) {
                    // TODO set partition teams properly (we could reduce over matches but this seems ugly...)
                    final var partitionTeams = groupTeams;
                    final var setDivisionMinileaguePartition = Round.of(
                            false, "%s partition %d".formatted(groupName, i + 1), partitionTeams, round.league());
                    setDivisionMinileaguePartition.matches().addAll(partitions.get(i));

                    setDivisionMinileague.subRounds().add(setDivisionMinileaguePartition);
                }
                setDivision.subRounds().add(setDivisionMinileague);
            });
            set1.subRounds().add(setDivision);
        }

        // Create a single list of races in the order they will be run
        // TODO Relocate this partitioning somewhere else and remove the fake partitioning (only occuring per division)
        //  from above
//        final List<Match<?>> allRaces = new ArrayList<>();
//        Collection<RaceGroup> groups;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0, n = allRaceGroups.size(); j < n; j++) {
//                groups = allRaceGroups.get(j).values();
//                for (RaceGroup group : groups) {
//                    var theseRaces = group.getRaces(i);
//                    for (int k = 0, m = theseRaces.size(); k < m; k++) {
//                        allRaces.add(theseRaces.get(k));
//                    }
//                }
//            }
//        }

        // TODO relocate this somewhere meaningful
        // Check that the public external storage is writable
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            final String PDF_FILE_NAME = "%s_kings_races_set_1.pdf";
//            String pdf = String.format(Locale.ENGLISH, PDF_FILE_NAME, DATE_FORMATTER.format(new Date()));
//            /*
//             * Dies:
//             * new File(Environment.getExternalStoragePublicDirectory("Race_Lists"), pdf)
//             * Good:
//             * new File(context.getExternalFilesDir("Race_Lists"), pdf);
//             * new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), pdf);
//             * new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdf)
//             */
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdf);
//            writer.writeRaceList(allRaces, teamDatasource.getTeams(null, null, null), file, "UTF-8");
//        } else {
//            log.error(LOG_TAG, "External media is not in a writable state");
//            publishProgress("Unable to create PDF!");
//        }

        return null;
    }

    /**
     * <p>
     * The method which determines the races required for the provided list of
     * {@link Team}s.
     * </p>
     *
     * <p>
     * The method first creates the required number of {@link RaceGroup}s, and
     * adds them to a map under the appropriate group name (A..H for round 1,
     * I..VIII for round 2). Teams are then added to each group (highest seeded
     * first, then alphabetically for unseeded) using the below standard until
     * all teams have been added:
     * <ol>
     * <li>For each group, starting at the first and working down, add the
     * highest available team if there is space</li>
     * <li>For each group, starting at the last and working up, add the highest
     * available team if there is space</li>
     * <li>Go back to 1</li>
     * </ol>
     *
     * For example with 8 teams the below groups would be generated:
     * <ul>
     *
     * Group A
     * <ul>
     * <li>1st team</li>
     * <li>6th team</li>
     * <li>7th team</li>
     * </ul>
     *
     * Group B
     * <ul>
     * <li>2nd team</li>
     * <li>5th team</li>
     * <li>8th team</li>
     * </ul>
     *
     * Group C
     * <ul>
     * <li>3rd team</li>
     * <li>4th team</li>
     * </ul>
     *
     * </ul>
     * </p>
     *
     * @param competingTeams
     *            The {@link List} of all {@link Team}s including the number of
     *            teams competing in the division
     * @return A {@link Map} of {@link RaceGroup}s for the league and division
     *         based on the number of teams which are competing as set in the
     *         {@code allClubs} parameter.
     * @throws InvalidNumberOfTeamsException
     *             The {@link RuntimeException} thrown when {@link RaceGroup}s
     *             cannot be created for the required number of teams
     */
    public Map<String, RaceGroup> generateRaceGroupMap(List<Team> competingTeams) throws InvalidNumberOfTeamsException {
        // Initialise the Map we are returning
        // FIXME??? Was android ArrayMap
        Map<String, RaceGroup> raceGroups = new HashMap<>();

        DivisionConfiguration config = new DivisionConfigurationSetOne(competingTeams.size());
        String[] groupNames = config.getGroupNames();
        GroupConfiguration[] groupGrid = config.getGroupGrid();

        // Create each of the current race groups
        for (int i = 0, n = groupNames.length; i < n; i++) {
            log.debug("creating race group " + groupNames[i]);

            RaceGroup group = new RaceGroup(groupNames[i], new ArrayList<>(), groupGrid[i], 999 /* this.control.id() */, THIS_ROUND_NO);
            raceGroups.put(groupNames[i], group);
        }

        int groupIdx = 0;
        int teamIdx = 0;
        int groupStep = 1;

        do {
            // Make sure we are in a valid group
            if (groupIdx >= groupNames.length || groupIdx < 0) {
                groupStep = -groupStep;
                groupIdx += groupStep;
            }

            // Groups may be full, iterate through until we find space in one
            while (raceGroups.get(groupNames[groupIdx]).hasFullTeamList()) {
                groupIdx += groupStep;
                if (groupIdx >= groupNames.length || groupIdx < 0) {
                    groupStep = -groupStep;
                    groupIdx += groupStep;
                }
            }

            // Add the current team to the current group
            raceGroups.get(groupNames[groupIdx]).getTeams().add(competingTeams.get(teamIdx));

            // Increment the team index by 1
            teamIdx++;

            // Move to the next group
            groupIdx += groupStep;

        } while (teamIdx < competingTeams.size());

        // Create the races for each group
        List<Team> theseTeams;
        for (int i = 0, n = groupNames.length; i < n; i++) {
            theseTeams = raceGroups.get(groupNames[i]).getTeams();
            for (int j = 0, m = theseTeams.size(); j < m; j++) {
                log.debug(theseTeams.get(j).name() + " in race group " + groupNames[i]);
            }

            // Call the method to generate the races inside the RaceGroup
            raceGroups.get(groupNames[i]).initRaces();

            log.info("race group " + groupNames[i] + " created, initialised and added");
        }

        return raceGroups;
    }
}
