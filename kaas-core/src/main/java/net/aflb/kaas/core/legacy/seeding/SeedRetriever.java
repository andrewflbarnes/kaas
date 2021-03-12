package net.aflb.kaas.core.legacy.seeding;

import net.aflb.kaas.core.model.Team;

import java.util.List;

/**
 * Defines an interface for the retrieval of {@link Team}s and their seeding information
 *
 * Created by Barnesly on 09/05/2017.
 */
public interface SeedRetriever {

    /**
     * Retrieve the seed information for {@link Team}s
     * @return The List of {@link Team}s and the seed information for them which was retrieved
     */
    List<Team> getSeeds();
}
