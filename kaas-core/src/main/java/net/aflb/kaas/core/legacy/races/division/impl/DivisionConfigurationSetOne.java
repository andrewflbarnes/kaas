/**
 *
 */
package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;

/**
 * <p>
 * Implementation of {@link DivisionConfiguration} used to determine and store the configuration
 * required in order to generate races for the specified number of teams for the first set of
 * races.
 * </p>
 * <p>
 * When the class is instantiated it determines the number and type of {@link RaceGroup}s
 * required in order to run the first set of races. It also determines the names of each of these
 * groups - determining the second set of races is dependant on the names in this set.
 * </p>
 * <p>
 * These race groups are analogous to the group tables which are created in the Kings Ski Club
 * Cheat Sheet.
 * </p>
 * <p>
 * Note that since these are the intial races and are not determined by previous races which have
 * run (only seeding), there is no transformation mapping provided by {@link
 * #getTransformationMapping()}, this simply returns null.
 * </p>
 *
 * @author Barnesly
 */
public class DivisionConfigurationSetOne implements DivisionConfiguration {
	private GroupConfiguration[] groupGrid;
	private String[] groupNames;

	/**
	 * <p>
	 * The constructor which sets the number and types of {@link RaceGroup}s as well as the
	 * group names for the required number of teams. No transformation mapping is set as the
	 * first set of races is not dependant on a previous set.
	 * </p>
	 *
	 * @param numTeams
	 *            the number of teams competing
	 * @throws InvalidNumberOfTeamsException
	 */
	public DivisionConfigurationSetOne(final int numTeams) throws InvalidNumberOfTeamsException {
		// Call the setTeams method as this does some configuration stuff
		setTeams(numTeams);
	}

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#setTeams(int)
	 */
	@Override
	public void setTeams(final int numTeams) throws InvalidNumberOfTeamsException {

		switch (numTeams) {
		case 4:
			this.groupGrid = new GroupConfiguration[] { GroupConfiguration.FOUR_SPECIAL };
			break;
		case 5:
			this.groupGrid = new GroupConfiguration[] { GroupConfiguration.FIVE_SPECIAL };
			break;
		case 6:
			this.groupGrid = new GroupConfiguration[] { GroupConfiguration.SIX_SPECIAL };
			break;
		case 7:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE };
			break;
		case 8:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			break;
		case 9:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE };
			break;
		case 10:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE };
			break;
		case 11:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE };
			break;
		case 12:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 13:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 14:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 15:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE };
			break;
		case 16:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			break;
		case 17:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.TWO };
			break;
		case 18:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 19:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 20:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 21:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.TWO,
					GroupConfiguration.TWO, GroupConfiguration.TWO };
			break;
		case 22:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.TWO, GroupConfiguration.TWO };
			break;
		case 23:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.TWO };
			break;
		case 24:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 25:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 26:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 27:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 28:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 29:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 30:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			break;
		case 31:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE };
			break;
		case 32:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			break;
		default:
			throw new InvalidNumberOfTeamsException("Too many/few teams (" + numTeams + ")");
		}

		switch (this.groupGrid.length) {
		case 1:
			this.groupNames = new String[] { "A" };
			break;
		case 2:
			this.groupNames = new String[] { "A", "B" };
			break;
		case 3:
			this.groupNames = new String[] { "A", "B", "C" };
			break;
		case 4:
			this.groupNames = new String[] { "A", "B", "C", "D" };
			break;
		case 6:
			this.groupNames = new String[] { "A", "D", "B", "E", "C", "F" };
			break;
		case 8:
			this.groupNames = new String[] { "A", "E", "B", "F", "C", "G", "D",
					"H" };
			break;
		}

		if (this.groupGrid.length != this.groupNames.length) {
			throw new InvalidSetupException("Number of groups (" + this.groupGrid.length + ") and" +
					" number of group names (" + this.groupNames.length + ") do not match");
		}
	}

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#getTransformationMapping()
	 */
	@Override
	public String[][] getTransformationMapping() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#getGroupNames()
	 */
	@Override
	public String[] getGroupNames() {
		return this.groupNames;
	}

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#getGroupGrid()
	 */
	@Override
	public GroupConfiguration[] getGroupGrid() {
		return this.groupGrid;
	}
}
