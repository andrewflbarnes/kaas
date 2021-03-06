/**
 *
 */
package net.aflb.kaas.core.legacy.races.group;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.Team;

// TODO Move the important methods of this out to an interface

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
			{ { 1, 2 } }, {}, {}});
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

	private String value;
	private int[][][] raceGrid;

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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the total number of races under this configuration
	 */
	public int raceCount() {
		return raceGrid[0].length + raceGrid[1].length + raceGrid[2].length;
	}

	/**
	 * @return the number of teams competing under this configuration
	 */
	public int teamCount() {
		return Integer.parseInt(this.value.substring(0, 1));
	}

	// TODO replace use of the below method with something along the lines of
	// getNoRaces(int segment)

	/**
	 * @return the multi-dimensional numeric array defining the races which will
	 *         be run
	 */
	public int[][][] getRaceGrid() {
		return this.raceGrid;
	}

	/**
	 * <p>
	 * The method which generates the list of {@link Match}es which this group's
	 * {@link Team}s will compete in.
	 * </p>
	 *
	 * <p>
	 * <b>IMPORTANT!</b> To prevent issues with team overlap, the returned list
	 * should not be reordered.
	 * </p>
	 *
	 * @param controlId
	 *            the control ID associated with these races
	 * @param roundNo
	 *            the round number associated with these races
	 * @param group
	 *            the name of the group that these races are identified by
	 * @param teams
	 *            the list of {@link Team}s which will be competing in the races
	 * @return the list of {@link Match}es the teams will be competing in
	 */
	public List<Match> createRaces(long controlId, long roundNo, String group,
			List<Team> teams) {
		List<Match> races = new ArrayList<Match>();
//		String division = teams.get(0).getDivision();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < raceGrid[i].length; j++) {
//				Match race = new Match();
//				race.setControlId(controlId);
//				race.setDivision(division);
//				race.setGroup(group);
//				race.setRoundNo(roundNo);
//				// the raceGrid is human readable (i.e. 1 indexed) so we must
//				// take 1 off when determining which listed team is competing
//				race.setTeamOne(teams.get(raceGrid[i][j][0] - 1).getTeamId());
//				race.setTeamTwo(teams.get(raceGrid[i][j][1] - 1).getTeamId());
//
//				races.add(race);

//				log.debug(
//						"race added for control id " + controlId
//								+ ", division " + division + ", round number "
//								+ roundNo + ", teams " + race.getTeamOne()
//								+ " " + race.getTeamTwo());
			}
		}

		return races;
	}

}
