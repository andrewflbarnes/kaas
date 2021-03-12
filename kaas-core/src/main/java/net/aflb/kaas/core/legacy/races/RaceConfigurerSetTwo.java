/**
 * Kings Ski Club Match Organiser
 */
package net.aflb.kaas.core.legacy.races;

//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.support.v4.util.ArrayMap;
//import android.util.Log;
//import android.widget.Toast;

import lombok.extern.slf4j.Slf4j;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration;
import net.aflb.kaas.core.legacy.races.division.DivisionConfiguration.InvalidNumberOfTeamsException;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationKnockout;
import net.aflb.kaas.core.legacy.races.division.impl.DivisionConfigurationSetTwo;
import net.aflb.kaas.core.legacy.races.group.GroupConfiguration;
import net.aflb.kaas.core.legacy.races.group.RaceGroup;
import net.aflb.kaas.core.legacy.races.group.RaceGroup.MarkBoothException;
import net.aflb.kaas.core.legacy.races.group.RaceGroup.RacesUnfinishedException;
import net.aflb.kaas.core.model.Club;
import net.aflb.kaas.core.model.competing.Match;
import net.aflb.kaas.core.model.competing.Round;
import net.aflb.kaas.core.model.Team;
import net.aflb.kaas.core.legacy.export.MatchSerializer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
// TODO Rename this class, it is actually a configurer for any set which is not the first.
// TODO Fix copypasta javadoc for the class
/**
 * <p>
 * Class for creating the initial configuration and Matchs based on the settings
 * for each {@link Club} as set in the database. This is a somewhat intensive
 * task and as runs in its own thread.
 * </p>
 *
 * <p>
 * Note that this class is package-private and should be utilised through the
 * {@link RaceConfigurer#generateRaces(Round, int, boolean)} method.
 * </p>
 *
 * <p>
 * When executed this task retrieves all clubs under the current set league from
 * the database. For each division it then retrieves all competing teams
 * (creating those which don't already exist), ordering by highest seeded first
 * then alphabetically for unseeded.
 * </p>
 *
 * <p>
 * For each set of {@link Team}s a {@link DivisionConfigurationSetTwo} object is
 * used to create the corresponding {@link RaceGroup}s. The total overall
 * ordered list of {@link Match}s for the round is then determined by iterating
 * over each of the sections in each of the groups in each division and pulling
 * out the corresponding Matchs. Finally, this list of Matchs is saved in order to
 * the database.
 * </p>
 *
 * @author Barnesly
 */
// FIXME
	@Slf4j
class RaceConfigurerSetTwo /* extends AsyncTask<Void, String, Boolean> */ {
	// private static final String LOG_TAG = MatchConfigurer.class.toString();
	private Round control;
//	private Context context;
//	private DaoFactory daoFactory;
//	private MatchDao MatchDatasource;
//	private TeamDao teamDatasource;
	private int MatchSetNo;
    private boolean isKnockouts;
    private MatchSerializer writer;

