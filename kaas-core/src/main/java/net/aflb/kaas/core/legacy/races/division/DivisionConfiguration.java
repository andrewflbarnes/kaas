/*
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races.division;

import net.aflb.kaas.kings.engine.GroupConfiguration;
import net.aflb.kaas.kings.engine.RaceGroup;

/**
 * <p>
 * Interface which provides methods for retrieving the configuration required for a set of races.
 * </p>
 * <p>
 * The information which can be retrieved is:
 * <ul>
 * <li>A transformation mapping using {@link #getTransformationMapping()}</li>
 * <li>The number and type of {@link RaceGroup}s required using {@link #getGroupGrid()} (These
 * are returned as an array of {@link GroupConfiguration}s)</li>
 * <li>The names of each group using {@link #getGroupNames()}</li>
 * </ul>
 * </p>
 * <p>
 * This information is determined as necessary and used as required to determine the required set
 * of races.
 * </p>
 *
 * @author Barnesly
 */
public interface DivisionConfiguration {

	/**
	 * <p>
	 * This returns the two dimensional string array of set transformation
	 * mappings from the previous set of races to the current set. If no
	 * mappings are required (e.g. for set one) then null must be returned.
	 * </p>
	 * <p>
	 * Each array of strings represents a group in the new set of races. Each
	 * string in these arrays represents a team as positioned in the previous
	 * set of races in the form (position in group)(group letter).
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * {{"1A", "1B"}, {"2A", "2B"}}
	 * </p>
	 * <p>
	 * This shows that in the set of races being configured there are two
	 * groups, each consisting of two teams. The teams in the first group are
	 * the team which came first in group A and group B in the previous set of
	 * races. The teams in the second group are the teams who came second in
	 * group A and group B in the previous set of races.
	 * </p>
	 *
	 * @return The two dimensional string array transformation for this
	 *         number of teams.
	 */
	String[][] getTransformationMapping();

	/**
	 * @return The String array of group names for this number of teams
	 */
	String[] getGroupNames();

	/**
	 * @return The {@link GroupConfiguration} array for this number of teams
	 */
	GroupConfiguration[] getGroupGrid();

	/**
	 * This method configures the {@link GroupConfiguration} object for the
	 * corresponding number of teams
	 *
	 * @param numTeams
	 *            The number of teams configuration is required for.
	 * @throws InvalidNumberOfTeamsException
	 */
	void setTeams(int numTeams)
			throws InvalidNumberOfTeamsException;

	/**
	 * Thrown when attempting to configure for an invalid number of teams
	 *
	 * @author Barnesly
	 */
	class InvalidNumberOfTeamsException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason
		 *            The {@link String} reason describing why the exception was
		 *            raised
		 */
		public InvalidNumberOfTeamsException(String reason) {
			super(reason);
		}
	}

	/**
     * Thrown when configuration is successful but the configuration produced is invalid.
     * e.g. configuration is produced resulting in 6 groups but only 5 group names are set.
     *
	 * @author Barnesly
	 */
	class InvalidSetupException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason
		 *            The {@link String} reason describing why the exception was
		 *            raised
		 */
		public InvalidSetupException(String reason) {
			super(reason);
		}
	}

}
