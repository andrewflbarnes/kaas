package net.aflb.kaas.core.legacy.races.group;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.model.competing.Match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is equivalent to the Tables configuration in the excel cheat sheet
 *
 * @author Barnesly
 *
 */
@Slf4j
public class RaceGroup {
	private static final int TEAM_NOT_FOUND = -1;

	private List<Team> teams;
	private GroupConfiguration configuration;
	private String groupName;
	private List<List<Match<?>>> matches;

	/**
	 * Blank constructor
	 */
	public RaceGroup() {
		// Empty constructor
	}

	/**
	 * @param groupName
	 *            the group name these races are being run under (A..H for round
	 *            1, I..VII for round 2).
	 * @param teams
	 *            the list of {@link Team}s which will be competing in this
	 *            group
	 * @param configuration
	 *            the {@link GroupConfiguration} which defines the races this
	 *            group will compete in
	 * @param controlId
	 *            the control ID under which these races are being run
	 * @param roundNo
	 *            the round number under which these races are being run
	 */
	public RaceGroup(String groupName, List<Team> teams,
			GroupConfiguration configuration, long controlId, int roundNo) {
		this.groupName = groupName;
		this.teams = teams;
		this.configuration = configuration;
	}

	/**
	 * @return the list of {@link Team}s competing in this group
	 */
	public List<Team> getTeams() {
		return teams;
	}

	/**
	 * @param teams
	 *            the list of {@link Team}s competing in this group
	 */
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	/**
	 * @return the group name these races are being run under
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return true if the list of {@link Team}s contains the number of teams as
	 *         required by the {@link GroupConfiguration}, false otherwise.
	 */
	public boolean hasFullTeamList() {
		return (this.teams.size() >= this.configuration.teamCount());
	}

	/**
	 * Method for setting creating and storing the {@link Match}es required for
	 * the provided {@link Team}s under the set {@link GroupConfiguration}
	 *
	 * @return true if the race list was generated, false otherwise.
	 */
	public List<List<Match<?>>> initRaces() {
		if (this.configuration == null) {
			log.warn("group configuration not set");
			return null;
		}
		if (this.teams == null || !hasFullTeamList()) {
			log.warn("teams not set");
			return null;
		}
		if (this.groupName == null || this.groupName.isEmpty()) {
			log.warn("group name not set");
			return null;
		}

//		this.matches = this.configuration.createRaces(this.controlId,
//				this.roundNo, this.groupName, this.teams);


		final var raceGrid = configuration.getRaceGrid();

		final List<List<Match<?>>> matches = new ArrayList<>();
		for (var partition : raceGrid) {
			final var partitionRaces = new ArrayList<Match<?>>();
			matches.add(partitionRaces);
			for (var match : partition) {
				Match<?> race = Match.of(teams.get(match[0] - 1), teams.get(match[1] - 1));
//				race.setControlId(controlId);
//				race.setDivision(division);
//				race.setGroup(group);
//				race.setRoundNo(roundNo);
//				// the raceGrid is human readable (i.e. 1 indexed) so we must
//				// take 1 off when determining which listed team is competing
//				race.setTeamOne(teams.get(raceGrid[i][j][0] - 1).getTeamId());
//				race.setTeamTwo(teams.get(raceGrid[i][j][1] - 1).getTeamId());

				partitionRaces.add(race);

				log.debug("race added for division {division}, teams {} {}",
						race.getTeamOne().name(),
						race.getTeamTwo().name());
			}
		}

		this.matches = matches;
		return matches;
	}

	public List<List<Match<?>>> getMatches() {
		return matches;
	}

	/**
	 * Races for each group are divided into 3 sections so that we can
	 * continually switch between mixed, ladies and boarders. This method
	 * returns the subset of races associated with the required section.
	 *
	 * Note that the section param follows standard programming convention, i.e.
	 * it is 0 indexed
	 *
	 * @param section
	 *            the section of races to be returned
	 * @return the list of races assoiciated with the section
	 */
	// TODO interface annotation making use of private static final ints (for compile time checking)
	public List<Match<?>> getRaces(int section) {
		return null;
	}

	@RequiredArgsConstructor
	private static class WinDsq {
		private final Team team;
		private int wins = 0;
		private int dsqs = 0;
		private int adjustments = 0;

		int weighting() {
			return 100 * wins - 10 * dsqs + adjustments;
		}
	}

	// TODO get from apache commons or similar
	private static boolean isEmpty(final String test) {
		return test == null || test.isBlank();
	}

