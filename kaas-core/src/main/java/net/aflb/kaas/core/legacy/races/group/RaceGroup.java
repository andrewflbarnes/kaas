package net.aflb.kaas.core.legacy.races.group;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private List<Match> matches;
	private GroupConfiguration configuration;
	private long controlId;
	private long roundNo;
	private String groupName;

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
		this.controlId = controlId;
		this.roundNo = roundNo;
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
	 * @return the list of {@link Match}es being run in this group
	 */
	public List<Match> getMatches() {
		return matches;
	}

	/**
	 * @param matches
	 *            the list of {@link Match}es being run in this group
	 */
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	/**
	 * @return the configuration for this group
	 */
	public GroupConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration for this group
	 */
	public void setConfiguration(GroupConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return the control ID the races are being run under
	 */
	public long getControlId() {
		return controlId;
	}

	/**
	 * @param controlId
	 *            the control ID the races are being run under
	 */
	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	/**
	 * @return the round number the races are being run under
	 */
	public long getRoundNo() {
		return roundNo;
	}

	/**
	 * @param roundNo
	 *            the round number the races are being run under
	 */
	public void setRoundNo(long roundNo) {
		this.roundNo = roundNo;
	}

	/**
	 * @return the group name these races are being run under
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the group name these races are being run under
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	public boolean initRaces() {
		if (this.configuration == null) {
			log.warn("group configuration not set");
			return false;
		}
		if (this.teams == null || !hasFullTeamList()) {
			log.warn("teams not set");
			return false;
		}
		if (this.controlId < 1) {
			log.warn("control ID not set");
			return false;
		}
		if (this.roundNo < 1) {
			log.warn("round number not set");
			return false;
		}
		if (this.groupName == null || this.groupName.isEmpty()) {
			log.warn("group name not set");
			return false;
		}

		this.matches = this.configuration.createRaces(this.controlId,
				this.roundNo, this.groupName, this.teams);

		return this.matches.size() > 0;
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
	public List<Match> getRaces(int section) {
		List<Match> sectionRaces = new ArrayList<Match>();

		if (this.matches != null && this.matches.size() > 0 && section < 3) {
			int raceIdx = 0;
			switch (section) {
			case 2:
				raceIdx += this.configuration.getRaceGrid()[1].length;
			case 1:
				raceIdx += this.configuration.getRaceGrid()[0].length;
			default:
			}

			for (int i = 0; i < this.configuration.getRaceGrid()[section].length; i++) {
				sectionRaces.add(this.matches.get(raceIdx));
				raceIdx++;
			}
		}

		return sectionRaces;
	}

	/**
	 * Returns a {@link List} of the {@link Team}s who have competed in this
	 * group and includes their win and DSQ information
	 *
	 * @return the list of teams from this group
	 */
	public List<Team> getTeamWinsAndDsqs() {
		List<Team> teamWinsAndDsqs = new ArrayList<Team>();
		int oldScore;

//		SparseArray<Team> allTeams = new SparseArray<Team>(this.teams.size());
//		Team objTeam;
//		for (int i = 0, n = teams.size(); i < n; i++) {
//			objTeam = teams.get(i);
//			allTeams.put(objTeam.getTeamId(), objTeam);
//		}
//
//		// TODO Do we need these SparseIntArrays? I suspect not
//		// Get a list of unique teams competing
//		SparseIntArray teamWins = new SparseIntArray(6);
//		SparseIntArray teamDsqs = new SparseIntArray(6);
//		int team;
//		Race race;
//
//		for (int i = 0, n = this.matches.size(); i < n; i++) {
//
//			race = this.matches.get(i);
//
//			team = race.getTeamOne();
//			for (int j = 1; j <= 2; j++) {
//				if (j == 2) {
//					team = race.getTeamTwo();
//				}
//
//				// Ensure that the team is added (in the case of no wins)
//				if (teamWins.get(team, TEAM_NOT_FOUND) == TEAM_NOT_FOUND) {
//					teamWins.put(team, 0);
//				}
//				if (teamDsqs.get(team, TEAM_NOT_FOUND) == TEAM_NOT_FOUND) {
//					teamDsqs.put(team, 0);
//				}
//
//				// Update the wins if appropriate
//				if (race.getTeamWin() == j) {
//					oldScore = teamWins.get(team);
//					teamWins.put(team, oldScore + 1);
//				}
//			}
//
//			// Update the DSQs if appropriate
//			if (race.getTeamOneDSQ() != null && !race.getTeamOneDSQ().isEmpty()) {
//				oldScore = teamDsqs.get(team);
//				teamDsqs.put(team, oldScore + 1);
//			}
//			if (race.getTeamTwoDSQ() != null && !race.getTeamTwoDSQ().isEmpty()) {
//				oldScore = teamDsqs.get(team);
//				teamDsqs.put(team, oldScore + 1);
//			}
//
//		}
//
//		int key;
//		for (int i = 0, n = teamWins.size(); i < n; i++) {
//			key = teamWins.keyAt(i);
//			objTeam = allTeams.get(key);
//			objTeam.setSetOneWins(teamWins.get(key));
//			objTeam.setSetOneDsqs(teamDsqs.get(key));
//			teamWinsAndDsqs.add(objTeam);
//		}

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
		List<Team> teamOrder = getTeamWinsAndDsqs();

		// Get the team wins and DSQs as an Array and set the weighting
		Team[] passOne = new Team[teamOrder.size()];
		for (int i = 0, n = teamOrder.size(); i < n; i++) {
			passOne[i] = teamOrder.get(i);
			// FIXME
//			passOne[i].setSetWeighting(passOne[i].getSetOneWins() * 10 - passOne[i].getSetOneDsqs());
		}

		// First pass: rough ordering based on scores and disqualifications
		Team tempTeam;
		for (int i = 0, n = passOne.length; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				// FIXME
//				if (passOne[j].getSetWeighting() > passOne[i].getSetWeighting()) {
//					tempTeam = passOne[i];
//					passOne[i] = passOne[j];
//					passOne[j] = tempTeam;
//				}
			}
		}

		// Second pass: traverse through the array and find drawing teams
		// -two drawing teams, single race:
		// ---see who won the race
		// -two drawing teams, 2 races:
		// ---see who won the races, if drawn require a rerun (butthurt)
		// -three drawing teams, single races (mitigated butthurt):
		// ---techinically we need to rerun all these, but we can't risk a
		// ---repeat result (as it could continue forever. Instead we take the
		// ---highest seeded team as first and then check who won the race
		// ---between the two lower teams.
		// -three drawing teams, 2 races (proper butthurt):
		// ---Why the fuck do you even have a division with 4 teams in it?
		// ---Anyway, as above take the highest seeded, then compare the
		// ---lower teams. If they have drawn require a rerun.
		// -four drawing teams (surprisingly, possible in a group of 6):
		// ---this is too complex for my poor brain. Basically you can
		// ---take into account of how races played out between these 4
		// ---teams with the undrawn teams to determine who is better/worse
		// ---splitting them into a drawn group of 3 and 1 or two drawn groups
		// ---of two.
		// ---Yeah so, I'm not coding the 4 case draw for shit.
		// ---Lob in an exception for this, relay info to user via Toast and
		// ---tell them to "massage" (Mark Booth) the figures.

		Team[] passTwo = new Team[passOne.length];
		List<Team> seedCheck = new ArrayList<Team>(3);
		int whoWon;
		for (int i = 0, n = passOne.length, idx = 0; i < n; i = idx) {
			passTwo[i] = passOne[i];

			// FIXME
//			while (idx < n - 1 && passTwo[i].getSetWeighting() == passOne[idx + 1].getSetWeighting()) {
//				// Add teams if they have the same weighting
//				idx ++;
//				passTwo[idx] = passOne[idx];
//			}

			// We now have idx - i + 1 elements in passTwo with matching weightings
			// in position i to idx
			int numDrawnTeams = idx - i + 1;
			if (numDrawnTeams == 2) {
				try {
					whoWon = whoWon(passTwo[i], passTwo[i + 1]);
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
				throw new MarkBoothException(String.valueOf(numDrawnTeams) + " team draw, massage required");
			}

			idx++;
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
		long teamOneId = teamOne.id();
		long teamTwoId = teamTwo.id();
		// We keep track of the return value as in some cases teams can race
		// each other multiple times in a single set
		int retval = 0;
		int racesFound = 0;
		int unrunRaces = 0;

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
			} else if (match.getTeamOne() == teamOne
					&& match.getTeamTwo() == teamTwo) {
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
	public static List<RaceGroup> racesToList(final List<Match> races, final List<Team> teams) {
		List<RaceGroup> raceGroups = new ArrayList<RaceGroup>();

		// Check that the list of races is not null or empty
		if (races == null || races.size() == 0) {
			return raceGroups;
		}

		// Sort the list of races passed in, this will order them by group
		Collections.sort(races);

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
