/**
 * Kings Ski Club Race Organiser
 */
package net.aflb.kaas.core.legacy.races;

//import android.content.Context;
//import android.widget.Toast;

import net.aflb.kaas.core.legacy.export.PdfMatchSerializer;
import net.aflb.kaas.core.model.competing.Round;

/**
 * <p>
 * Delegation class which generates the races to run.
 * </p>
 * <p>
 * Races are generated based on a {@link Round} and set number. The
 * control defines the league and control id for which to generate races. The
 * set number dictates what kind of implementation is required:
 * <ol>
 * <li>
 * Races are generated based on the number of teams currently set for each club
 * in the CLUBS table under the corresponding control id and league.</li>
 * <li>
 * Races are generated based on the results of the first set of races under the
 * corresponding control id and league.</li>
 * </ol>
 * </p>
 *
 * @author Barnesly
 *
 */
public class RaceConfigurer {

	// FIXME
	/**
	 * Generates the races for the required set under the control id and league
	 * in the {@link Round} parameter.
	 *
//	 * @param context
//	 *            The context which DAO related activities will use.
	 * @param control
	 *            The {@link Round} containing the league and control id
	 *            for race generation is required.
	 * @param raceSet
	 *            Which set of races need to be generated.
	 */
	public static void generateRaces(final Round control, final int raceSet, boolean isKnockouts) {
//		Toast.makeText(context, "Generating set " + String.valueOf(raceSet) + " races...", Toast.LENGTH_SHORT).show();
		switch (raceSet){
		case 1:
			// FIXME .execute()
    		new RaceConfigurerSetOne(new PdfMatchSerializer(), control).doInBackground();
			break;
		case 2:
		case 3:
			// FIXME .execute()
			new RaceConfigurerSetTwo(new PdfMatchSerializer(), control, raceSet, isKnockouts).doInBackground();
			break;
		default:
			throw new InvalidSetException("Invalid race set: " + String.valueOf(raceSet));
		}

	}

	/**
	 * Exception thrown when an invalid round number is passed for configuration
	 *
	 * @author Barnesly
	 */
	public static class InvalidSetException extends RuntimeException  {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor
		 *
		 * @param reason
		 *            The reason the exception was raised
		 */
		public InvalidSetException(String reason) {
			super(reason);
		}
	}

}