	/**
	 * Returns a {@link List} of the {@link Team}s who have competed in this
	 * group and includes their win and DSQ information
	 *
	 * @return the list of teams from this group
	 */
	public Map<Team, WinDsq> getTeamWinsAndDsqs() {
		final Map<Team, WinDsq> teamWinsAndDsqs = new HashMap<>(6);

		// FIXME - what if we're not complete? NPE central!
		matches.stream().flatMap(List::stream).forEach(match -> {
			final Team teamOne = match.getTeamOne();
			final Team teamTwo = match.getTeamTwo();

			// Update wins
			teamWinsAndDsqs.get(match.getWinner()).wins += 1;

			// Update the DSQs if appropriate
			teamWinsAndDsqs.computeIfAbsent(teamOne, WinDsq::new);
			if (!isEmpty(match.getTeamOneDsq())) {
				teamWinsAndDsqs.get(teamOne).dsqs += 1;
			}
			teamWinsAndDsqs.computeIfAbsent(teamTwo, WinDsq::new);
			if (!isEmpty(match.getTeamTwoDsq())) {
				teamWinsAndDsqs.get(teamTwo).dsqs += 1;
			}

		});

		return teamWinsAndDsqs;
	}

	//  TODO Move to utility group
	/**
	 * Returns a {@link List} of the {@link Team}s who have competed in this
	 * group ordered by their performance in the first set of races with best
	 * first
	 *
	 * @return the list of teams in this group ordered by performance in the
	 *         first set of races
	 * @throws RacesUnfinishedException
	 *             Exception thrown when races for this group have not been
	 *             completed
	 * @throws MarkBoothException
	 *             Exception thrown when there are more than 3 drawn teams in
	 *             this group
	 */
	public List<Team> getSetOneTeamOrder() throws RacesUnfinishedException, MarkBoothException {
		Map<Team, WinDsq> teamOrder = getTeamWinsAndDsqs();

		// pass one - we have a built in weighting which we sort on giving approximate team standings
		final var passOne = teamOrder.values().stream()
				.sorted(Comparator.comparingInt(WinDsq::weighting))
				.collect(Collectors.toList());

		// Second pass: traverse through the array and find drawing teams
		// -two drawing teams, single race:
		// ---see who won the race
		// -two drawing teams, 2 races:
		// ---see who won the races, if drawn require a rerun
		// -three drawing teams, single races (FIXME):
		// ---technically we need to rerun all these, but we can't risk a
		// ---repeat result (as it could continue forever. Instead we take the
		// ---highest seeded team as first and then check who won the race
		// ---between the two lower teams.
		// -three drawing teams, 2 races (FIXME):
		// ---Why the fuck do you even have a division with 4 teams in it?
		// ---Anyway, as above take the highest seeded, then compare the
		// ---lower teams. If they have drawn require a rerun.
		// -four drawing teams (surprisingly, possible in a group of 6) (TODO):
		// ---this is too complex for my poor brain. Basically you can
		// ---take into account of how races played out between these 4
		// ---teams with the undrawn teams to determine who is better/worse
		// ---splitting them into a drawn group of 3 and 1 or two drawn groups
		// ---of two.
		// ---Yeah so, I'm not coding the 4 case draw for shit.
		// ---Lob in an exception for this, relay info to user and telll them to
		// ---"massage" the figures.

		final var passTwo = new ArrayList<WinDsq>(passOne.size());
		final List<Team> seedCheck = new ArrayList<>(3);
		for (int i = 0, n = passOne.size(), idx = 0; i < n; i = idx) {
			passTwo.clear();
			final var current = passOne.get(i);
			passTwo.add(current);
			final var currentWeighting = current.weighting();
			while (idx < n - 1 && currentWeighting == passOne.get(idx + 1).weighting()) {
				// Add teams if they have the same weighting
				idx ++;
				passTwo.add(passOne.get(idx));
			}

			// We now have idx - i + 1 elements in passTwo with matching weightings
			int numDrawnTeams = passTwo.size();
			//TODO
//			switch (numDrawnTeams) {
//				case 1:
//					continue;
//				case 2:
//				case 3:
//				default:
//			}
//			idx++;
			if (numDrawnTeams == 2) {
				int whoWon;
				try {
					whoWon = whoWon(passTwo.get(i).team, passTwo.get(i + 1).team);
				} catch (RaceNotRunException e) {
					throw new RacesUnfinishedException(e);
				}
				if (whoWon < 0) {
					passTwo[i] = passOne[i + 1];
					passTwo[i + 1] = passOne[i];
				} else if (whoWon == 0 && passTwo[i].compareTo(passTwo[i + 1]) > 0) {
					// TODO Add rerace functionality here instead of seeding
					passTwo[i] = passOne[i + 1];
					passTwo[i + 1] = passOne[i];
				}
			} else if (numDrawnTeams == 3) {
				seedCheck.clear();
				for (int j = i; j < i + 3; j++) {
					seedCheck.add(passTwo[j]);
				}

				// Get the highest seed then the next two arbitrarily
				Collections.sort(seedCheck);
				passTwo[i] = seedCheck.get(0);
				passTwo[i + 1] = seedCheck.get(1);
				passTwo[i + 2] = seedCheck.get(2);

				// See who won of the other two
				int whoWon;
				try {
					whoWon = whoWon(passTwo[i + 1], passTwo[i + 2]);
				} catch (RaceNotRunException e) {
					throw new RacesUnfinishedException(e);
				}

				// Use teamOne as a temporary variable
				// Note: if whoWon > 0 then the teams are already in the correct order
				if (whoWon < 0) {
					tempTeam =  passTwo[i + 1];
					passTwo[i + 1] = passOne[i + 2];
					passTwo[i + 2] = tempTeam;
				} else if (whoWon == 0 && passTwo[i + 1].compareTo(passTwo[i + 2]) > 0) {
					// TODO Add rerace functionality here instead of seeding
					tempTeam =  passTwo[i + 1];
					passTwo[i + 1] = passOne[i + 2];
					passTwo[i + 2] = tempTeam;
				}
			} else if (numDrawnTeams > 3) {
				// More than 3 drawn teams throw exception
				throw new MarkBoothException(numDrawnTeams + " team draw, massage required");
			}

		}

		// We now have the ordered teams in passTwo, just put them into teamOrder
		teamOrder.clear();
		for (int i = 0, n = passTwo.length; i < n; i++) {
			teamOrder.add(passTwo[i]);
		}
		return teamOrder;
	}