	private static final String UNKNOWN_MARK_BOOTH = "Too many teams drawn: MASSAGE";
	private static final int MINIMUM_SET_NO = 2;
	private static final int MIN_SET_TWO_TEAMS = 7;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK);

    /**
	 * Parameterised constructor for instantiation.
	 *
	 * @param control
	 *            the {@link Round} to configure the Matchs for
     * @param MatchSetNo
     *            the number of the set to generate Matchs for
     * @param isKnockouts
     *            true if this round is the final knockout set for determining team position
	 */
    public RaceConfigurerSetTwo(MatchSerializer writer, Round control, int MatchSetNo, boolean isKnockouts) {
        if (MatchSetNo < MINIMUM_SET_NO){
            throw new SetNumberTooLowException("Param MatchSetNo (" + MatchSetNo + ") must be " +
                    MINIMUM_SET_NO + " or higher");
        }
        this.control = control;
        this.writer = writer;
		this.MatchSetNo = MatchSetNo;
        this.isKnockouts = isKnockouts;
	}

    /**
     * @return the {@link MatchSerializer} which writes Matchs out to file
     */
    public MatchSerializer getWriter() {
        return writer;
    }

    /**
     * @param writer the {@link MatchSerializer} instance which writes Matchs out to file
     */
    public void setWriter(MatchSerializer writer) {
        this.writer = writer;
    }

    /**
	 * @return the {@link Round} which configuration will be created for
	 */
	public Round getControl() {
		return control;
	}

	/**
	 * @param control
	 *            the {@link Round} object for which configuration will be
	 *            created for
	 */
	public void setControl(Round control) {
		this.control = control;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
//	@Override
	protected Boolean doInBackground(Void... params) {

		List<Map<String, RaceGroup>> allRaceGroups = new ArrayList<>(3);
//		try {
//			allRaceGroups.add(generateRaceGroupMap(Division.MIXED));
//			allRaceGroups.add(generateRaceGroupMap(Division.LADIES));
//			allRaceGroups.add(generateRaceGroupMap(Division.BOARD));
//		} catch (MatchGenerationFailException e) {
//			publishProgress(e.getMessag e());
//			return Boolean.FALSE;
//		}

		// TODO Put a better check in - we need to understand if there are no Matchs
		// run for this league yet or if we simply have very low numbers of
		// teams competing
		int racesToRun = 0;
		for (int i = 0, n = allRaceGroups.size(); i < n; i++) {
			racesToRun += allRaceGroups.get(i).size();
		}
		if (racesToRun < 1) {
//			publishProgress("No Matchs created!");
			return Boolean.FALSE;
		}

		// Create a single list of Matchs in the order they will be run
		List<Match<?>> allMatchs = new ArrayList<>();
		Collection<RaceGroup> groups;
		List<Match<?>> theseMatchs;
		for (int i = 0; i < 3; i++) {
			for (int j = 0, n = allRaceGroups.size(); j < n; j++) {
				groups = allRaceGroups.get(j).values();
				// Easier to use implicit iterators for the Map
				for (RaceGroup group : groups) {
					theseMatchs = group.getRaces(i);
					for (int k = 0, m = theseMatchs.size(); k < m; k++) {
						allMatchs.add(theseMatchs.get(k));
					}
				}
			}
		}

		// Begin the transaction to the database
//		this.raceDatasource.beginTransactionNonExclusive();

		// Delete Matchs for this round if they already exist
//		this.raceDatasource.deleteMatchs(allMatchs.get(0).getControlId(), this.MatchSetNo);

        if (this.isKnockouts) {
            Collections.sort(allMatchs, new Comparator<Match<?>>() {
                @Override
                public int compare(Match<?> matchOne, Match<?> matchTwo) {
//                    int check = matchTwo.getDivision().toUpperCase().toCharArray()[0] -
//                            matchOne.getDivision().toUpperCase().toCharArray()[0];
//                    String divisionLetter;
//
//                    if (check == 0) {
//                        return matchTwo.getGroup().toUpperCase().toCharArray()[0] -
//                                matchOne.getGroup().toUpperCase().toCharArray()[0];
//                    } else {
//                        divisionLetter = matchOne.getDivision().substring(0, 1);
//                        if (divisionLetter.equalsIgnoreCase("L")) {
//                            // We want ladies first
//                            return -1;
//                        } else if (divisionLetter.equalsIgnoreCase("M")) {
//                            // We want mixed last
//                            return 1;
//                        } else if (matchTwo.getDivision().substring(0, 1).equalsIgnoreCase("M")) {
//                            // Again we want mixed last (after boarders)
//                            return -1;
//                        } else {
//                            // We want ladies first (before boarders)
//                            return 1;
//                        }
//                    }
					return 1;
                }
            });
        }

//		// Add the new Matchs
//		for (int i = 0, n = allMatchs.size(); i < n; i++) {
//			this.raceDatasource.addMatch(allMatchs.get(i));
//		}
//
//		// Mark the transaction to the database as successful and end it
//		this.raceDatasource.setTransactionSuccessful();
//		this.raceDatasource.endTransaction();

        // Check that the public external storage is writable
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            final String PDF_FILE_NAME = "%s_kings_Matchs_set_%d.pdf";
//            String pdf = String.format(Locale.ENGLISH, PDF_FILE_NAME, DATE_FORMATTER.format(new Date()), MatchSetNo);
//            /*
//             * Dies:
//             * new File(Environment.getExternalStoragePublicDirectory("Match_Lists"), pdf)
//             * Good:
//             * new File(context.getExternalFilesDir("Match_Lists"), pdf);
//             * new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), pdf);
//             * new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdf)
//             */
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdf);
//            writer.writeMatchList(allMatchs, this.teamDatasource.getTeams(null, null, null), file, "UTF-8");
//        } else {
//            log.error(LOG_TAG, "External media is not in a writable state");
//            publishProgress("Unable to create PDF!");
//        }
//
//        this.teamDatasource.close();
//        this.raceDatasource.close();

        return Boolean.TRUE;
	}

	// TODO got to be a better way of doing this
	/**
	 * Creates a map of {@link RaceGroup}s for the required league, division and
	 * {@link Club}s. Each group will have it's Matchs created as well.
	 *
	 * @param division
	 *            The division to create the Matchs and groups for.
	 * @return A {@link Map} of {@link RaceGroup}s using the group name as the
	 *         key.
	 * @throws MatchGenerationFailException
	 *             If there is any error generating the Match groups or Matchs
	 */
	private Map<String, RaceGroup> generateRaceGroupMap(String division) throws
			MatchGenerationFailException {

//		List<Team> teamsList = this.teamDatasource.getTeamsByDivision(division);

		// Get the list of Matchs from the previous set
//		List<Match> MatchsList = this.raceDatasource.getMatchs(
//				this.control.id(), division, this.MatchSetNo - 1);

//		List<RaceGroup> raceGroups = RaceGroup.racesToList(racesList, teamsList);

		// Generate the maps of team position to teams
//		Map<String, Team> teamOrder;
//		try {
//			teamOrder = getOrderedTeamMap(raceGroups);
//		} catch (RacesUnfinishedException e) {
//			throw new MatchGenerationFailException("Unfinished " + division
//					+ " Match(s)");
//		} catch (MarkBoothException e) {
//			if (e.getMessage() == null || e.getMessage().isEmpty()) {
//				throw new MatchGenerationFailException(division + ": "
//						+ UNKNOWN_MARK_BOOTH);
//			} else {
//				throw new MatchGenerationFailException(division + ": "
//						+ e.getMessage());
//			}
//		}

		Map<String, RaceGroup> RaceGroupList = null;
//		try {
//			RaceGroupList = createRaceGroups(teamOrder);
//		} catch (InvalidNumberOfTeamsException e) {
//			throw new MatchGenerationFailException(division + ": "
//					+ e.getMessage());
//		}

		return RaceGroupList;
	}

