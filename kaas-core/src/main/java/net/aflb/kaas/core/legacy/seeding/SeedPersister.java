package net.aflb.kaas.core.legacy.seeding;

import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.Team;

import java.util.List;

/* TODO Not sure this class is necessary since it just amounts to an additional layer of abstraction
 * around a club and team DAO - we should probably just push these into the
 * SeedRetrieverPersisterManager
 */
/**
 * Defines an interface for the persistence of (@link Team}s and their seeding information utilised
 * by the {@link SeedRetrieverPersisterManager}.
 *
 * Created by Barnesly on 09/05/2017.
 */
public interface SeedPersister {

    /**
     * Initialise the instance. This method should perform any initialisation operations such as
     * opening database connections.
     */
    void init();

    /**
     * "Uninitialise" the instance. This method should perform any closedown operations such as
     * closing database connections.
     */
    void uninit();

    /**
     * Resets the seeding/points associated with all teams in a given league and division.
     *
     * @param league The league which teams should be reset for
     * @param division The division which teams should be reset for
     */
    void resetTeams(String league, String division);

    /**
     * Persists a team either by updating an existing entry if it already exists or adding a new one
     * otherwise.
     *
     * @param team the {@link Team} which should be updated/added
     * @return true if the {@link Team} was successfully persisted, false otherwise
     */
    boolean addOrUpdateTeam(Team team);

    /**
     * Persists a new club to the database. This method does not need to account for a {@link Club}
     * already existing since the {@link SeedRetrieverPersisterManager} checks if the {@link Club}
     * already exists by checking the List of {@link Club}s returned by {@link #getClubs()}
     *
     * @param club the {@link Club} to exist
     * @return true if the {@link Club} was successfully added, false otherwise
     */
    boolean addClub(Club club);

    /**
     * Retrieves details for a {@link Team} if it already exists
     *
     * @param team the {@link Team} to retrieve existing details for
     * @return a {@link Team} object representing the {@link Team} if it already existed, null if it
     * didn't
     */
    Team getExistingTeamDetails(Team team);

    /**
     * The list of already existing {@link Club}s. This should return all {@link Club}s regardless
     * of league to account for the potential of large national scale clubs who could potentially
     * compete at multiple leagues.
     *
     * @return a List of all existing {@link Club}s
     */
    List<Club> getClubs();
}
