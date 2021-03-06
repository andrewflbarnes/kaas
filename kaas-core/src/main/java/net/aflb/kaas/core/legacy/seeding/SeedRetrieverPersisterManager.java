package net.aflb.kaas.core.legacy.seeding;

import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Team;

import java.util.Collections;
import java.util.List;

/**
 * This class manages the retrieval of seed information for {@link Team}s and persisting them. This
 * is controlled by two components:
 * <ul>
 *     <li>{@link SeedRetriever} implementation - retrieves the seed information for teams</li>
 *     <li>{@link SeedPersister} implementation - persists the seed information for teams</li>
 * </ul>
 *
 * Created by Barnesly on 09/05/2017.
 */
public class SeedRetrieverPersisterManager {

    private SeedRetriever seedRetriever;
    private SeedPersister seedPersister;
    private List<Club> clubList;

    /**
     * Create a {@link SeedRetrieverPersisterManager} instance with the provided retriever and
     * persistence components set
     *
     * @param seedRetriever The {@link SeedRetriever} component this manager will use to retrieve
     *                      seed results
     * @param seedPersister The {@link SeedPersister} component this manager will use to persist the
     *                      {@link Team}s retrieved by the {@link SeedRetriever}
     *
     */
    public SeedRetrieverPersisterManager(SeedRetriever seedRetriever, SeedPersister seedPersister) {
        this.seedRetriever = seedRetriever;
        this.seedPersister = seedPersister;
    }

    /**
     * Retrieves the seeded {@link Team}s using the {@link SeedRetriever} component and persists
     * them using the {@link SeedPersister} component.
     * @return the List of {@link Team}s which were retrieved and persisted.
     */
    public List<Team> execute() {
        seedPersister.init();
        List<Team> teamSeedings = seedRetriever.getSeeds();
        teamSeedings = persistSeededTeams(teamSeedings);
        seedPersister.uninit();
        return teamSeedings;
    }

    /**
     * Persists the List of seeded {@link Team}s.
     * @param teams
     * @return
     */
    public List<Team> persistSeededTeams(List<Team> teams) {

        // We know what league and division we have, reset all scores for
        // existing teams to 0. Doing it this way means we don't retain bad data
        // i.e. teams which did have scores but are no longer seeded, and also
        // means we don't have to delete any teams
        if (teams.size() > 0) {
            Collections.sort(teams);
            clubList = seedPersister.getClubs();
//            seedPersister.resetTeams(teams.get(0).getLeague(), teams.get(0).getDivision());

            String lf = System.getProperty("line.separator");
            StringBuilder refreshErrors = new StringBuilder();

            for (int i = 0, n = teams.size(); i < n; i++) {
                setAndAddClub(teams.get(i));
                if (seedPersister.addOrUpdateTeam(teams.get(i))) {
                    refreshErrors.append("update failed for ").append(teams.get(i).toString()).append(lf);
                }
            }
        }

        return teams;
    }

    /**
     * Sets or guesses the club name and division index of the provided club
     * either by retrieving an existing team from the database or, if it doesn't
     * already exist, working it out based entirely on the team name.
     *
     * @param team
     *            the {@link Team} object which requires the details setting
     * @return the updated {@link Team} object, this is the same object passed
     *         in as the param
     */
    private Team setAndAddClub(Team team) {
        // Search against team name and division in the database. If the team
        // can't be found then work out the details
        Team detailedTeam = seedPersister.getExistingTeamDetails(team);

//        if (detailedTeam != null) {
//            // Set the team details
//            team.setClubName(detailedTeam.getClubName());
//            team.setDivisionIndex(detailedTeam.getDivisionIndex());
//            team.setTeamId(detailedTeam.getTeamId());
//        } else {
//            // The team doesn't already exist - work out the details
//            team.setDivisionIndex(determineDivisionFromName(team.getTeamName()));
//            // Determine the club name from the team name
//            String genClubName = determineClubName(team.getTeamName());
//
//            // Unset the club name associated with the team. We can check if it doesn't get set
//            // to an existing club and "default" it to the determined club name
//            team.setClubName("");
//
//            // Now check if the club exists and set the club name if it does
//            for (int i = 0; i < clubList.size(); i++) {
//                if (clubList.get(i).getClubName().equalsIgnoreCase(genClubName)
//                        || clubList.get(i).getClubShortName().equalsIgnoreCase(genClubName)) {
//                    team.setClubName(clubList.get(i).getClubName());
//                }
//            }
//
//            // If the club didn't exist then create a new club and set the team's club name
//            if (team.getClubName().isEmpty()) {
//                team.setClubName(genClubName);
//
//                // Create the new club
//                Club club = new Club();
//                club.setClubName(genClubName);
//                club.setClubShortName(genClubName);
//                club.setLeague(team.getLeague());
//
//                // Persist the club and add it to the list
//                if (seedPersister.addClub(club)) {
//                    // Add the club to the list for future chceks
//                    clubList.add(club);
//                }
//            }
//        }

        return team;
    }

    /**
     * Determines the division index using the team name. We assume when there is no number in the
     * team name representing the division index then this is the top seeded team in the club and
     * return 1 in that case.
     *
     * @param teamName The name of the team we are determining the division index for
     * @return The division index determined from the team name, 1 if one wasn't found
     */
    private static int determineDivisionFromName(String teamName) {
        try {
            return Integer.parseInt(teamName.substring(teamName.length() - 1));
        } catch (NumberFormatException enf) {
            // If there is no team number associated with this team, they
            // are the first team
            return 1;
        }
    }

    /**
     * Determine the club name from the team name. If there is no nuber representing the division
     * index found in the team name then we assume it is the same as the team name. If we do find
     * a division index then we assume it is everything excluding the team index. A trim is applied
     * in either case.
     *
     * @param teamName The name of the team we are determining the club name for
     * @return The name of the club which we determined
     */
    private static String determineClubName(String teamName) {
        try {
            Integer.parseInt(teamName.substring(teamName.length() - 1));
            // Ignore the suffixing space and numerics
            int end = 1;
            int i = teamName.length() - 2;
            while (i >= 0 && ignoreLetter(teamName.substring(i, i + 1))) {
                end++;
                i--;
            }

            return teamName.substring(0, teamName.length() - end).trim();
        } catch (NumberFormatException enf) {
            // If there is no team number associated with this team, they
            // are the first team
            return teamName.trim();
        }
    }

    /**
     * @param chr
     *            The letter to be checked
     * @return true if chr is null, empty, " " or numeric, false otherwise
     */
    private static boolean ignoreLetter(String chr) {
        if (chr == null || chr.isEmpty()) {
            return true;
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(chr.substring(0, 1));
        } catch (NumberFormatException nfe) {
            if (!chr.substring(0, 1).equals(" ")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the {@link SeedRetriever} component this manager will use
     * @param seedRetriever the {@link SeedRetriever} this manager will use
     */
    public void setSeedRetriever(SeedRetriever seedRetriever) {
        this.seedRetriever = seedRetriever;
    }

    /**
     * Sets the {@link SeedPersister} component this manager will use
     * @param seedPersister the {@link SeedPersister} this manager will use
     */
    public void setSeedPersister(SeedPersister seedPersister) {
        this.seedPersister = seedPersister;
    }
}
