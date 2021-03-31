package net.aflb.kaas.kings.engine;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;

import java.util.Arrays;

/**
 * <p>
 * Takes a list of {@link Team}s who are competing under the same group within a
 * division and returns the corresponding {@link Match}es they will compete in.
 * </p>
 *
 * <p>
 * Each enumerated GroupConfiguration is associated with a multi-dimensional
 * numeric array which defines the races which must be run.For all intents and
 * purposes this is merely an array of numeric pairs (stored as an array), 1 for
 * each race.
 * </p>
 *
 * <p>
 * This array itself is split into 3 sections since each round is split into 3
 * sections (as we run a mix of races i.e. MLBMLBMLB). This simplifies the
 * process later on of determining the overall race order for a round as we can
 * retrieve the race grid and determine how many races are required for each
 * section and which races these are.
 * </p>
 *
 * @author Barnesly
 */
@Slf4j
public enum GroupConfiguration {

    // Each of the race grids is composed of an array containing 3 arrays (1 per
    // section) of race pairs - each themselves a 2 dimensional array
    TWO("2", 2, new int[][][]{
            {{1, 2}},
            {{2, 1}},
            {{1, 2}}}),
    THREE("3", 3, new int[][][]{
            {{1, 2}},
            {{2, 3}},
            {{3, 1}}}),
    FOUR("4", 4, new int[][][]{
            {{1, 2}, {3, 4}},
            {{2, 3}, {4, 1}},
            {{1, 3}, {2, 4}}}),
    FOUR_SPECIAL("4S", 4, new int[][][]{
            {{1, 2}, {3, 4}, {2, 3}, {4, 1}},
            {{1, 3}, {2, 4}, {2, 1}, {4, 3}},
            {{3, 2}, {1, 4}, {3, 1}, {4, 2}}}),
    FIVE_SPECIAL("5S", 5, new int[][][]{
            {{1, 2}, {3, 4}, {4, 5}},
            {{2, 3}, {5, 1}, {4, 2}},
            {{5, 3}, {1, 4}, {2, 5}, {3, 1}}}),
    SIX_SPECIAL("6S", 6, new int[][][]{
            {{1, 2}, {3, 4}, {5, 6}, {2, 3}, {6, 1}},
            {{4, 5}, {3, 1}, {4, 6}, {5, 2}, {1, 4}},
            {{2, 6}, {3, 5}, {5, 1}, {4, 2}, {6, 3}}}),
    KNOCKOUT("2F", 2, new int[][][]{
            {{1, 2}}});

    private final String alias;
    private final int teamCount;
    private final int[][][] grid;

    GroupConfiguration(String alias, int teamCount, int[][][] grid) {
        this.alias = alias;
        this.teamCount = teamCount;
        this.grid = grid;
    }

    /**
     * @return the alias name for this configuration
     */
    public String getName() {
        return alias;
    }

    /**
     * @return the number of teams competing under this configuration
     */
    public int teamCount() {
        return teamCount;
    }

    /**
     * @return the multi-dimensional numeric array defining the races which will
     *         be run
     */
    public int[][][] getRaceGrid() {
        return copy(grid);
    }

    private int[][][] copy(final int[][][] original) {
        final var iLength = original.length;
        final var copy = new int[iLength][][];
        for (int i = 0; i < iLength; i++) {
            final var jLength = original[i].length;
            copy[i] = new int[jLength][];
            for (int j = 0; j < jLength; j++) {
                copy[i][j] = Arrays.copyOf(original[i][j], original[i][j].length);
            }
        }

        return copy;
    }
}
