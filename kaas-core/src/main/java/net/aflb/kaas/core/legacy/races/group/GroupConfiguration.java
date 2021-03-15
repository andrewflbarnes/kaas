/**
 *
 */
package net.aflb.kaas.core.legacy.races.group;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;

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
public class GroupConfiguration {
	// Each of the race grids is composed of an array containing 3 arrays (1 per
	// section) of race pairs - each themselves a 2 dimensional array
	public static final GroupConfiguration TWO = new GroupConfiguration("2", new int[][][] {
			{ { 1, 2 } },
			{ { 2, 1 } },
			{ { 1, 2 } } });
	public static final GroupConfiguration KNOCKOUT = new GroupConfiguration("2F", new int[][][] {
			{ { 1, 2 } }});
	public static final GroupConfiguration THREE = new GroupConfiguration("3", new int[][][] {
			{ { 1, 2 } },
			{ { 2, 3 } },
			{ { 3, 1 } } });
	public static final GroupConfiguration FOUR = new GroupConfiguration("4", new int[][][] {
			{ { 1, 2 }, { 3, 4 } },
			{ { 2, 3 }, { 4, 1 } },
			{ { 1, 3 }, { 2, 4 } } });
	public static final GroupConfiguration FOUR_SPECIAL = new GroupConfiguration("4S", new int[][][] {
			{ { 1, 2 }, { 3, 4 }, { 2, 3 }, { 4, 1 } },
			{ { 1, 3 }, { 2, 4 }, { 2, 1 }, { 4, 3 } },
			{ { 3, 2 }, { 1, 4 }, { 3, 1 }, { 4, 2 } } });
	public static final GroupConfiguration FIVE_SPECIAL = new GroupConfiguration("5S", new int[][][] {
			{ { 1, 2 }, { 3, 4 }, { 4, 5 } },
			{ { 2, 3 }, { 5, 1 }, { 4, 2 } },
			{ { 5, 3 }, { 1, 4 }, { 2, 5 }, { 3, 1 } } });
	public static final GroupConfiguration SIX_SPECIAL = new GroupConfiguration("6S", new int[][][] {
			{ { 1, 2 }, { 3, 4 }, { 5, 6 }, { 2, 3 }, { 6, 1 } },
			{ { 4, 5 }, { 3, 1 }, { 4, 6 }, { 5, 2 }, { 1, 4 } },
			{ { 2, 6 }, { 3, 5 }, { 5, 1 }, { 4, 2 }, { 6, 3 } } });

	private final String value;
	private final int[][][] raceGrid;

	/**
	 * The constructor
	 *
	 * @param value
	 *            the value associated with this configuration. The first digit
	 *            must be the number of teams in this group
	 * @param raceGrid
	 *            the multi-dimensional integer array defining the races
	 *            which will be run for this number of teams
	 */
	private GroupConfiguration(String value, int[][][] raceGrid) {
		this.value = value;
		this.raceGrid = raceGrid;
	}
	/**
	 * @return the number of teams competing under this configuration
	 */
	public int teamCount() {
		return Integer.parseInt(this.value.substring(0, 1));
	}

	/**
	 * @return the multi-dimensional numeric array defining the races which will
	 *         be run
	 */
	public int[][][] getRaceGrid() {
		return this.raceGrid;
	}
}