//	/**
//	 * This method is overriden specifically to display any error message as a
//	 * {@link Toast} prior to the execute method failing as a result of a
//	 * handling a caught exception or other error.
//	 *
//	 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
//	 */
//	@Override
//	protected void onProgressUpdate(String... messages) {
//		Toast.makeText(getContext(), messages[0], Toast.LENGTH_LONG).show();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//	 */
//	@Override
//	protected void onPostExecute(Boolean success) {
//		if (success) {
//			Toast.makeText(getContext(), "Match configuration completed",
//					Toast.LENGTH_LONG).show();
//		} else {
//			Toast.makeText(getContext(), "Match configuration failed!",
//					Toast.LENGTH_LONG).show();
//		}
//	}

	/**
	 * Returns a mapping of {@link Team}s with using their position within the group and the
	 * group letter as the key. This allows Matchs to be determined from the transformation mapping
	 * provided by the corresponding {@link DivisionConfiguration} implementation.
	 *
	 * @param RaceGroups
	 *            the list of {@link RaceGroup}s which we need to determine the map for
	 * @return a map of {@link Team}s with thei key as their position withing the {@link
	 * RaceGroup} and the Match group name.
	 * @throws RacesUnfinishedException
	 * @throws MarkBoothException
	 */
	private static Map<String, Team> getOrderedTeamMap(List<RaceGroup> RaceGroups)
			throws RacesUnfinishedException, MarkBoothException {
		// Initialise the Map we are returning
		// FIXME was ArrayMap
		Map<String, Team> orderedTeamMap = new HashMap<>();

		// Variable declarations for processing
		String groupName;
		List<Team> groupOrderedTeams;
		RaceGroup rg;

		// Loop trough the Match groups and retrieve the list of ordered teams,
		// then add these teams to the map using the group letter and position
		// as the key
		for (int i = 0, n = RaceGroups.size(); i < n; i++) {
			rg = RaceGroups.get(i);
			groupName = rg.getGroupName();
			groupName = groupName.substring(groupName.indexOf(" ") + 1);

            // For roman numeral groups convert them to standard numbers
            switch (groupName) {
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "H":
                // Ignore the lettered groups
                break;
            case "I":
                groupName = "1";
                break;
            case "II":
                groupName = "2";
                break;
            case "III":
                groupName = "3";
                break;
            case "IV":
                groupName = "4";
                break;
            case "V":
                groupName = "5";
                break;
            case "VI":
                groupName = "6";
                break;
            case "VII":
                groupName = "7";
                break;
            case "VIII":
                groupName = "8";
                break;
            default:
                throw new DivisionConfiguration.InvalidSetupException("Unrecognised  group " +
                        "name (" + groupName + ")");
            }

			groupOrderedTeams = rg.getSetOneTeamOrder();
			for (int j = 0, m = groupOrderedTeams.size(); j < m; j++) {
				orderedTeamMap.put(String.valueOf(j + 1) + groupName,
						groupOrderedTeams.get(j));
			}
		}

		// Return the Map of teams
		return orderedTeamMap;
	}

	/**
	 * <p>
	 * The method which determines the Matchs required for the provided Map of
	 * {@link Team}s.
	 * </p>
	 *
	 * <p>
	 * For each set of positions in the {@code setTwoTransformation} the
	 * corresponding teams are retrieved from the map of teams and added to a
	 * {@link RaceGroup}. A map of these groups is then returned.
	 * </p>
	 *
	 * @param teamsMap
	 *            The {@link Map} of {@link Team}s which Match groups are needed
	 *            for
	 * @return A {@link Map} of {@link RaceGroup}s based on the previous rounds
	 *         results
	 * @throws InvalidNumberOfTeamsException
	 *             The {@link RuntimeException} thrown when {@link RaceGroup}s
	 *             cannot be created for the required number of teams
	 */
	public Map<String, RaceGroup> createRaceGroups(Map<String, Team> teamsMap) throws InvalidNumberOfTeamsException {
        // TODO return empty map if the teamMap size is 0

        // Generate the configuration required for this set of Matchs
		DivisionConfiguration config;
        // TODO gross - pass in a generic implementation of DivisionConfiguration which is
        // instantiated here
        /*
         * public class Blah<T extends DivisionConfiguration>...
         *    private Clazz<T> clazz;
         * public Blah(params..., Class<T> clazz)...
         *    this.clazz = clazz;...
         * T config = clazz.newInstance();
         * config.setupTeams(teamsMap.size());
         *
         * etc.
         */
        if (this.isKnockouts) {
            config = new DivisionConfigurationKnockout(teamsMap.size());
        } else {
            switch (this.MatchSetNo) {
                case 2:
                    config = new DivisionConfigurationSetTwo(teamsMap.size());
                    break;
                case 3:
                    config = new DivisionConfigurationKnockout(teamsMap.size());
                    break;
                default:
                    throw new DivisionConfiguration.InvalidSetupException("No division configuration " +
                            "exists for the required set (" + this.MatchSetNo + ")");
            }
        }

		String[][] transformationMapping = config.getTransformationMapping();
		String[] groupNames = config.getGroupNames();
		GroupConfiguration[] groupGrid = config.getGroupGrid();

		// Initialise the map we are returning
		// FIXME was ArrayMap
		Map<String, RaceGroup> raceGroups = new HashMap<>(groupNames.length);

		// If there are no Matchs to run (sero length groupNames array or a zero length groupGrid
		// array or zero length transformationMapping array) return the empty map
		if (groupNames.length == 0) {
			return raceGroups;
		}

		// Create the Match groups
		int numTeams;
		for (int i = 0, n = transformationMapping.length; i < n; i++) {
			numTeams = transformationMapping[i].length;
			ArrayList<Team> teams = new ArrayList<>(numTeams);

			for (int j = 0; j < numTeams; j++) {
				teams.add(teamsMap.get(transformationMapping[i][j]));
			}

			// Create a new RageGroup and add the teams who are competing to it
			RaceGroup group = new RaceGroup(groupNames[i],
					teams, groupGrid[i], 999, this.MatchSetNo);

			// Add the group to the map
			raceGroups.put(groupNames[i], group);
		}

		// Create the Matchs for each group
		List<Team> theseTeams;
		for (int i = 0; i < groupNames.length; i++) {
			theseTeams = raceGroups.get(groupNames[i]).getTeams();
			for (int j = 0, m = theseTeams.size(); j < m; j++) {
				log.debug(theseTeams.get(j).name() + " in Match group " + groupNames[i]);
			}

			// Call the method to generate the Matchs inside the RaceGroup
			raceGroups.get(groupNames[i]).initRaces();

			log.info("Match group " + groupNames[i] + " created, initialised and added");
		}

		// return the map of raceGroups
		return raceGroups;
	}

	/**
	 * Exception thrown when an error occurs trying to generate this set of Matchs
	 *
	 * @author Barnesly
	 */
	private class MatchGenerationFailException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason The reason the exception was raised
		 */
		public MatchGenerationFailException(String reason) {
			super(reason);
		}
	}

	/**
	 * Exception thrown when an attempt is made to instantiate the class with a set < 2
	 *
	 * @author Barnesly
	 */
	private class SetNumberTooLowException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason The reason the exception was raised
		 */
		public SetNumberTooLowException(String reason) {
			super(reason);
		}
	}

}
