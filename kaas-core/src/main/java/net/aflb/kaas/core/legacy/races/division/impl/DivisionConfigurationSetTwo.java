/**
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races.division.impl;

import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;

/**
 * <p>
 * Implementation of {@link DivisionConfiguration} used to determine and store the configuration
 * required in order to generate races for the specified number of teams for the second set of
 * races.
 * </p>
 * <p>
 * When the class is instantiated it determines the number and type of {@link RaceGroup}s
 * required in order to run the first set of races. It also determines the names of each of these
 * groups - determining the final knockouts is dependant on the names in this set.
 * </p>
 * <p>
 * The transformation map is also determined when the class is instantiated. This a simple string
 * array mapping teams from the first set of races to the second set of races based on the
 * position they came within their group. See {@link #getTransformationMapping()} for more details.
 * </p>
 * <p>
 * These race groups are analogous to the group tables which are created in the Kings Ski Club
 * Cheat Sheet as are the transformation mappings.
 * </p>
 *
 * @author Barnesly
 */
public class DivisionConfigurationSetTwo implements DivisionConfiguration {
	private GroupConfiguration[] groupGrid;
	private String[] groupNames;
	private String[][] setTwoTransformation;

	/**
	 * <p>
	 * The constructor which sets the number and types of {@link RaceGroup}s, the transformation
	 * mapping from the previous set of races and the group names for the required number of teams.
	 * </p>
	 *
	 * @param numTeams
	 *            the number of teams competing
	 * @throws InvalidNumberOfTeamsException
	 */
	public DivisionConfigurationSetTwo(final int numTeams) throws InvalidNumberOfTeamsException {
		// Call the setTeams method as this does some configuration stuff
		setTeams(numTeams);
	}

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#getTransformationMapping()
	 */
	@Override
	public String[][] getTransformationMapping() {
		return setTwoTransformation;
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

	/* (non-Javadoc)
	 * @see org.kingsski.wax.configure.races.division.DivisionConfiguration#setTeams(int)
	 */
	@Override
	public void setTeams(final int numTeams) throws InvalidNumberOfTeamsException {

		switch (numTeams) {
        case 4:
        case 5:
        case 6:
            // No second set of races, but we still need to configure some emtpy arrays
            this.groupGrid = new GroupConfiguration[0];
            this.setTwoTransformation = new String[0][0];
            break;
		case 7:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "2A", "2B"},
					{"3A", "3B", "4A"}};
			break;
		case 8:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "2A", "2B"},
					{"3A", "3B", "4A", "4B"}};
			break;
		case 9:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C"},
					{"2A", "1B", "2C"},
					{"3A", "3B", "3C"}};
			break;
		case 10:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C"},
					{"2A", "1B", "2C"},
					{"3A", "3B", "3C", "4A"}};
			break;
		case 11:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "3A"},
					{"2A", "1B", "2C", "3B"},
					{"3C", "4A", "4B"}};
			break;
		case 12:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "2D"},
					{"2A", "1B", "2C", "1D"},
					{"3A", "3B", "3C", "3D"}};
			break;
		case 13:
			// Yup, 12 teams only. 13th place goes to 4th from Group A in round 1
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "2D"},
					{"2A", "1B", "2C", "1D"},
					{"3A", "3B", "3C", "3D"}};
			break;
		case 14:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "2D"},
					{"2A", "1B", "2C", "1D"},
					{"3A", "4B", "3C"},
					{"4A", "3B ", "4D"}};
			break;
		case 15:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "2D"},
					{"2A", "1B", "2C", "1D"},
					{"3A", "3B", "4D"},
					{"4A", "4B", "3C", "4C"}};
			break;
		case 16:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "2B", "1C", "2D"},
					{"2A", "1B", "2C", "1D"},
					{"3A", "3B", "3C", "3D"},
					{"4A", "4B", "4C", "4D"}};
			break;
		case 17:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.TWO };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C"},
					{"1D", "1E", "1F"},
					{"2A", "2B", "2C"},
					{"2D", "2E", "2F"},
					{"3A", "3B", "3C"},
					{"3D", "3E"}};
			break;
		case 18:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C"},
					{"1D", "1E", "1F"},
					{"2A", "2B", "2C"},
					{"2D", "2E", "2F"},
					{"3A", "3B", "3C"},
					{"3D", "3E", "3F"}};
			break;
		case 19:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C"},
					{"1D", "1E", "1F"},
					{"2A", "2B", "2C"},
					{"2D", "2E", "2F"},
					{"4A", "3C", "3E"},
					{"3A", "3B", "3D", "3F"}};
			break;
		case 20:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.THREE, GroupConfiguration.THREE,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C"},
					{"1D", "1E", "1F"},
					{"2A", "2B", "2C"},
					{"2D", "2E", "2F"},
					{"3B", "4A", "3C", "3E"},
					{"3A", "4D", "3D", "3F"}};
			break;
		case 21:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.TWO };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C"},
					{"3E", "3F"}};
			break;
		case 22:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C"},
					{"3E", "3F", "3G"}};
			break;
		case 23:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE};
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G"}};
			break;
		case 24:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"}};
			break;
		case 25:
			// Non-standard configuration
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE,
					GroupConfiguration.TWO };
			//Non standard configuration
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G"},
					{"3H", "4A"}};
			break;
		case 26:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.TWO };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4E"}};
			break;
		case 27:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4E"}};
			break;
		case 28:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4E", "4F"}};
			break;
		case 29:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.TWO };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4C"},
					{"4E", "4F"}};
			break;
		case 30:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.THREE, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4C"},
					{"4E", "4F", "4G"}};
			break;
		case 31:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.THREE };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4C", "4D"},
					{"4E", "4F", "4G"}};
			break;
		case 32:
			this.groupGrid = new GroupConfiguration[] {
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR,
					GroupConfiguration.FOUR, GroupConfiguration.FOUR };
			this.setTwoTransformation = new String[][] {
					{"1A", "1B", "1C", "1D"},
					{"1E", "1F", "1G", "1H"},
					{"2A", "2B", "2C", "2D"},
					{"2E", "2F", "2G", "2H"},
					{"3A", "3B", "3C", "3D"},
					{"3E", "3F", "3G", "3H"},
					{"4A", "4B", "4C", "4D"},
					{"4E", "4F", "4G", "4H"}};
			break;
		default:
            throw new InvalidNumberOfTeamsException("Too many/few teams (" + String.valueOf(numTeams) + ")");
		}

		switch (this.groupGrid.length) {
        case 0:
            this.groupNames = new String[0];
            break;
		case 1:
			this.groupNames = new String[] { "I" };
			break;
		case 2:
			this.groupNames = new String[] { "I", "II" };
			break;
		case 3:
			this.groupNames = new String[] { "I", "II", "III" };
			break;
		case 4:
			this.groupNames = new String[] { "I", "II", "III", "IV" };
			break;
		case 5:
			this.groupNames = new String[] { "I", "II", "III", "IV", "V" };
			break;
		case 6:
			this.groupNames = new String[] { "I", "II", "III", "IV", "V", "VI" };
			break;
		case 7:
			this.groupNames = new String[] { "I", "II", "III", "IV", "V", "VI", "VII" };
			break;
		case 8:
			this.groupNames = new String[] { "I", "II", "III", "IV", "V", "VI", "VII", "VIII" };
			break;
		}

        if (this.groupGrid.length != this.groupNames.length) {
            throw new InvalidSetupException("Number of groups (" + this.groupGrid.length + ") and" +
                    " number of group names (" + this.groupNames.length + ") do not match");
        }
    }
}