	// TODO Move to utility group
	// TODO Use a interface annotation and private static ints for compile time checking
	// WHY THE FUCK DOESN'T THE INTDEF ANNOTATION WORK IN ECLIPSE?!?!?!
	/**
	 * <p>
	 * Given two team IDs, returns the difference between the number of races
	 * won by each. In the case where a race has not been raced, a
	 * {@link RaceNotRunException} is thrown.
	 * </p>
	 *
	 * <p>
	 * Note that in some group sizes teams will race each other team more than
	 * once and hence values are not limited to -1. 0 and 1. In the case
	 * </p>
	 *
	 * @param teamOne
	 *            the id of the first competing team
	 * @param teamTwo
	 *            the id of the second competing team
	 * @return positive value if teamOne won, negative value if teamTwo won, 0
	 *         if the battle has yet to be fought (or, in the case of multiple
	 *         races, finished)
	 * @throws RaceNotRunException
	 *             if there is a race between the two teams which has yet to be
	 *             completed
	 */
	private int whoWon(Team teamOne, Team teamTwo) throws RaceNotRunException {
		final var teamOneId = teamOne.id();
		final var teamTwoId = teamTwo.id();
		// We keep track of the return value as in some cases teams can race
		// each other multiple times in a single set
		int retval = 0;
		int racesFound = 0;
		int unrunRaces = 0;
		// FIXME
		for (int i = 0, n = this.matches.size(); i < n; i++) {
			Match match = this.matches.get(i);
			// TODO sort out duplicated code
			// FIXME for above???
			if (match.getTeamOne() == teamOne && match.getTeamTwo() == teamTwo) {
				retval += match.getWinner() == null
						? 0
						: match.getWinner() == teamOne ? 1 : -1;
				if (match.getWinner() == teamOne) {
					retval += 1;
				} else if (match.getWinner() == teamTwo) {
					retval -= 1;
				} else {
					unrunRaces += 1;
				}
				racesFound += 1;
			} else if (match.getTeamOne() == teamTwo && match.getTeamTwo() == teamOne) {
				if (match.getWinner() == teamOne) {
					retval -= 1;
				} else if (match.getWinner() == teamTwo) {
					retval += 1;
				} else {
					unrunRaces += 1;
				}
				racesFound += 1;
			}
		}

		// If the race doesn't exist throw an unchecked exception - the way we
		// currently run races, all teams in a group compete against each other.
		if (racesFound == 0) {
			throw new RaceNotFoundException("Race for team " + teamOneId
					+ " and team " + teamTwoId + " not found");
		}

		if (unrunRaces > 0) {
			throw new RaceNotRunException(unrunRaces + " unrun races for team " + teamOneId
					+ " and team " + teamTwoId + " not found");
		}

		return retval;
	}

	/**
	 * Unchecked exception thrown when a {@link Match} object cannot be retrieved
	 * or found
	 *
	 * @author Barnesly
	 */
	public class RaceNotFoundException extends IllegalStateException {
		private static final long serialVersionUID = 1L;

		/**
		 * String constructor
		 *
		 * @param reason
		 *            the {@link String} description of the cause
		 */
		public RaceNotFoundException(String reason) {
			super(reason);
		}

		/**
		 * Chaining constructor
		 *
		 * @param t
		 *            the {@link Throwable} we are chaining from
		 */
		public RaceNotFoundException(Throwable t) {
			super(t);
		}
	}

