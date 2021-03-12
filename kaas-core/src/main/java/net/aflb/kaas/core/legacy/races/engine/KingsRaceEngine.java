package net.aflb.kaas.core.legacy.races.engine;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationKnockout;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetOne;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetTwo;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.model.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/*

N O T E S

When creating races before, we were removing all existing races from the database for the same
race control and set number i.e. ensuring we din't end up with duplicate races defined. This
behaviour MUST be replicated

 */
// TODO Remove use of ArrayMap
@Slf4j
public class KingsRaceEngine implements RaceEngine {

    private static final Pattern NUMERIC = Pattern.compile("^([1-9]+)");

    /*
     *
     *
     *          S E E D I N G   B A S E D
     *
     *
     */
    @Override
    public List<Match<?>> generateRacesFromSeeding(Round control, Map<String, List<Team>> competingTeams)
            throws RaceGenerationFailException {

        // Create the race groups and races for each division
        List<Map<String, RaceGroup>> allRaceGroups = new ArrayList<>(3);
        try {
            for (String division : competingTeams.keySet()) {
                List<Team> divisionCompetingTeams = competingTeams.get(division);

                log.debug(division + " teams competing:");
                Collections.sort(divisionCompetingTeams);
                for (int i = 0, n = divisionCompetingTeams.size(); i < n; i++) {
                    log.debug(divisionCompetingTeams.get(i).toString());
                }

                allRaceGroups.add(generateSeedingRaceGroupMap(divisionCompetingTeams, control));
            }
        } catch (DivisionConfiguration.InvalidNumberOfTeamsException e) {
            // FIXME
//            publishProgress(e.getMessage());
//            return Boolean.FALSE;
            throw new RaceGenerationFailException(e);
        }

        // Create a single list of races in the order they will be run
        //TODO Comments!
        List<Match<?>> allRaces = new ArrayList<>();
        Collection<RaceGroup> groups;
        List<Match<?>> theseRaces;
        for (int i = 0; i < 3; i++) {
            for (int j = 0, n = allRaceGroups.size(); j < n; j++) {
                groups = allRaceGroups.get(j).values();
                for (RaceGroup group : groups) {
                    theseRaces = group.getRaces(i);
                    for (int k = 0, m = theseRaces.size(); k < m; k++) {
                        allRaces.add(theseRaces.get(k));
                        log.debug("SIZE: " + theseRaces.size());
                    }
                }
            }
        }

        return allRaces;
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
     * @throws DivisionConfiguration.InvalidNumberOfTeamsException
     *             The {@link RuntimeException} thrown when {@link RaceGroup}s
     *             cannot be created for the required number of teams
     */
    private Map<String, RaceGroup> generateSeedingRaceGroupMap(List<Team> competingTeams, Round control)
            throws DivisionConfiguration.InvalidNumberOfTeamsException {
        // Initialise the Map we are returning
        // FIXME? was android ArrayMap
        Map<String, RaceGroup> raceGroups = new HashMap<>();

        DivisionConfiguration config = new DivisionConfigurationSetOne(competingTeams.size());
        String[] groupNames = config.getGroupNames();
        GroupConfiguration[] groupGrid = config.getGroupGrid();

        // Create each of the current race groups
        for (int i = 0, n = groupNames.length; i < n; i++) {
            log.debug("creating race group " + groupNames[i]);

            RaceGroup group = new RaceGroup(groupNames[i], new ArrayList<Team>(), groupGrid[i], 999, 1);
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

    /*
     *
     *
     *          R E S U L T S   B A S E D
     *
     *
     */
    @Override
    public List<Match<?>> generateRacesFromResults(Round control, Map<String, List<Match<?>>> previousSetRaces,
                                                   Map<String, List<Team>> teams, int raceSetNo, boolean isKnockouts)
            throws RaceGenerationFailException {

        List<Map<String, RaceGroup>> allRaceGroups = new ArrayList<>(3);

        for (String division : previousSetRaces.keySet()) {
            List<Team> divisionTeams = teams.get(division);
            List<Match<?>> divisionRaces = previousSetRaces.get(division);
            Map<String, RaceGroup> divisionRaceGroup =
                    generateResultsRaceGroupMap(control, divisionTeams, divisionRaces, isKnockouts, raceSetNo);
            allRaceGroups.add(divisionRaceGroup);
        }

        // TODO Put a better check in - we need to understand if there are no races
        // run for this league yet or if we simply have very low numbers of
        // teams competing
        int racesToRun = 0;
        for (int i = 0, n = allRaceGroups.size(); i < n; i++) {
            racesToRun += allRaceGroups.get(i).size();
        }
        if (racesToRun < 1) {
            // FIXME
//            publishProgress("No races created!");
//            return Boolean.FALSE;
            throw new RaceGenerationFailException("TODO");
        }

        // Create a single list of races in the order they will be run
        List<Match<?>> allRaces = new ArrayList<>();
        Collection<RaceGroup> groups;
        List<Match<?>> theseRaces;
        for (int i = 0; i < 3; i++) {
            for (int j = 0, n = allRaceGroups.size(); j < n; j++) {
                groups = allRaceGroups.get(j).values();
                // Easier to use implicit iterators for the Map
                for (RaceGroup group : groups) {
                    theseRaces = group.getRaces(i);
                    for (int k = 0, m = theseRaces.size(); k < m; k++) {
                        allRaces.add(theseRaces.get(k));
                    }
                }
            }
        }


        if (isKnockouts) {
            Collections.sort(allRaces, new Comparator<Match<?>>() {
                @Override
                public int compare(Match<?> raceOne, Match<?> raceTwo) {
                    return -1;
                    // FIXME
//                    int check = raceTwo.getDivision().toUpperCase().toCharArray()[0] -
//                            raceOne.getDivision().toUpperCase().toCharArray()[0];
//                    String divisionLetter;
//
//                    if (check == 0) {
//                        Matcher raceTwoMatcher = NUMERIC.matcher(raceTwo.getGroup());
//                        if (raceTwoMatcher.find()) {
//
//                            Matcher raceOneMatcher = NUMERIC.matcher(raceOne.getGroup());
//                            if (raceOneMatcher.find()) {
//                                return Integer.parseInt(raceTwoMatcher.group())
//                                        - Integer.parseInt(raceOneMatcher.group());
//                            }
//                        }
//
//                        return raceTwo.getGroup().toUpperCase().toCharArray()[0] -
//                                raceOne.getGroup().toUpperCase().toCharArray()[0];
//                    } else {
//                        divisionLetter = raceOne.getDivision().substring(0, 1);
//                        if (divisionLetter.equalsIgnoreCase("L")) {
//                            // We want ladies first
//                            return -1;
//                        } else if (divisionLetter.equalsIgnoreCase("M")) {
//                            // We want mixed last
//                            return 1;
//                        } else if (raceTwo.getDivision().substring(0, 1).equalsIgnoreCase("M")) {
//                            // Again we want mixed last (after boarders)
//                            return -1;
//                        } else {
//                            // We want ladies first (before boarders)
//                            return 1;
//                        }
//                    }
                }
            });
        }

        return allRaces;
    }

    // TODO got to be a better way of doing this
    /**
     * Creates a map of {@link RaceGroup}s for the required league, division and
     * {@link Club}s. Each group will have it's races created as well.
     *
     * @return A {@link Map} of {@link RaceGroup}s using the group name as the
     *         key.
     * @throws RaceGenerationFailException
     *             If there is any error generating the race groups or races
     */
    private Map<String, RaceGroup> generateResultsRaceGroupMap(Round control, List<Team> teamsList, List<Match<?>> racesList, boolean isKnockouts, int raceSetNo) throws
            RaceGenerationFailException {

        List<RaceGroup> raceGroups = RaceGroup.racesToList(racesList, teamsList);

        // Generate the maps of team position to teams
        Map<String, Team> teamOrder;
        try {
            teamOrder = getOrderedTeamMap(raceGroups);
        } catch (RaceGroup.RacesUnfinishedException e) {
            //FIXME
//            throw new RaceGenerationFailException("Unfinished " + division + " race(s)");
            throw new RaceGenerationFailException(e);
        } catch (RaceGroup.MarkBoothException e) {
            if (e.getMessage() == null || e.getMessage().isEmpty()) {
                //FIXME
//                throw new RaceGenerationFailException(division + ": " + UNKNOWN_MARK_BOOTH);
                throw new RaceGenerationFailException(e);
            } else {
                //FIXME
//                throw new RaceGenerationFailException(division + ": " + e.getMessage());
                throw new RaceGenerationFailException(e);
            }
        }

        Map<String, RaceGroup> raceGroupList;
        try {
            raceGroupList = createRaceGroups(control, teamOrder, isKnockouts, raceSetNo);
        } catch (DivisionConfiguration.InvalidNumberOfTeamsException e) {
            //FIXME
//            throw new RaceGenerationFailException(division + ": " + e.getMessage());
            throw new RaceGenerationFailException(e);
        }

        return raceGroupList;
    }

    /**
     * Returns a mapping of {@link Team}s with using their position within the group and the
     * group letter as the key. This allows races to be determined from the transformation mapping
     * provided by the corresponding {@link DivisionConfiguration} implementation.
     *
     * @param raceGroups
     *            the list of {@link RaceGroup}s which we need to determine the map for
     * @return a map of {@link Team}s with the key as their position within the {@link
     * RaceGroup} and the race group name.
     * @throws RaceGroup.RacesUnfinishedException
     * @throws RaceGroup.MarkBoothException
     */
    private static Map<String, Team> getOrderedTeamMap(List<RaceGroup> raceGroups)
            throws RaceGroup.RacesUnfinishedException, RaceGroup.MarkBoothException {
        // Initialise the Map we are returning
        // FIXME??? Was using android ArrayMap
        Map<String, Team> orderedTeamMap = new HashMap<>();

        // Variable declarations for processing
        String groupName;
        List<Team> groupOrderedTeams;
        RaceGroup rg;

        // Loop trough the race groups and retrieve the list of ordered teams,
        // then add these teams to the map using the group letter and position
        // as the key
        for (int i = 0, n = raceGroups.size(); i < n; i++) {
            rg = raceGroups.get(i);
            groupName = rg.getGroupName();
            groupName = groupName.substring(groupName.indexOf(" ") + 1);

            // For roman numeral groups convert them to standard numbers
            switch (groupName) {
                case "A":
                case "B":
                case "C":
                case "D":
                case "E":
                case "F":
                case "G":
                case "H":
                    // Ignore the lettered groups
                    break;
                case "I":
                    groupName = "1";
                    break;
                case "II":
                    groupName = "2";
                    break;
                case "III":
                    groupName = "3";
                    break;
                case "IV":
                    groupName = "4";
                    break;
                case "V":
                    groupName = "5";
                    break;
                case "VI":
                    groupName = "6";
                    break;
                case "VII":
                    groupName = "7";
                    break;
                case "VIII":
                    groupName = "8";
                    break;
                default:
                    throw new DivisionConfiguration.InvalidSetupException("Unrecognised  group " +
                            "name (" + groupName + ")");
            }

            groupOrderedTeams = rg.getSetOneTeamOrder();
            for (int j = 0, m = groupOrderedTeams.size(); j < m; j++) {
                orderedTeamMap.put(String.valueOf(j + 1) + groupName,
                        groupOrderedTeams.get(j));
            }
        }

        // Return the Map of teams
        return orderedTeamMap;
    }

    /**
     * <p>
     * The method which determines the races required for the provided Map of
     * {@link Team}s.
     * </p>
     *
     * <p>
     * For each set of positions in the {@code setTwoTransformation} the
     * corresponding teams are retrieved from the map of teams and added to a
     * {@link RaceGroup}. A map of these groups is then returned.
     * </p>
     *
     * @param teamsMap
     *            The {@link Map} of {@link Team}s which race groups are needed
     *            for
     * @return A {@link Map} of {@link RaceGroup}s based on the previous rounds
     *         results
     * @throws DivisionConfiguration.InvalidNumberOfTeamsException
     *             The {@link RuntimeException} thrown when {@link RaceGroup}s
     *             cannot be created for the required number of teams
     */
    private Map<String, RaceGroup> createRaceGroups(Round control, Map<String, Team> teamsMap, boolean isKnockouts,int raceSetNo) throws DivisionConfiguration.InvalidNumberOfTeamsException {
        // TODO return empty map if the teamMap size is 0

        // Generate the configuration required for this set of races
        DivisionConfiguration config;
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
        if (isKnockouts) {
            config = new DivisionConfigurationKnockout(teamsMap.size());
        } else {
            switch (raceSetNo) {
                case 2:
                    config = new DivisionConfigurationSetTwo(teamsMap.size());
                    break;
                case 3:
                    config = new DivisionConfigurationKnockout(teamsMap.size());
                    break;
                default:
                    throw new DivisionConfiguration.InvalidSetupException("No division configuration " +
                            "exists for the required set (" + raceSetNo + ")");
            }
        }

        String[][] transformationMapping = config.getTransformationMapping();
        String[] groupNames = config.getGroupNames();
        GroupConfiguration[] groupGrid = config.getGroupGrid();

        // Initialise the map we are returning
        // FIXME??? Was using android ArrayMap
        Map<String, RaceGroup> raceGroups = new HashMap<>(groupNames.length);

        // If there are no races to run (zero length groupNames array or a zero length groupGrid
        // array or zero length transformationMapping array) return the empty map
        if (groupNames.length == 0) {
            return raceGroups;
        }

        // Create the race groups
        int numTeams;
        for (int i = 0, n = transformationMapping.length; i < n; i++) {
            numTeams = transformationMapping[i].length;
            ArrayList<Team> teams = new ArrayList<>(numTeams);

            for (int j = 0; j < numTeams; j++) {
                teams.add(teamsMap.get(transformationMapping[i][j]));
            }

            // Create a new RageGroup and add the teams who are competing to it
            RaceGroup group = new RaceGroup(groupNames[i],
                    teams, groupGrid[i], 999, raceSetNo);

            // Add the group to the map
            raceGroups.put(groupNames[i], group);
        }

        // Create the races for each group
        List<Team> theseTeams;
        for (int i = 0; i < groupNames.length; i++) {
            theseTeams = raceGroups.get(groupNames[i]).getTeams();
            for (int j = 0, m = theseTeams.size(); j < m; j++) {
                log.debug(theseTeams.get(j).name() + " in race group " + groupNames[i]);
            }

            // Call the method to generate the races inside the RaceGroup
            raceGroups.get(groupNames[i]).initRaces();

            log.info("race group " + groupNames[i] + " created, initialised and added");
        }

        // return the map of RaceGroups
        return raceGroups;
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