	/**
	 * Unchecked exception thrown when a {@link Match} object has not been run.
	 * This is thrown by the {@code compare()} of
	 * {@link RaceGroup#getSetOneTeamOrder()} must be caught and chained to a
	 * checked {@link RacesUnfinishedException}
	 *
	 * @author Barnesly
	 */
	private class RaceNotRunException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * String constructor
		 *
		 * @param reason
		 *            the {@link String} description of the cause
		 */
		public RaceNotRunException(String reason) {
			super(reason);
		}
	}

	/**
	 * Checked exception thrown when a {@link Match} we try and process races for
	 * the next set but there are unfinished/unrun races
	 *
	 * @author Barnesly
	 */
	public class RacesUnfinishedException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason
		 *            the {@link String} description of the cause
		 */
		public RacesUnfinishedException(String reason) {
			super(reason);
		}

		/**
		 * Chaining constructor
		 *
		 * @param t
		 *            the {@link Throwable} we are chaining from
		 */
		public RacesUnfinishedException(Throwable t) {
			super(t);
		}
	}

	/**
	 * Checked exception thrown when a there are more than 3 drawn teams in this
	 * group of races
	 *
	 * @author Barnesly
	 */
	public class MarkBoothException extends Exception	 {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason
		 *            the {@link String} description of the cause
		 */
		public MarkBoothException(String reason) {
			super(reason);
		}
	}

	// TODO Move to utility class
	/**
	 * Takes a list of {@link Match}es and returns the corresponding
	 * {@link RaceGroup}
	 *
	 * @param races
	 *            The list of {@link Match}es in the group
	 * @param teams
	 *            A list of {@link Team}s containing the teams racing
	 * @return The {@link RaceGroup} equivalent of the {@link Match}es
	 */
	public static List<RaceGroup> racesToList(final List<Match<?>> races, final List<Team> teams) {
		List<RaceGroup> raceGroups = new ArrayList<>();

		// Check that the list of races is not null or empty
		if (races == null || races.size() == 0) {
			return raceGroups;
		}

		// TODO
		// Sort the list of races passed in, this will order them by group
//		Collections.sort(races);

		// FIXME
//		// Store the control id and round no for creating the race groups
//		final int controlId = races.get(0).getControlId();
//		final int roundNo = races.get(0).getRoundNo();
//
//		// Store the team list as a sparse array to make it easier to retrieve teams by their ID
//		SparseArray<Team> teamArray = new SparseArray<Team>(teams.size());
//		for (int i = 0, n = teams.size(); i < n; i++) {
//			teamArray.append(teams.get(i).getTeamId(), teams.get(i));
//		}
//
//		String raceGroupName;
//		String thisRaceGroupName;
//		Team teamToAdd;
//		Race thisRace;
//		int i = 0;
//		while (i < races.size()) {
//			// Initialise the group name variables
//			thisRaceGroupName = races.get(i).getDivision() + " "
//					+ races.get(i).getGroup();
//			raceGroupName = thisRaceGroupName;
//
//			// Create the new race group to store this race and all races in the same group
//			RaceGroup raceGroup = new RaceGroup(raceGroupName, null, null,
//					controlId, roundNo);
//
//			// Create the lists which will store the team and races for this group
//			List<Match> theseRaces = new ArrayList<Match>();
//			List<Team> theseTeams = new ArrayList<Team>();
//
//			// For each race which is in the same group as the current race, add
//			// the race and the teams to the appropriate lists
//			while (thisRaceGroupName.equalsIgnoreCase(raceGroupName) && i < races.size()) {
//				// Add this race to the race list
//				thisRace = races.get(i);
//				theseRaces.add(thisRace);
//
//				// Add team one to the teams list if not already present
//				teamToAdd = teamArray.get(thisRace.getTeamOne());
//				if (!theseTeams.contains(teamToAdd)) {
//					theseTeams.add(teamToAdd);
//				}
//
//				// Add team two to the teams list if not already present
//				teamToAdd = teamArray.get(thisRace.getTeamTwo());
//				if (!theseTeams.contains(teamToAdd)) {
//					theseTeams.add(teamToAdd);
//				}
//
//				// Increment the counter and store the next races group name
//				i++;
//				if (i < races.size()) {
//					thisRaceGroupName = races.get(i).getDivision() + " "
//							+ races.get(i).getGroup();
//				}
//			}
//
//			// We've just processed all races which were in the same group. Add
//			// the lists of teams and races to the race group, then add the race
//			// group to the list of groups which we are returning
//			raceGroup.setTeams(theseTeams);
//			raceGroup.setMatches(theseRaces);
//			raceGroups.add(raceGroup);
//		}

		return raceGroups;
	}
}
